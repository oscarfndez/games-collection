package com.oscarfndez.inventory.ports.repositories;

import com.oscarfndez.inventory.adapters.persistence.entities.PlatformEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface PlatformJpaRepository extends JpaRepository<PlatformEntity, UUID> {

    @Query("""
        select p
        from PlatformEntity p
        where (:search is null or :search = ''
           or lower(p.name) like lower(concat('%', :search, '%'))
           or lower(p.description) like lower(concat('%', :search, '%')))
    """)
    Page<PlatformEntity> search(@Param("search") String search, Pageable pageable);
}