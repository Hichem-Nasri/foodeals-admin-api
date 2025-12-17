package net.foodeals.notification.application.service;


import java.time.Instant;

import net.foodeals.notification.domain.entity.AdminNotification;
import net.foodeals.notification.domain.enums.AdminNotificationPriority;
import net.foodeals.notification.domain.enums.AdminNotificationType;
import org.springframework.data.jpa.domain.Specification;

public class AdminNotificationSpecs {

    public static Specification<AdminNotification> typeEq(AdminNotificationType type) {
        return (root, q, cb) -> type == null ? cb.conjunction() : cb.equal(root.get("type"), type);
    }

    public static Specification<AdminNotification> priorityEq(AdminNotificationPriority p) {
        return (root, q, cb) -> p == null ? cb.conjunction() : cb.equal(root.get("priority"), p);
    }

    public static Specification<AdminNotification> createdBetween(Instant from, Instant to) {
        return (root, q, cb) -> {
            if (from == null && to == null) return cb.conjunction();
            if (from != null && to != null) return cb.between(root.get("createdAt"), from, to);
            if (from != null) return cb.greaterThanOrEqualTo(root.get("createdAt"), from);
            return cb.lessThanOrEqualTo(root.get("createdAt"), to);
        };
    }
}

