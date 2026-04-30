package com.oscarfndez.inventory.adapters.rest.controllers;

import com.oscarfndez.framework.core.model.dto.PageResponseDto;
import com.oscarfndez.inventory.adapters.rest.dtos.GameItemDto;
import com.oscarfndez.inventory.adapters.rest.dtos.mappers.GameItemModelDtoMapper;
import com.oscarfndez.inventory.core.model.GameItem;
import com.oscarfndez.inventory.core.services.GameItemService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<PageResponseDto<GameItemDto>> loadCollection(
            @RequestParam(required = false) final String search,
            @RequestParam(required = false, defaultValue = "game") final String sortField,
            @RequestParam(required = false, defaultValue = "asc") final String sortDir,
            @RequestParam(required = false, defaultValue = "0") final int page,
            @RequestParam(required = false, defaultValue = "10") final int size,
            HttpServletRequest request
    ) {
        UUID userId = authenticatedUserId(request);
        Page<GameItem> resultPage = gameItemService.retrievePage(userId, search, sortField, sortDir, page, size);

        List<GameItemDto> content = resultPage.getContent()
                .stream()
                .map(gameItemModelDtoMapper::mapToDTO)
                .toList();

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
