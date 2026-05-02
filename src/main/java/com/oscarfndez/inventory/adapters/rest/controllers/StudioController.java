package com.oscarfndez.inventory.adapters.rest.controllers;

import com.oscarfndez.framework.core.model.dto.PageResponseDto;
import com.oscarfndez.inventory.adapters.rest.dtos.StudioDto;
import com.oscarfndez.inventory.adapters.rest.dtos.mappers.StudioModelDtoMapper;
import com.oscarfndez.inventory.core.model.Studio;
import com.oscarfndez.inventory.core.services.StudioService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping({"/api/studio", "/gamesCollection/api/studio"})
@AllArgsConstructor
public class StudioController {

    private final StudioModelDtoMapper studioModelDtoMapper;
    private final StudioService studioService;

    @GetMapping
    @PreAuthorize("@authorizationService.hasRole('USER')")
    public ResponseEntity<StudioDto> loadStudio(@RequestParam final UUID id) {
        return ResponseEntity.ok(studioModelDtoMapper.mapToDTO(studioService.retrieveOne(id)));
    }

    @GetMapping("/all")
    @PreAuthorize("@authorizationService.hasRole('USER')")
    public ResponseEntity<PageResponseDto<StudioDto>> loadAllStudios(
            @RequestParam(required = false) final String search,
            @RequestParam(required = false, defaultValue = "name") final String sortField,
            @RequestParam(required = false, defaultValue = "asc") final String sortDir,
            @RequestParam(required = false, defaultValue = "0") final int page,
            @RequestParam(required = false, defaultValue = "10") final int size
    ) {
        Page<Studio> resultPage = studioService.retrievePage(search, sortField, sortDir, page, size);
        List<StudioDto> content = resultPage.getContent().stream()
                .map(studioModelDtoMapper::mapToDTO)
                .toList();

        return ResponseEntity.ok(new PageResponseDto<>(
                content,
                resultPage.getNumber(),
                resultPage.getSize(),
                resultPage.getTotalElements(),
                resultPage.getTotalPages()
        ));
    }

    @PostMapping
    @PreAuthorize("@authorizationService.hasRole('ADMIN')")
    public ResponseEntity<StudioDto> createStudio(@RequestBody StudioDto studioDto) {
        return new ResponseEntity<>(studioModelDtoMapper.mapToDTO(studioService.createStudio(
                studioDto.getName(),
                studioDto.getDescription(),
                studioDto.getLocation(),
                studioDto.getFirstParty()
        )), HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("@authorizationService.hasRole('ADMIN')")
    public ResponseEntity<StudioDto> updateStudio(@RequestParam final UUID id, @RequestBody StudioDto studioDto) {
        return ResponseEntity.ok(studioModelDtoMapper.mapToDTO(studioService.updateStudio(
                id,
                studioDto.getName(),
                studioDto.getDescription(),
                studioDto.getLocation(),
                studioDto.getFirstParty()
        )));
    }

    @DeleteMapping
    @PreAuthorize("@authorizationService.hasRole('ADMIN')")
    public ResponseEntity<Void> deleteStudio(@RequestParam final UUID id) {
        studioService.deleteOne(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}
