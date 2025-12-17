package net.foodeals.notification.domain.entity;

import java.time.LocalDateTime;
import java.util.*;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.user.domain.entities.User;

@Entity
@Table(name = "notification_deliveries")
@Setter
@Getter
public class NotificationDelivery extends AbstractEntity<UUID>{
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "notification_id")
    private Notification notification;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String deliveryMethod; // 'push', 'email', 'sms'
    private String status; // 'pending', 'delivered', 'failed', 'read'

    private LocalDateTime deliveredAt;
    private LocalDateTime readAt;
    private String errorMessage;
 
}