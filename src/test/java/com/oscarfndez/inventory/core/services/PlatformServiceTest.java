package com.oscarfndez.inventory.core.services;

import com.oscarfndez.inventory.core.model.Platform;
import com.oscarfndez.inventory.ports.repositories.PlatformRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlatformServiceTest {

    @Mock
    private PlatformRepository platformRepository;

    @InjectMocks
    private PlatformService platformService;

    @Test
    void retrieveAnyWithoutSearchReturnsSortedPlatforms() {
        List<Platform> platforms = List.of(platform("Nintendo Switch"));
        when(platformRepository.findAllSorted("name", true)).thenReturn(platforms);

        assertThat(platformService.retrieveAny(null, "unknown", "asc")).isSameAs(platforms);
    }

    @Test
    void retrieveAnyWithSearchTrimsAndUsesMappedSort() {
        List<Platform> platforms = List.of(platform("Nintendo Switch"));
        when(platformRepository.searchAndSort("console", "description", false)).thenReturn(platforms);

        assertThat(platformService.retrieveAny(" console ", "description", "desc")).isSameAs(platforms);
    }

    @Test
    void retrievePageBuildsPageableWithDefaultsForUnknownSort() {
        Page<Platform> page = new PageImpl<>(List.of(platform("Nintendo Switch")));
        when(platformRepository.search(any(String.class), any(Pageable.class))).thenReturn(page);

        assertThat(platformService.retrievePage(null, "unknown", "asc", 1, 5)).isSameAs(page);

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(platformRepository).search("", pageableCaptor.capture());
        Pageable pageable = pageableCaptor.getValue();
        assertThat(pageable.getPageNumber()).isEqualTo(1);
        assertThat(pageable.getPageSize()).isEqualTo(5);
        Sort.Order order = pageable.getSort().getOrderFor("name");
        assertThat(order).isNotNull();
        assertThat(order.getDirection()).isEqualTo(Sort.Direction.ASC);
    }

    @Test
    void createPlatformBuildsPlatformWithGeneratedId() {
        when(platformRepository.save(any(Platform.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Platform createdPlatform = platformService.createPlatform("Nintendo Switch", "Console", "switch.png");

        assertThat(createdPlatform.getId()).isNotNull();
        assertThat(createdPlatform.getName()).isEqualTo("Nintendo Switch");
        assertThat(createdPlatform.getDescription()).isEqualTo("Console");
        assertThat(createdPlatform.getImageUrl()).isEqualTo("switch.png");
    }

    @Test
    void updatePlatformKeepsProvidedId() {
        UUID platformId = UUID.randomUUID();
        when(platformRepository.save(any(Platform.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Platform updatedPlatform = platformService.updatePlatform(platformId, "Steam", "Store", "steam.png");

        assertThat(updatedPlatform.getId()).isEqualTo(platformId);
        assertThat(updatedPlatform.getName()).isEqualTo("Steam");
    }

    @Test
    void deleteOneDelegatesToRepository() {
        UUID platformId = UUID.randomUUID();

        platformService.deleteOne(platformId);

        verify(platformRepository).deleteOne(platformId);
    }

    private static Platform platform(String name) {
        return Platform.builder()
                .id(UUID.randomUUID())
                .name(name)
                .description("Description")
                .imageUrl("image.png")
                .build();
    }
}
