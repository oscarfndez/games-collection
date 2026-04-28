package com.oscarfndez.inventory.adapters.rest.controllers;

import com.oscarfndez.inventory.adapters.rest.dtos.GameItemDto;
import com.oscarfndez.inventory.adapters.rest.dtos.mappers.GameItemModelDtoMapper;
import com.oscarfndez.inventory.core.services.GameItemService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping({"/api/collection", "/gamesCollection/api/collection"})
@AllArgsConstructor
@PreAuthorize("@authorizationService.hasRole('USER')")
public class GameItemController {

    private final GameItemService gameItemService;
    private final GameItemModelDtoMapper gameItemModelDtoMapper;

    @GetMapping
    public ResponseEntity<List<GameItemDto>> loadCollection(HttpServletRequest request) {
        UUID userId = authenticatedUserId(request);
        return ResponseEntity.ok(gameItemService.retrieveByUserId(userId)
                .stream()
                .map(gameItemModelDtoMapper::mapToDTO)
                .toList());
    }

    @PostMapping
    public ResponseEntity<GameItemDto> addToCollection(@RequestBody GameItemDto gameItemDto,
            HttpServletRequest request) {
        UUID userId = authenticatedUserId(request);
        return new ResponseEntity<>(
                gameItemModelDtoMapper.mapToDTO(gameItemService.addToCollection(
                        userId,
                        gameItemDto.getGameId(),
                        gameItemDto.getPlatformId()
                )),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteFromCollection(@RequestParam UUID id, HttpServletRequest request) {
        UUID userId = authenticatedUserId(request);
        gameItemService.removeFromCollection(id, userId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    private UUID authenticatedUserId(HttpServletRequest request) {
        Object userId = request.getAttribute("authenticatedUserId");
        if (userId instanceof UUID authenticatedUserId) {
            return authenticatedUserId;
        }

        throw new IllegalArgumentException("JWT must include user_id claim.");
    }
}
