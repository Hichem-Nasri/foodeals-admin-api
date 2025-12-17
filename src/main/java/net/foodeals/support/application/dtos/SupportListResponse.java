package net.foodeals.support.application.dtos;

import java.util.List;

public record SupportListResponse(
	    List<SupportListItemDto> supports,
	    Pagination pagination
	) {
	    public record Pagination(
	        int current_page, int total_pages, long total_items, int per_page, boolean has_next, boolean has_prev
	    ) {}
	}
