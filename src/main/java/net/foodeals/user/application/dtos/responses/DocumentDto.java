package net.foodeals.user.application.dtos.responses;

import java.time.Instant;
import java.util.UUID;

import lombok.Builder;

@Builder
public record DocumentDto(
	    UUID id,
	    String name,
	    String path,
	    Instant uploadedAt
	) {}