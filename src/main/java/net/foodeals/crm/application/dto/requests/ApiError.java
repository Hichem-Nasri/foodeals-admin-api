package net.foodeals.crm.application.dto.requests;

import java.util.List;

public record ApiError(String code, String message, List<FieldError> details) {
    public record FieldError(String field, String message) {}
}