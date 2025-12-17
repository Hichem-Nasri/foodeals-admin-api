package net.foodeals.notification.domain.entity;


import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;
import net.foodeals.user.domain.entities.User;

@Entity
@Table(
        name = "admin_notification_states",
        uniqueConstraints = @UniqueConstraint(columnNames = {"notification_id", "admin_id"})
)
@Getter @Setter
@NoArgsConstructor
public class AdminNotificationState {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id", nullable = false)
    private AdminNotification notification;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;

    @Column(nullable = false)
    private boolean isRead = false;

    private Instant readAt;

    public static AdminNotificationState unread(AdminNotification n, User admin) {
        AdminNotificationState s = new AdminNotificationState();
        s.setNotification(n);
        s.setAdmin(admin);
        s.setRead(false);
        return s;
    }

    public void markRead() {
        this.isRead = true;
        this.readAt = Instant.now();
    }
}
