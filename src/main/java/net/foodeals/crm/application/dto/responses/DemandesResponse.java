package net.foodeals.crm.application.dto.responses;

import java.util.List;

import net.foodeals.crm.application.dto.requests.CrmDemandeResponseDto;

public record DemandesResponse(
	    List<CrmDemandeResponseDto> demandes,
	    Pagination pagination,
	    Filters filters
	) {
	    public record Pagination(int currentPage,int totalPages,long totalItems,int itemsPerPage,boolean hasNextPage,boolean hasPreviousPage){}
	    public record Filters(List<String> countries,List<String> cities,List<String> activities,List<String> statuses){}
	}