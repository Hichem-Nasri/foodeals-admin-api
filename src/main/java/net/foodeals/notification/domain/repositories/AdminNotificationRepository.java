package net.foodeals.notification.domain.repositories;

import java.time.Instant;
import java.util.UUID;

import net.foodeals.notification.domain.entity.AdminNotification;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface AdminNotificationRepository extends JpaRepository<AdminNotification, UUID>, JpaSpecificationExecutor<AdminNotification> {
}