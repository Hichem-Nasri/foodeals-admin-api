package net.foodeals.notification.application.dtos;


import java.time.Instant;

public record MarkAllReadResponse(int markedCount, Instant timestamp) {}