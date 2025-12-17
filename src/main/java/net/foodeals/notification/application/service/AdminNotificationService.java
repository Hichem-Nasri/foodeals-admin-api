package net.foodeals.notification.application.service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import net.foodeals.notification.application.dtos.*;
import net.foodeals.notification.domain.entity.AdminNotification;
import net.foodeals.notification.domain.entity.AdminNotificationPreferences;
import net.foodeals.notification.domain.entity.AdminNotificationState;
import net.foodeals.notification.domain.enums.AdminNotificationPriority;
import net.foodeals.notification.domain.enums.AdminNotificationType;
import net.foodeals.notification.domain.repositories.AdminNotificationPreferencesRepository;
import net.foodeals.notification.domain.repositories.AdminNotificationRepository;
import net.foodeals.notification.domain.repositories.AdminNotificationStateRepository;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import net.foodeals.user.application.services.UserService;
import net.foodeals.user.domain.entities.User;

@Service
@RequiredArgsConstructor
public class AdminNotificationService {

    private final AdminNotificationRepository notificationRepo;
    private final AdminNotificationStateRepository stateRepo;
    private final AdminNotificationPreferencesRepository prefsRepo;

    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messaging;

    private static final String WS_TOPIC = "/topic/admin-notifications";

    private Map<String, Object> parseMetadata(String json) {
        try {
            if (json == null || json.isBlank()) return Map.of();
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            return Map.of();
        }
    }

    private AdminNotificationDto toDto(AdminNotification n, boolean isRead) {
        return new AdminNotificationDto(
                n.getId(),
                n.getType(),
                n.getTitle(),
                n.getMessage(),
                n.getIcon(),
                isRead,
                n.getActionUrl(),
                parseMetadata(n.getMetadataJson()),
                n.getSourceApp(),
                n.getPriority(),
                n.getCreatedAt()
        );
    }

    /** Create a new admin notification and create states for all admins you want to target.
     *  (Tu peux remplacer le "getAllAdmins()" par un ciblage plus fin)
     */
    @Transactional
    public UUID create(AdminNotification n, List<User> targetAdmins) {
        AdminNotification saved = notificationRepo.save(n);
        List<AdminNotificationState> states = targetAdmins.stream()
                .map(a -> AdminNotificationState.unread(saved, a))
                .toList();
        stateRepo.saveAll(states);

        // WS: notification:new
        messaging.convertAndSend(WS_TOPIC, Map.of(
                "event", "notification:new",
                "payload", toDto(saved, false)
        ));

        // WS: count_update (broadcast simple)
        messaging.convertAndSend(WS_TOPIC, Map.of(
                "event", "notification:count_update",
                "payload", buildUnreadCountPayloadForCurrentUserIfPossible()
        ));

        return saved.getId();
    }

    // helper best-effort (si tu as un "room per user", tu feras mieux)
    private Map<String, Object> buildUnreadCountPayloadForCurrentUserIfPossible() {
        try {
            User u = userService.getConnectedUser();
            long total = stateRepo.countUnread(u.getId());
            Map<String, Long> byType = toUnreadByType(u.getId());
            return Map.of("total", total, "byType", byType);
        } catch (Exception e) {
            return Map.of();
        }
    }

    private Map<String, Long> toUnreadByType(Integer adminId) {
        List<Object[]> rows = stateRepo.countUnreadByType(adminId);
        Map<String, Long> out = new HashMap<>();
        for (Object[] r : rows) {
            out.put(String.valueOf(r[0]), (Long) r[1]);
        }
        return out;
    }

    @Transactional(readOnly = true)
    public ApiResponse<AdminNotificationListData> list(
            int page, int size,
            AdminNotificationType type,
            Boolean isRead,
            AdminNotificationPriority priority,
            Instant dateFrom,
            Instant dateTo,
            String sortBy, String sortDir
    ) {
        User admin = userService.getConnectedUser();

        Sort sort = Sort.by("createdAt");
        if ("priority".equalsIgnoreCase(sortBy)) sort = Sort.by("priority");
        sort = "asc".equalsIgnoreCase(sortDir) ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<AdminNotification> spec =
                Specification.where(AdminNotificationSpecs.typeEq(type))
                        .and(AdminNotificationSpecs.priorityEq(priority))
                        .and(AdminNotificationSpecs.createdBetween(dateFrom, dateTo));

        Page<AdminNotification> notifPage = notificationRepo.findAll(spec, pageable);

        // Charger states pour savoir read/unread
        List<UUID> ids = notifPage.getContent().stream().map(AdminNotification::getId).toList();
        Map<UUID, Boolean> readMap = stateRepo.findAll().stream()
                .filter(s -> s.getAdmin().getId().equals(admin.getId()))
                .filter(s -> ids.contains(s.getNotification().getId()))
                .collect(Collectors.toMap(s -> s.getNotification().getId(), AdminNotificationState::isRead));

        List<AdminNotificationDto> dtos = notifPage.getContent().stream()
                .map(n -> toDto(n, readMap.getOrDefault(n.getId(), false)))
                .filter(dto -> isRead == null || dto.isRead() == isRead.booleanValue())
                .toList();

        long unreadCount = stateRepo.countUnread(admin.getId());

        AdminNotificationListData data = new AdminNotificationListData(
                dtos,
                new Pagination(
                        notifPage.getNumber(),
                        notifPage.getSize(),
                        notifPage.getTotalPages(),
                        notifPage.getTotalElements()
                ),
                unreadCount
        );

        return ApiResponse.ok(data);
    }

