package com.oscarfndez.gamescollection.adapters.rest.controllers;

import com.oscarfndez.gamescollection.adapters.rest.dtos.PlatformDto;
import com.oscarfndez.gamescollection.adapters.rest.dtos.mappers.PlatformModelDtoMapper;
import com.oscarfndez.gamescollection.core.services.PlatformService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@PreAuthorize("@authorizationService.hasRole('ADMIN')")
public class PlatformController {

    private final PlatformModelDtoMapper platformModelDtoMapper;
    private final PlatformService platformService;

    @GetMapping("/api/platform")
    public ResponseEntity<PlatformDto> loadPlatform(@RequestParam final UUID id) {

        return new ResponseEntity<>(
                platformModelDtoMapper.mapToDTO(platformService.retrieveOne(id))
                , HttpStatus.OK);
    }

    @GetMapping("/api/platform/all")
    public ResponseEntity<List<PlatformDto>> loadAllPlatforms() {
        return new ResponseEntity<>(
                platformService.retrieveAny().stream().map(platformModelDtoMapper::mapToDTO).collect(Collectors.toList())
                , HttpStatus.OK);
    }

    @PostMapping("/api/platform")
    public ResponseEntity<PlatformDto> createPlatform(@RequestBody PlatformDto platformDto) {
        return new ResponseEntity<>(
                platformModelDtoMapper.mapToDTO(platformService.createPlatform(platformDto.getName(), platformDto.getDescription())
                ), HttpStatus.CREATED);
    }

    @PutMapping("/api/platform")
    public ResponseEntity<PlatformDto> updatePlatform(@RequestParam final UUID id, @RequestBody PlatformDto platformDto) {
        return new ResponseEntity<>(
                platformModelDtoMapper.mapToDTO(platformService.updatePlatform(id, platformDto.getName(), platformDto.getDescription())
                ), HttpStatus.CREATED);
    }

    @DeleteMapping("/api/platform")
    public ResponseEntity<Void> deletePlatform(@RequestParam final UUID id) {
        platformService.deleteOne(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}
