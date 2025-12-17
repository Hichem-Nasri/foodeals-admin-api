package net.foodeals.crm.application.dto.requests;

public record DemandeSearchQuery(
	    String type,
	    String country,
	    String city,
	    String activity,
	    String status,
	    String dateFrom,
	    String dateTo,
	    String search,
	    Integer page,
	    Integer limit,
	    String sort,
	    String order
	) {}