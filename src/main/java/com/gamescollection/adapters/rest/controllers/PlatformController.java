package com.gamescollection.adapters.rest.controllers;

import com.gamescollection.adapters.rest.dtos.PlatformDto;
import com.gamescollection.adapters.rest.dtos.mappers.PlatformModelDtoMapper;
import com.gamescollection.core.services.PlatformService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
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
                platformService.retrieveAny().stream().map(p -> platformModelDtoMapper.mapToDTO(p)).collect(Collectors.toList())
                , HttpStatus.OK);
    }


    @PostMapping("/api/platform")
    public ResponseEntity<PlatformDto> createGame(@RequestBody PlatformDto platformDto) {
        return new ResponseEntity<>(
                platformModelDtoMapper.mapToDTO(platformService.createPlatform(platformDto.getName(), platformDto.getDescription())
                ), HttpStatus.CREATED);
    }

    @PutMapping("/api/platform")
    public ResponseEntity<PlatformDto> updateGame(@RequestParam final UUID id, @RequestBody PlatformDto platformDto) {
        return new ResponseEntity<>(
                platformModelDtoMapper.mapToDTO(platformService.updatePlatform(id, platformDto.getName(), platformDto.getDescription())
                ), HttpStatus.CREATED);
    }
}
