package net.foodeals.notification.infrastructure;


import java.time.Instant;
import java.util.*;

import net.foodeals.notification.application.dtos.*;
import net.foodeals.notification.application.service.AdminNotificationService;
import net.foodeals.notification.domain.enums.AdminNotificationPriority;
import net.foodeals.notification.domain.enums.AdminNotificationType;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/admin/notifications")
@RequiredArgsConstructor
public class AdminNotificationController {

    private final AdminNotificationService service;

    @GetMapping
    public ApiResponse<AdminNotificationListData> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) AdminNotificationType type,
            @RequestParam(required = false) Boolean isRead,
            @RequestParam(required = false) AdminNotificationPriority priority,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant dateTo,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        return service.list(page, size, type, isRead, priority, dateFrom, dateTo, sortBy, sortDir);
    }

    @GetMapping("/{id}")
    public ApiResponse<AdminNotificationDto> getOne(@PathVariable UUID id) {
        return service.getOne(id);
    }

    @PutMapping("/{id}/read")
    public ApiResponse<MarkReadResponse> markRead(@PathVariable UUID id) {
        return service.markRead(id);
    }

    @PostMapping("/mark-all-read")
    public ApiResponse<MarkAllReadResponse> markAllRead(@RequestBody(required = false) Map<String, String> body) {
        AdminNotificationType type = null;
        if (body != null && body.get("type") != null) {
            type = AdminNotificationType.valueOf(body.get("type"));
        }
        return service.markAllRead(type);
    }

    @GetMapping("/unread-count")
    public ApiResponse<UnreadCountResponse> unreadCount() {
        return service.unreadCount();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @PostMapping("/bulk-delete")
    public ApiResponse<BulkDeleteResponse> bulkDelete(@RequestBody BulkDeleteRequest req) {
        return service.bulkDelete(req.notificationIds());
    }

    @GetMapping("/preferences")
    public ApiResponse<PreferencesResponse> getPreferences() {
        return service.getPreferences();
    }

    @PutMapping("/preferences")
    public ApiResponse<PreferencesResponse> updatePreferences(@RequestBody PreferencesUpdateRequest req) {
        return service.updatePreferences(req);
    }
}
