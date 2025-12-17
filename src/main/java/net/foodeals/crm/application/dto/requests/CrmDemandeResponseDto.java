package net.foodeals.crm.application.dto.requests;

import java.util.List;

public record CrmDemandeResponseDto(
	    String id,
	    String companyName,
	    List<String> activity,
	    String country,
	    String city,
	    String date,
	    String respansable,
	    String address,
	    String email,
	    String phone,
	    String status,
	    String createdAt,
	    String updatedAt,
	    String notes,
	    List<DocumentDto> documents,
	    List<HistoryEntryDto> history
	) {}