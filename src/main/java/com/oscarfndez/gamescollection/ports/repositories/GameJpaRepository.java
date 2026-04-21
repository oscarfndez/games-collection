package com.oscarfndez.gamescollection.ports.repositories;

import com.oscarfndez.gamescollection.adapters.persistence.entities.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface GameJpaRepository extends JpaRepository<GameEntity, UUID> {

    @Query("""
        select g
        from GameEntity g
        join g.platform p
        where lower(g.name) like lower(concat('%', :search, '%'))
           or lower(g.description) like lower(concat('%', :search, '%'))
           or lower(p.name) like lower(concat('%', :search, '%'))
    """)
    List<GameEntity> search(@Param("search") String search);
}