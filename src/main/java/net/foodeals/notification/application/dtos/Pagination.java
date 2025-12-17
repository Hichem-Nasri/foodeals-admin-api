package net.foodeals.notification.application.dtos;


public record Pagination(
        int currentPage,
        int pageSize,
        int totalPages,
        long totalElements
) {}
