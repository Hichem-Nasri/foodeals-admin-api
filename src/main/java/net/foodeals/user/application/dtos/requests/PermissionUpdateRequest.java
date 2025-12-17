package net.foodeals.user.application.dtos.requests;

public record PermissionUpdateRequest(String permission, boolean isGranted) {}