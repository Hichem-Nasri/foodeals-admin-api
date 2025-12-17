package net.foodeals.support.application.dtos;

public record SupportUpdateResponseDto(
	    String message,
	    Boolean is_internal // ignoré si type != internal_note (on ne change pas le type)
	) {}