    @Transactional(readOnly = true)
    public ApiResponse<AdminNotificationDto> getOne(UUID id) {
        User admin = userService.getConnectedUser();
        AdminNotification n = notificationRepo.findById(id).orElseThrow();
        boolean isRead = stateRepo.findByNotificationIdAndAdminId(id, admin.getId())
                .map(AdminNotificationState::isRead)
                .orElse(false);
        return ApiResponse.ok(toDto(n, isRead));
    }

    @Transactional
    public ApiResponse<MarkReadResponse> markRead(UUID id) {
        User admin = userService.getConnectedUser();
        AdminNotificationState s = stateRepo.findByNotificationIdAndAdminId(id, admin.getId())
                .orElseThrow();

        if (!s.isRead()) {
            s.markRead();
            stateRepo.save(s);
        }

        // WS: notification:read
        messaging.convertAndSend(WS_TOPIC, Map.of(
                "event", "notification:read",
                "payload", Map.of("notificationId", id, "readAt", s.getReadAt())
        ));

        // WS: count_update
        messaging.convertAndSend(WS_TOPIC, Map.of(
                "event", "notification:count_update",
                "payload", Map.of(
                        "total", stateRepo.countUnread(admin.getId()),
                        "byType", toUnreadByType(admin.getId())
                )
        ));

        return ApiResponse.ok(new MarkReadResponse(id, true, s.getReadAt()));
    }

    @Transactional
    public ApiResponse<MarkAllReadResponse> markAllRead(AdminNotificationType type) {
        User admin = userService.getConnectedUser();
        int marked = stateRepo.markAllRead(admin.getId(), type);

        messaging.convertAndSend(WS_TOPIC, Map.of(
                "event", "notification:count_update",
                "payload", Map.of(
                        "total", stateRepo.countUnread(admin.getId()),
                        "byType", toUnreadByType(admin.getId())
                )
        ));

        return ApiResponse.ok(new MarkAllReadResponse(marked, Instant.now()));
    }

    @Transactional(readOnly = true)
    public ApiResponse<UnreadCountResponse> unreadCount() {
        User admin = userService.getConnectedUser();
        long total = stateRepo.countUnread(admin.getId());
        Map<String, Long> byType = toUnreadByType(admin.getId());
        return ApiResponse.ok(new UnreadCountResponse(total, byType));
    }

    @Transactional
    public void delete(UUID id) {
        // Ici tu peux décider : delete global (notif) ou delete seulement la visibilité pour l’admin
        // Doc dit DELETE notification → je fais delete global + states.
        notificationRepo.deleteById(id);

        messaging.convertAndSend(WS_TOPIC, Map.of(
                "event", "notification:deleted",
                "payload", Map.of("notificationId", id)
        ));
    }

    @Transactional
    public ApiResponse<BulkDeleteResponse> bulkDelete(List<UUID> ids) {
        // delete global
        ids.forEach(notificationRepo::deleteById);

        messaging.convertAndSend(WS_TOPIC, Map.of(
                "event", "notification:deleted",
                "payload", Map.of("notificationIds", ids)
        ));

        return ApiResponse.ok(new BulkDeleteResponse(ids.size()));
    }

    @Transactional(readOnly = true)
    public ApiResponse<PreferencesResponse> getPreferences() {
        User admin = userService.getConnectedUser();

        AdminNotificationPreferences prefs = prefsRepo.findByAdminId(admin.getId())
                .orElseGet(() -> prefsRepo.save(AdminNotificationPreferences.defaults(admin)));

        return ApiResponse.ok(new PreferencesResponse(
                admin.getId(), // si tu as uuid côté user, sinon adapte
                prefs.isOrderNotifications(),
                prefs.isSupportNotifications(),
                prefs.isReportNotifications(),
                prefs.isSystemNotifications(),
                prefs.isEmailDigest(),
                prefs.isSoundEnabled()
        ));
    }

    @Transactional
    public ApiResponse<PreferencesResponse> updatePreferences(PreferencesUpdateRequest req) {
        User admin = userService.getConnectedUser();

        AdminNotificationPreferences prefs = prefsRepo.findByAdminId(admin.getId())
                .orElseGet(() -> AdminNotificationPreferences.defaults(admin));

        prefs.setOrderNotifications(req.orderNotifications());
        prefs.setSupportNotifications(req.supportNotifications());
        prefs.setReportNotifications(req.reportNotifications());
        prefs.setSystemNotifications(req.systemNotifications());
        prefs.setEmailDigest(req.emailDigest());
        prefs.setSoundEnabled(req.soundEnabled());

        prefs = prefsRepo.save(prefs);

        return ApiResponse.ok(new PreferencesResponse(
                admin.getId(),
                prefs.isOrderNotifications(),
                prefs.isSupportNotifications(),
                prefs.isReportNotifications(),
                prefs.isSystemNotifications(),
                prefs.isEmailDigest(),
                prefs.isSoundEnabled()
        ));
    }
}

