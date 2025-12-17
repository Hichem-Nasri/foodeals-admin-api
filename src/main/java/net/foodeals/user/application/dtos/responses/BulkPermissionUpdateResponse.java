package net.foodeals.user.application.dtos.responses;

import java.time.Instant;
import java.util.List;

public record BulkPermissionUpdateResponse(Integer userId, List<PermissionChangeResult> updatedPermissions, Instant updatedAt) {}