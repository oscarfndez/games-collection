package com.oscarfndez.gamescollection.ports.repositories;

import com.oscarfndez.gamescollection.adapters.persistence.entities.PlatformEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PlatformJpaRepository extends JpaRepository<PlatformEntity, UUID> {

    @Query("""
        select p
        from PlatformEntity p
        where lower(p.name) like lower(concat('%', :search, '%'))
           or lower(p.description) like lower(concat('%', :search, '%'))
    """)
    List<PlatformEntity> search(@Param("search") String search);
}