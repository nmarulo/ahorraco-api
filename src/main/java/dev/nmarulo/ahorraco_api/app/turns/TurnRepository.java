package dev.nmarulo.ahorraco_api.app.turns;

import dev.nmarulo.ahorraco_api.app.pools.Pool;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TurnRepository extends JpaRepository<Turn, Long> {
    
    boolean existsByPool(Pool pool);
    
    @EntityGraph(attributePaths = "participant")
    Optional<Turn> findByPoolAndMonth(Pool pool, LocalDate month);
    
    /**
     * El `EntityGraph` incluye la instancia de la entidad `Participant` (campo `participant`) como si este campo fuera de tipo "EAGER".
     */
    @EntityGraph(attributePaths = "participant")
    List<Turn> findAllByPoolOrderByPositionAsc(Pool pool);
    
}
