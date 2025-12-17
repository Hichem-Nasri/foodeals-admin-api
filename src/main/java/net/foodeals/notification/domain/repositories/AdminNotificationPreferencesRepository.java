package net.foodeals.notification.domain.repositories;

import java.util.Optional;
import java.util.UUID;

import net.foodeals.notification.domain.entity.AdminNotificationPreferences;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminNotificationPreferencesRepository extends JpaRepository<AdminNotificationPreferences, UUID> {
    Optional<AdminNotificationPreferences> findByAdminId(Integer adminId);
}
