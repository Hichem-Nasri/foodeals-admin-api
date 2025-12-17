package net.foodeals.user.application.dtos.responses;

public record PermissionChangeResult(String permission, boolean isGranted, String action) {}