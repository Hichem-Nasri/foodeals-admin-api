package net.foodeals.notification.application.dtos;

public record NotificationTestRequest(
	    String title,
	    String message,
	    String type
	) {}