package com.oscarfndez.framework.core.services.auth.impl;

import com.oscarfndez.framework.core.services.auth.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {
    @Value("${token.signing.key}")
    private String jwtSigningKey;
    @Override
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public String extractRole(String token) {
        return extractRoles(token).stream().findFirst().orElse(null);
    }

    @Override
    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);

        Object roleClaim = claims.get("role");
        if (roleClaim instanceof String role) {
            return List.of(role);
        }

        Object rolesClaim = claims.get("roles");
        if (rolesClaim instanceof Collection<?> roles) {
            return roles.stream()
                    .map(Object::toString)
                    .filter(Objects::nonNull)
                    .toList();
        }

        Object authoritiesClaim = claims.get("authorities");
        if (authoritiesClaim instanceof Collection<?> authorities) {
            return authorities.stream()
                    .map(Object::toString)
                    .filter(Objects::nonNull)
                    .toList();
        }

        return List.of();
    }

    @Override
    public UUID extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        Object userIdClaim = claims.get("user_id");

        if (userIdClaim == null) {
            return null;
        }

        return UUID.fromString(userIdClaim.toString());
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put(
                "role",
                userDetails.getAuthorities().stream()
                        .findFirst()
                        .map(Object::toString)
                        .orElse(null)
        );
        return generateToken(extraClaims, userDetails);
    }

    @Override
    public boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
