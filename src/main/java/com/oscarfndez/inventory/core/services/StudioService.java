package com.oscarfndez.inventory.core.services;

import com.oscarfndez.inventory.core.model.Studio;
import com.oscarfndez.inventory.ports.repositories.StudioRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.UUID;

@AllArgsConstructor
@Component
public class StudioService {

    private final StudioRepository studioRepository;

    public Studio retrieveOne(UUID id) {
        return studioRepository.retrieveOne(id);
    }

    public Page<Studio> retrievePage(String search, String sortField, String sortDir, int page, int size) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, mapSortField(sortField)));
        return studioRepository.search(search == null ? "" : search.trim(), pageable);
    }

    public Studio createStudio(String name, String description, String location, Boolean firstParty) {
        return studioRepository.save(Studio.builder()
                .id(UUID.randomUUID())
                .name(name)
                .description(description)
                .location(location)
                .firstParty(firstParty)
                .games(java.util.List.of())
                .build());
    }

    public Studio updateStudio(UUID id, String name, String description, String location, Boolean firstParty) {
        return studioRepository.save(Studio.builder()
                .id(id)
                .name(name)
                .description(description)
                .location(location)
                .firstParty(firstParty)
                .games(java.util.List.of())
                .build());
    }

    public void deleteOne(UUID id) {
        studioRepository.deleteOne(id);
    }

    private String mapSortField(String sortField) {
        return switch (sortField) {
            case "description" -> "description";
            case "location" -> "location";
            case "firstParty" -> "firstParty";
            default -> "name";
        };
    }
}
