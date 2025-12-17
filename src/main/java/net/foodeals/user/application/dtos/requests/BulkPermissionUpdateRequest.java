package net.foodeals.user.application.dtos.requests;

import java.util.List;

public record BulkPermissionUpdateRequest(List<PermissionChangeDto> permissions) {}