package net.foodeals.notification.application.dtos;


public record PreferencesUpdateRequest(
        boolean orderNotifications,
        boolean supportNotifications,
        boolean reportNotifications,
        boolean systemNotifications,
        boolean emailDigest,
        boolean soundEnabled
) {}
