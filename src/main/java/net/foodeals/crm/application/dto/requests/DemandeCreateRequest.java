package net.foodeals.crm.application.dto.requests;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record DemandeCreateRequest(
	     String type,
	     String companyName,
	    List< String> activity,
	    String country,
	     String city,
	    String respansable,
	    String address,
	      String email,
	    String phone,
	    String notes
	) {}
