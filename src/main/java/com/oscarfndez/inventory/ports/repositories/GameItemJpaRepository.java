package com.oscarfndez.inventory.ports.repositories;

import com.oscarfndez.inventory.adapters.persistence.entities.GameItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface GameItemJpaRepository extends JpaRepository<GameItemEntity, UUID> {
    @Query("""
        select distinct gi
        from GameItemEntity gi
        join fetch gi.game g
        join fetch gi.platform p
        left join fetch g.platforms gp
        where gi.userId = :userId
          and gi.active = true
        order by g.name asc, p.name asc
    """)
    List<GameItemEntity> findByUserId(@Param("userId") UUID userId);

    @Query("""
        select gi
        from GameItemEntity gi
        join gi.game g
        join gi.platform p
        where gi.userId = :userId
          and gi.active = true
          and (:search is null or :search = ''
             or lower(g.name) like lower(concat('%', :search, '%'))
             or lower(g.description) like lower(concat('%', :search, '%'))
             or lower(p.name) like lower(concat('%', :search, '%')))
    """)
    Page<GameItemEntity> searchByUserId(
            @Param("userId") UUID userId,
            @Param("search") String search,
            Pageable pageable
    );

    boolean existsByIdAndUserIdAndActiveTrue(UUID id, UUID userId);

    void deleteByIdAndUserId(UUID id, UUID userId);

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("""
        update GameItemEntity gi
        set gi.active = false
        where gi.userId = :userId
          and gi.active = true
    """)
    int deactivateByUserId(@Param("userId") UUID userId);
}
