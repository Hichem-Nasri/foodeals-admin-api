package net.foodeals.notification.application.dtos;


import java.time.Instant;
import java.util.UUID;

public record MarkReadResponse(UUID id, boolean isRead, Instant readAt) {}