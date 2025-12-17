package net.foodeals.notification.domain.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;
import net.foodeals.notification.domain.enums.AdminNotificationPriority;
import net.foodeals.notification.domain.enums.AdminNotificationSourceApp;
import net.foodeals.notification.domain.enums.AdminNotificationType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "admin_notifications")
@Getter @Setter
@NoArgsConstructor
public class AdminNotification {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdminNotificationType type;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2000)
    private String message;

    private String icon;
    private String actionUrl;

    // JSON string (metadata)

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String metadataJson;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdminNotificationSourceApp sourceApp = AdminNotificationSourceApp.SYSTEM;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdminNotificationPriority priority = AdminNotificationPriority.MEDIUM;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();
}
