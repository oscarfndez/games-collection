package com.oscarfndez.inventory.ports.repositories;

import com.oscarfndez.inventory.adapters.persistence.entities.PlatformEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlatformJpaRepository extends JpaRepository<PlatformEntity, UUID> {

    @Query("""
        select p
        from PlatformEntity p
        where p.deleted = false
          and (:search is null or :search = ''
           or lower(p.name) like lower(concat('%', :search, '%'))
           or lower(p.description) like lower(concat('%', :search, '%')))
    """)
    Page<PlatformEntity> search(@Param("search") String search, Pageable pageable);

    Optional<PlatformEntity> findByIdAndDeletedFalse(UUID id);

    List<PlatformEntity> findAllByDeletedFalse();

    List<PlatformEntity> findAllByIdInAndDeletedFalse(List<UUID> ids);

    @Modifying
    @Query("""
        update PlatformEntity p
        set p.deleted = true
        where p.id = :id
          and p.deleted = false
    """)
    int softDeleteById(@Param("id") UUID id);
}
