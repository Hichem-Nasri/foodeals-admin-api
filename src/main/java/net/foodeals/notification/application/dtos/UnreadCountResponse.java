package net.foodeals.notification.application.dtos;


import java.util.Map;

public record UnreadCountResponse(long total, Map<String, Long> byType) {}