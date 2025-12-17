package net.foodeals.crm.application.dto.requests;

public record HistoryEntryDto(String id, String action, String performedBy, String timestamp, String details) {}