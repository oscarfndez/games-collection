package com.oscarfndez.inventory.adapters.rest.controllers;

import com.oscarfndez.framework.core.model.dto.PageResponseDto;
import com.oscarfndez.inventory.adapters.rest.dtos.PlatformDto;
import com.oscarfndez.inventory.adapters.rest.dtos.mappers.PlatformModelDtoMapper;
import com.oscarfndez.inventory.core.model.Platform;
import com.oscarfndez.inventory.core.services.PlatformService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping({"/api/platform", "/gamesCollection/api/platform"})
@AllArgsConstructor
@PreAuthorize("@authorizationService.hasRole('USER')")
public class PlatformController {

    private final PlatformModelDtoMapper platformModelDtoMapper;
    private final PlatformService platformService;

    @GetMapping
    public ResponseEntity<PlatformDto> loadPlatform(@RequestParam final UUID id) {

        return new ResponseEntity<>(
                platformModelDtoMapper.mapToDTO(platformService.retrieveOne(id))
                , HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<PageResponseDto<PlatformDto>> loadAllPlatforms(
            @RequestParam(required = false) final String search,
            @RequestParam(required = false, defaultValue = "name") final String sortField,
            @RequestParam(required = false, defaultValue = "asc") final String sortDir,
            @RequestParam(required = false, defaultValue = "0") final int page,
            @RequestParam(required = false, defaultValue = "10") final int size
    ) {
        Page<Platform> resultPage = platformService.retrievePage(search, sortField, sortDir, page, size);

        List<PlatformDto> content = resultPage.getContent()
                .stream()
                .map(platformModelDtoMapper::mapToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new PageResponseDto<>(
                        content,
                        resultPage.getNumber(),
                        resultPage.getSize(),
                        resultPage.getTotalElements(),
                        resultPage.getTotalPages()
                )
        );
    }

    @PostMapping
    public ResponseEntity<PlatformDto> createPlatform(@RequestBody PlatformDto platformDto) {
        return new ResponseEntity<>(
                platformModelDtoMapper.mapToDTO(platformService.createPlatform(platformDto.getName(), platformDto.getDescription(), platformDto.getImageUrl())
                ), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<PlatformDto> updatePlatform(@RequestParam final UUID id, @RequestBody PlatformDto platformDto) {
        return new ResponseEntity<>(
                platformModelDtoMapper.mapToDTO(platformService.updatePlatform(id, platformDto.getName(), platformDto.getDescription(),platformDto.getImageUrl())
                ), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deletePlatform(@RequestParam final UUID id) {
        platformService.deleteOne(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}
