package net.foodeals.user.application.dtos.responses;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Builder;

@Builder
public record AbsenceDto(
	    UUID id,
	    LocalDate startDate,
	    LocalDate endDate,
	    String reason,
	    String justificationPath,
	    String validatedBy,
	    String validatedByAvatar
	) {}
