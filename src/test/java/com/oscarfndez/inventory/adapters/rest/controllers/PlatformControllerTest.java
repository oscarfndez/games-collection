package com.oscarfndez.inventory.adapters.rest.controllers;

import com.oscarfndez.inventory.adapters.rest.dtos.PlatformDto;
import com.oscarfndez.inventory.adapters.rest.dtos.mappers.PlatformModelDtoMapper;
import com.oscarfndez.inventory.core.model.Platform;
import com.oscarfndez.inventory.core.services.PlatformService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlatformControllerTest {

    @Mock
    private PlatformModelDtoMapper platformModelDtoMapper;

    @Mock
    private PlatformService platformService;

    @InjectMocks
    private PlatformController platformController;

    @Test
    void loadPlatformReturnsPlatformDto() {
        UUID platformId = UUID.randomUUID();
        Platform platform = platform(platformId);
        PlatformDto dto = platformDto(platformId);
        when(platformService.retrieveOne(platformId)).thenReturn(platform);
        when(platformModelDtoMapper.mapToDTO(platform)).thenReturn(dto);

        var response = platformController.loadPlatform(platformId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(dto);
    }

    @Test
    void loadAllPlatformsReturnsPagedDtoResponse() {
        UUID platformId = UUID.randomUUID();
        Platform platform = platform(platformId);
        PlatformDto dto = platformDto(platformId);
        when(platformService.retrievePage("switch", "name", "asc", 0, 10))
                .thenReturn(new PageImpl<>(List.of(platform)));
        when(platformModelDtoMapper.mapToDTO(platform)).thenReturn(dto);

        var response = platformController.loadAllPlatforms("switch", "name", "asc", 0, 10);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).containsExactly(dto);
        assertThat(response.getBody().getPage()).isZero();
        assertThat(response.getBody().getSize()).isEqualTo(1);
        assertThat(response.getBody().getTotalElements()).isEqualTo(1);
    }

    @Test
    void createPlatformDelegatesToServiceAndReturnsCreatedDto() {
        UUID platformId = UUID.randomUUID();
        PlatformDto request = PlatformDto.builder()
                .name("Nintendo Switch")
                .description("Console")
                .imageUrl("switch.png")
                .build();
        Platform createdPlatform = platform(platformId);
        PlatformDto responseDto = platformDto(platformId);
        when(platformService.createPlatform("Nintendo Switch", "Console", "switch.png")).thenReturn(createdPlatform);
        when(platformModelDtoMapper.mapToDTO(createdPlatform)).thenReturn(responseDto);

        var response = platformController.createPlatform(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isSameAs(responseDto);
    }

    @Test
    void updatePlatformDelegatesToServiceAndReturnsOkDto() {
        UUID platformId = UUID.randomUUID();
        PlatformDto request = PlatformDto.builder()
                .name("Nintendo Switch")
                .description("Console")
                .imageUrl("switch.png")
                .build();
        Platform updatedPlatform = platform(platformId);
        PlatformDto responseDto = platformDto(platformId);
        when(platformService.updatePlatform(platformId, "Nintendo Switch", "Console", "switch.png"))
                .thenReturn(updatedPlatform);
        when(platformModelDtoMapper.mapToDTO(updatedPlatform)).thenReturn(responseDto);

        var response = platformController.updatePlatform(platformId, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(responseDto);
    }

    @Test
    void deletePlatformDelegatesToServiceAndReturnsNoContent() {
        UUID platformId = UUID.randomUUID();

        var response = platformController.deletePlatform(platformId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(platformService).deleteOne(platformId);
    }

    private static Platform platform(UUID platformId) {
        return Platform.builder()
                .id(platformId)
                .name("Nintendo Switch")
                .description("Console")
                .imageUrl("switch.png")
                .build();
    }

    private static PlatformDto platformDto(UUID platformId) {
        return PlatformDto.builder()
                .id(platformId)
                .name("Nintendo Switch")
                .description("Console")
                .imageUrl("switch.png")
                .build();
    }
}
