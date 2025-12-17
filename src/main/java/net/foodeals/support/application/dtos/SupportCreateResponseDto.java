package net.foodeals.support.application.dtos;

import java.util.UUID;

public record SupportCreateResponseDto(
	    String message,
	    String type,          // "admin_response" | "internal_note" | "user_message"
	    Boolean is_internal,  // par défaut false (sera forcé true si type = internal_note)
	    Integer author_id,
	    String author_name,
	    String author_email,
	    String author_role
	) {}