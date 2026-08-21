package dev.nmarulo.ahorraco_api.app.pools;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pools")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
public class Pool {
    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
    
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "public_id", nullable = false, updatable = false, length = 36)
    private UUID publicId;
    
    @Column(name = "name", nullable = false, length = 80)
    private String name;
    
    @Column(name = "monthly_fee", nullable = false, precision = 11, scale = 2)
    private BigDecimal monthlyFee;
    
    @Column(name = "num_participants", nullable = false)
    private Integer numParticipants;
    
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    
    @Column(name = "payment_due_day", nullable = false)
    private Integer paymentDueDay;
    
    @Column(name = "notes", length = 500)
    private String notes;
    
    @Column(name = "management_code", nullable = false, length = 30)
    private String managementCode;
    
    @Column(name = "invitation_token", nullable = false, length = 10)
    private String invitationToken;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
}
