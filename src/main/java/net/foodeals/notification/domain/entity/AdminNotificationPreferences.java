package net.foodeals.notification.domain.entity;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;
import net.foodeals.user.domain.entities.User;

@Entity
@Table(name = "admin_notification_preferences")
@Getter @Setter
@NoArgsConstructor
public class AdminNotificationPreferences {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false, unique = true)
    private User admin;

    private boolean orderNotifications = true;
    private boolean supportNotifications = true;
    private boolean reportNotifications = true;
    private boolean systemNotifications = true;

    private boolean emailDigest = false;
    private boolean soundEnabled = true;

    public static AdminNotificationPreferences defaults(User admin) {
        AdminNotificationPreferences p = new AdminNotificationPreferences();
        p.setAdmin(admin);
        return p;
    }
}
