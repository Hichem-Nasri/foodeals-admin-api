package net.foodeals.notification.application.dtos;

import java.util.List;
import java.util.Map;

public record NotificationStatsResponse(
	    Overview overview,
	    Map<String, Object> byType,
	    List<TimelineEntry> timeline
	) 
{}
