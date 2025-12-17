package net.foodeals.notification.application.dtos;


import java.util.UUID;

public record PreferencesResponse(
        Integer userId,
        boolean orderNotifications,
        boolean supportNotifications,
        boolean reportNotifications,
        boolean systemNotifications,
        boolean emailDigest,
        boolean soundEnabled
) {}
