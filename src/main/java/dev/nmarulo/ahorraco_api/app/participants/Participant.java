package dev.nmarulo.ahorraco_api.app.participants;

import dev.nmarulo.ahorraco_api.app.pools.Pool;
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

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "participants")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
public class Participant {
    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
    
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "public_id", nullable = false, updatable = false, length = 36)
    private UUID publicId;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pool_id", nullable = false)
    @ToString.Exclude
    private Pool pool;
    
    @Column(name = "full_name", nullable = false, length = 80)
    private String fullName;
    
    @Column(name = "phone", length = 20)
    private String phone;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
}
