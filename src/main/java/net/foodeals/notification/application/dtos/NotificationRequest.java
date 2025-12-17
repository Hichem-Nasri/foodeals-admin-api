package net.foodeals.notification.application.dtos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record NotificationRequest(
	    String title,
	    String message,
	    String type,
	    String targetType,
	    List<Integer> targetIds,
	    String filePath,
	    LocalDateTime scheduledAt,
	    boolean sendImmediately
	) {}
