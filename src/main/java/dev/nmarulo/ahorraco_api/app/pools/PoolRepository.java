package dev.nmarulo.ahorraco_api.app.pools;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PoolRepository extends JpaRepository<Pool, Long> {
    
    boolean existsByInvitationToken(String invitationToken);
    
    Optional<Pool> findByPublicId(UUID publicId);
    
    Optional<Pool> findByInvitationTokenIgnoreCase(String invitationToken);
    
}
