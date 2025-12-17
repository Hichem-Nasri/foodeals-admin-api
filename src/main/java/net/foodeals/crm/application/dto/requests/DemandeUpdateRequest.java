package net.foodeals.crm.application.dto.requests;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record DemandeUpdateRequest(
	    String type,
	    String companyName,
	    List<String> activity,
	    String country,
	    String city,
	    String respansable,
	    String address,
	    @Email String email,
	    String phone,
	    @Pattern(regexp="pending|approved|rejected") String status,
	    String notes
	) {}