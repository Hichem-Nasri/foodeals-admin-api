package net.foodeals.user.application.dtos.requests;

import java.util.List;

public record RolePermissionsDto(String role, String label, List<String> basePermissions) {}