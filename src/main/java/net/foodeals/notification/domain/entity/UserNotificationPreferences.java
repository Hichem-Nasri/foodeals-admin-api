package net.foodeals.notification.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.user.domain.entities.User;

@Entity
@Table(name = "user_notification_preferences")
@Getter
@Setter
public class UserNotificationPreferences extends AbstractEntity<UUID> {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private boolean pushEnabled = true;
    private boolean emailEnabled = true;
    private boolean smsEnabled = false;

    @Column(columnDefinition = "jsonb")
    private String notificationTypes;

   
}