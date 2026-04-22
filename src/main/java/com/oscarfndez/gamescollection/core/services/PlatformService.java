package com.oscarfndez.gamescollection.core.services;


import com.oscarfndez.gamescollection.core.model.Platform;
import com.oscarfndez.gamescollection.ports.repositories.PlatformRepository;
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

    public Platform retrieveOne(UUID id) {
        return platformRepository.retrieveOne(id);
    }

    public List<Platform> retrieveAny() {
        return platformRepository.retrieveAny();
    }

    public List<Platform> retrieveAny(String search, String sortField, String sortDir) {
        String field = mapSortField(sortField);
        boolean asc = !"desc".equalsIgnoreCase(sortDir);

        if (search == null || search.isBlank()) {
            return platformRepository.findAllSorted(field, asc);
        }

        return platformRepository.searchAndSort(search.trim(), field, asc);
    }


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

    public Platform createPlatform(String name, String description) {
        return platformRepository.save(
                Platform.builder()
                        .id(UUID.randomUUID())
                        .name(name)
                        .description(description)
                        .build()
        );
    }

    public Platform updatePlatform(UUID id, String name, String description) {
        return platformRepository.save(
                Platform.builder()
                        .id(id)
                        .name(name)
                        .description(description)
                        .build()
        );
    }

    public void deleteOne(UUID id) {
        platformRepository.deleteOne(id);
    }
}