package net.foodeals.user.application.dtos.requests;

import java.time.Instant;
import java.util.List;

public record UserWithPermissionsDto(
	    Integer id, String firstName, String lastName, String email, String role, String status,
	    List<String> customPermissions, Instant createdAt, Instant updatedAt, Instant lastLogin) {}
