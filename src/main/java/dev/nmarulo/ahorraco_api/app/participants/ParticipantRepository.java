package dev.nmarulo.ahorraco_api.app.participants;

import dev.nmarulo.ahorraco_api.app.pools.Pool;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    
    long countByPool(Pool pool);
    
    boolean existsByPoolAndFullNameIgnoreCase(Pool pool, String fullName);
    
    List<Participant> findAllByPoolOrderByIdAsc(Pool pool);
    
}
