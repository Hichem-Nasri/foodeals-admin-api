package net.foodeals.notification.domain.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.user.domain.entities.User;


@Entity
@Table(name = "notifications")

@Getter
@Setter
public class Notification extends AbstractEntity<UUID> {

	@Id
    @GeneratedValue(generator = "UUID")
    @Column(updatable = false, nullable = false)
    private UUID id;

    private String title;
    private String message;
    private String type;
    private String targetType;
    
    private String imageUrl;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "target_ids", columnDefinition = "jsonb")
    private List<Integer> targetIds;

    private LocalDateTime scheduledAt;
    private LocalDateTime sentAt;
    private String status;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

}
