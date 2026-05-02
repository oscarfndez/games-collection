package com.oscarfndez.inventory.ports.repositories;

import com.oscarfndez.inventory.adapters.persistence.entities.StudioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface StudioJpaRepository extends JpaRepository<StudioEntity, UUID> {

    @Query("""
        select distinct s
        from StudioEntity s
        left join fetch s.games
        where s.id = :id
        """)
    StudioEntity findOneWithGames(@Param("id") UUID id);

    @Query("""
        select s
        from StudioEntity s
        where lower(s.name) like lower(concat('%', :search, '%'))
           or lower(s.description) like lower(concat('%', :search, '%'))
           or lower(s.location) like lower(concat('%', :search, '%'))
        """)
    Page<StudioEntity> search(@Param("search") String search, Pageable pageable);
}
