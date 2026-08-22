package dev.nmarulo.ahorraco_api.app.payments;

import dev.nmarulo.ahorraco_api.app.participants.Participant;
import dev.nmarulo.ahorraco_api.app.pools.Pool;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
public class Payment {
    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pool_id", nullable = false)
    @ToString.Exclude
    private Pool pool;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "participant_id", nullable = false)
    @ToString.Exclude
    private Participant participant;
    
    @Column(name = "month", nullable = false)
    private LocalDate month;
    
    @Column(name = "marked", nullable = false)
    private boolean marked;
    
    /**
     * Confirmación del organizador.
     */
    @Column(name = "confirmed", nullable = false)
    private boolean confirmed;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
}
