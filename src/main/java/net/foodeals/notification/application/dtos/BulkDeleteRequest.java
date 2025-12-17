package net.foodeals.notification.application.dtos;


import java.util.List;
import java.util.UUID;

public record BulkDeleteRequest(List<UUID> notificationIds) {}