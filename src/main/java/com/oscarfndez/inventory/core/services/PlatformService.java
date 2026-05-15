package com.oscarfndez.inventory.core.services;


import com.oscarfndez.inventory.core.model.Platform;
import com.oscarfndez.inventory.ports.repositories.PlatformRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;


import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class PlatformService {

    private final PlatformRepository platformRepository;

    @Cacheable(cacheNames = "platforms", key = "#p0")
    public Platform retrieveOne(UUID id) {
        return platformRepository.retrieveOne(id);
    }

    @Cacheable(cacheNames = "platformPages", key = "'all'")
    public List<Platform> retrieveAny() {
        return platformRepository.retrieveAny();
    }

    @Cacheable(cacheNames = "platformPages", key = "{#p0 == null ? '' : #p0.trim(), #p1, #p2}")
    public List<Platform> retrieveAny(String search, String sortField, String sortDir) {
        String field = mapSortField(sortField);
        boolean asc = !"desc".equalsIgnoreCase(sortDir);

        if (search == null || search.isBlank()) {
            return platformRepository.findAllSorted(field, asc);
        }

        return platformRepository.searchAndSort(search.trim(), field, asc);
    }


    @Cacheable(cacheNames = "platformPages", key = "{#p0 == null ? '' : #p0.trim(), #p1, #p2, #p3, #p4}")
    public Page<Platform> retrievePage(String search, String sortField, String sortDir, int page, int size) {
        String mappedSortField = mapSortField(sortField);
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, mappedSortField));

        return platformRepository.search(search == null ? "" : search.trim(), pageable);
    }

    private String mapSortField(String sortField) {
        return switch (sortField) {
            case "name" -> "name";
            case "description" -> "description";
            default -> "name";
        };
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "platformPages", allEntries = true),
            @CacheEvict(cacheNames = "gamePages", allEntries = true),
            @CacheEvict(cacheNames = "collectionPages", allEntries = true)
    })
    public Platform createPlatform(String name, String description, String imageUrl) {
        return platformRepository.save(
                Platform.builder()
                        .id(UUID.randomUUID())
                        .name(name)
                        .description(description)
                        .imageUrl(imageUrl)
                        .build()
        );
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "platforms", key = "#p0"),
            @CacheEvict(cacheNames = "platformPages", allEntries = true),
            @CacheEvict(cacheNames = "gamePages", allEntries = true),
            @CacheEvict(cacheNames = "collectionPages", allEntries = true)
    })
    public Platform updatePlatform(UUID id, String name, String description, String imageUrl) {
        return platformRepository.save(
                Platform.builder()
                        .id(id)
                        .name(name)
                        .description(description)
                        .imageUrl(imageUrl)
                        .build()
        );
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "platforms", key = "#p0"),
            @CacheEvict(cacheNames = "platformPages", allEntries = true),
            @CacheEvict(cacheNames = "gamePages", allEntries = true),
            @CacheEvict(cacheNames = "collectionPages", allEntries = true)
    })
    public void deleteOne(UUID id) {
        platformRepository.deleteOne(id);
    }
}
