package net.foodeals.notification.application.dtos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record NotificationResponse(
	    UUID id,
	    String title,
	    String message,
	    String type,
	    String targetType,
	    List<Integer> targetIds,
	    LocalDateTime scheduledAt,
	    LocalDateTime sentAt,
	    String status,
	    Integer createdBy,
	    LocalDateTime createdAt
	) {}