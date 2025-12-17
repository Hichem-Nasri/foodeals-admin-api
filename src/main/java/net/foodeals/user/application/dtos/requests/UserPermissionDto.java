package net.foodeals.user.application.dtos.requests;

import java.time.Instant;

public record UserPermissionDto(Integer userId, String permission, boolean isGranted, Instant updatedAt) {}
