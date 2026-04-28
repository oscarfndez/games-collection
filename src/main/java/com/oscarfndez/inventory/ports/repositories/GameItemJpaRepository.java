package com.oscarfndez.inventory.ports.repositories;

import com.oscarfndez.inventory.adapters.persistence.entities.GameItemEntity;
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
        order by g.name asc, p.name asc
    """)
    List<GameItemEntity> findByUserId(@Param("userId") UUID userId);

    void deleteByIdAndUserId(UUID id, UUID userId);
}
