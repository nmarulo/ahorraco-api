package dev.nmarulo.ahorraco_api.app.payments;

import dev.nmarulo.ahorraco_api.app.participants.Participant;
import dev.nmarulo.ahorraco_api.app.pools.Pool;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    Optional<Payment> findByPoolAndParticipantAndMonth(Pool pool, Participant participant, LocalDate month);
    
    List<Payment> findAllByPoolAndParticipantOrderByMonthAsc(Pool pool, Participant participant);
    
    List<Payment> findAllByPoolAndMonth(Pool pool, LocalDate month);
    
    /**
     * Número de cuotas confirmadas por el organizador.
     */
    long countByPoolAndMonthAndConfirmedIsTrue(Pool pool, LocalDate month);
    
}
