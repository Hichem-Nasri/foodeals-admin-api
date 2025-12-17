package net.foodeals.notification.application.dtos;

import java.util.List;

public record AdminNotificationListData(
        List<AdminNotificationDto> notifications,
        Pagination pagination,
        long unreadCount
) {}