package net.foodeals.notification.application.dtos;


import net.foodeals.notification.domain.enums.AdminNotificationPriority;
import net.foodeals.notification.domain.enums.AdminNotificationSourceApp;
import net.foodeals.notification.domain.enums.AdminNotificationType;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;


public record AdminNotificationDto(
        UUID id,
        AdminNotificationType type,
        String title,
        String message,
        String icon,
        boolean isRead,
        String actionUrl,
        Map<String, Object> metadata,
        AdminNotificationSourceApp sourceApp,
        AdminNotificationPriority priority,
        Instant createdAt
) {}
