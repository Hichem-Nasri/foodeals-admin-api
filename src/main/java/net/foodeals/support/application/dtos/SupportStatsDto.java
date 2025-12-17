package net.foodeals.support.application.dtos;

import java.math.BigDecimal;

public record SupportStatsDto(
	    long total,
	    long pending,
	    long in_progress,
	    long resolved,
	    long closed,
	    String average_response_time,
	    BigDecimal satisfaction_rating
	) {}
