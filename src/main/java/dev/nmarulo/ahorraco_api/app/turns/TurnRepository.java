package dev.nmarulo.ahorraco_api.app.turns;

import dev.nmarulo.ahorraco_api.app.pools.Pool;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TurnRepository extends JpaRepository<Turn, Long> {
    
    boolean existsByPool(Pool pool);
    
}
