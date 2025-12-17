package net.foodeals.notification.application.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.lang.Collections;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.foodeals.notification.application.dtos.DeliveryInfo;
import net.foodeals.notification.application.dtos.DeliveryStats;
import net.foodeals.notification.application.dtos.NotificationDetailResponse;
import net.foodeals.notification.application.dtos.NotificationRequest;
import net.foodeals.notification.application.dtos.NotificationResponse;
import net.foodeals.notification.application.dtos.NotificationStatsResponse;
import net.foodeals.notification.application.dtos.NotificationTestRequest;
import net.foodeals.notification.application.dtos.Overview;
import net.foodeals.notification.application.dtos.TimelineEntry;
import net.foodeals.notification.application.dtos.TypeStats;
import net.foodeals.notification.application.service.NotificationService;
import net.foodeals.notification.domain.entity.Notification;
import net.foodeals.notification.domain.entity.NotificationDelivery;
import net.foodeals.notification.domain.repositories.NotificationDeliveryRepository;
import net.foodeals.notification.domain.repositories.NotificationRepository;
import net.foodeals.user.application.services.UserService;
import net.foodeals.user.domain.entities.User;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	private final NotificationRepository notificationRepository;
	private final NotificationDeliveryRepository deliveryRepository;
	private final UserService userService;
	private final SimpMessagingTemplate messagingTemplate;
	private final ObjectMapper objectMapper;
	@Value("${upload.directory}")
	private String uploadDir;

	public UUID create(NotificationRequest request) {
		Notification notification = new Notification();
		notification.setTitle(request.title());
		notification.setMessage(request.message());
		notification.setType(request.type());
		notification.setTargetType(request.targetType());

		notification.setTargetIds(request.targetIds());

		notification.setScheduledAt(request.scheduledAt());
		notification.setStatus("draft");
        User user =userService.getConnectedUser();
        notification.setCreatedBy(user);
		Notification saved = notificationRepository.save(notification);
		return saved.getId();
	}

	public Page<NotificationResponse> getNotifications(Pageable pageable) {
		return notificationRepository.findAll(pageable)
				.map(notification -> new NotificationResponse(notification.getId(), notification.getTitle(),
						notification.getMessage(), notification.getType(), notification.getTargetType(),
						notification.getTargetIds(), notification.getScheduledAt(), notification.getSentAt(),
						notification.getStatus(), notification.getCreatedBy().getId(),
						LocalDateTime.ofInstant(notification.getCreatedAt(), ZoneId.of("UTC"))));
	}

	public NotificationDetailResponse getDetails(UUID id) {
		Notification notification = notificationRepository.findById(id).orElseThrow();
		List<NotificationDelivery> deliveries = deliveryRepository.findByNotificationId(id);

		int delivered = 0, failed = 0, read = 0;
		List<DeliveryInfo> deliveryInfos = new ArrayList();

		for (NotificationDelivery d : deliveries) {
			if ("delivered".equals(d.getStatus()))
				delivered++;
			if ("failed".equals(d.getStatus()))
				failed++;
			if ("read".equals(d.getStatus()))
				read++;

			deliveryInfos.add(new DeliveryInfo(d.getUser().getId(),
					d.getUser().getName().firstName() + " " + d.getUser().getName().lastName(), d.getDeliveryMethod(),
					d.getStatus(), d.getDeliveredAt(), d.getReadAt()));
		}

		int total = deliveries.size();
		double deliveryRate = total > 0 ? (delivered * 100.0) / total : 0;
		double readRate = total > 0 ? (read * 100.0) / total : 0;

		return new NotificationDetailResponse(notification.getId(), notification.getTitle(), notification.getMessage(),
				notification.getType(), notification.getTargetType(), notification.getTargetIds(),
				notification.getScheduledAt(), notification.getSentAt(), notification.getStatus(),
				notification.getCreatedBy() != null ? notification.getCreatedBy().getId() : null,
				LocalDateTime.ofInstant(notification.getCreatedAt(), ZoneId.of("UTC")),
				new DeliveryStats(total, delivered, failed, read, deliveryRate, readRate), deliveryInfos);
	}

	public void update(UUID id, NotificationRequest request) {
		Notification notification = notificationRepository.findById(id).orElseThrow();
		if (!"draft".equals(notification.getStatus())) {
			throw new IllegalStateException("Only draft notifications can be updated");
		}
		notification.setTitle(request.title());
		notification.setMessage(request.message());
		notification.setType(request.type());
		notification.setTargetType(request.targetType());

		notification.setTargetIds(request.targetIds());

		notification.setScheduledAt(request.scheduledAt());
		notification.setUpdatedAt(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
		notificationRepository.save(notification);
	}

	public void delete(UUID id) {
		notificationRepository.deleteById(id);
	}

	public void send(UUID id, String partnerType) {
	    Notification notification = notificationRepository.findById(id).orElseThrow();
	    notification.setStatus("sent");
	    notification.setSentAt(LocalDateTime.ofInstant(Instant.now(), ZoneId.of("UTC")));
	    notificationRepository.save(notification);

	    messagingTemplate.convertAndSend("/topic/notifications",
	            Map.of(
	                "id", notification.getId(),
	                "title", notification.getTitle(),
	                "message", notification.getMessage(),
	                "type", notification.getType(),
	                "imageUrl", notification.getImageUrl(),          // <— ajouté
	                "partnerType", partnerType.toLowerCase(),        // <— ajouté
	                "created_at", notification.getCreatedAt().toString(),
	                "read", false
	            )
	    );
	}


	public void cancel(UUID id) {
		Notification notification = notificationRepository.findById(id).orElseThrow();
		if (!"scheduled".equals(notification.getStatus())) {
			throw new IllegalStateException("Only scheduled notifications can be cancelled");
		}
		notification.setStatus("draft");
		notificationRepository.save(notification);
	}

	public NotificationStatsResponse getStats(String period, Optional<LocalDate> dateFrom, Optional<LocalDate> dateTo) {
		List<Notification> all = notificationRepository.findAll();
		List<NotificationDelivery> deliveries = deliveryRepository.findAll();

		int totalSent = (int) all.stream().filter(n -> "sent".equals(n.getStatus())).count();
		int totalDelivered = (int) deliveries.stream().filter(d -> "delivered".equals(d.getStatus())).count();
		int totalFailed = (int) deliveries.stream().filter(d -> "failed".equals(d.getStatus())).count();
		int totalRead = (int) deliveries.stream().filter(d -> "read".equals(d.getStatus())).count();

		double deliveryRate = totalSent > 0 ? (totalDelivered * 100.0) / totalSent : 0;
		double readRate = totalSent > 0 ? (totalRead * 100.0) / totalSent : 0;

		Map<String, Object> byType = all.stream().collect(
				Collectors.groupingBy(Notification::getType, Collectors.collectingAndThen(Collectors.toList(), list -> {
					List<UUID> ids = list.stream().map(Notification::getId).toList();
					List<NotificationDelivery> typeDeliveries = deliveries.stream()
							.filter(d -> ids.contains(d.getNotification().getId())).toList();

					int sent = list.size();
					int delivered = (int) typeDeliveries.stream().filter(d -> "delivered".equals(d.getStatus()))
							.count();
					int read = (int) typeDeliveries.stream().filter(d -> "read".equals(d.getStatus())).count();
					return new TypeStats(sent, delivered, read);
				})));

		List<TimelineEntry> timeline = all.stream().filter(n -> n.getSentAt() != null).collect(Collectors
				.groupingBy(n -> n.getSentAt().atZone(ZoneId.systemDefault()).toLocalDate(), Collectors.toList()))
				.entrySet().stream().map(e -> {
					LocalDate date = e.getKey();
					List<UUID> ids = e.getValue().stream().map(Notification::getId).toList();
					List<NotificationDelivery> dateDeliveries = deliveries.stream()
							.filter(d -> ids.contains(d.getNotification().getId())).toList();

					int sent = e.getValue().size();
					int delivered = (int) dateDeliveries.stream().filter(d -> "delivered".equals(d.getStatus()))
							.count();
					int read = (int) dateDeliveries.stream().filter(d -> "read".equals(d.getStatus())).count();
					return new TimelineEntry(date, sent, delivered, read);
				}).sorted(Comparator.comparing(TimelineEntry::date)).toList();

		return new NotificationStatsResponse(
				new Overview(totalSent, totalDelivered, totalFailed, totalRead, deliveryRate, readRate), byType,
				timeline);
	}

	public void testNotification(NotificationTestRequest request) {
		// TODO: Implémenter envoi ciblé vers les admins connectés
		System.out.println("Test Notification Sent: " + request);
	}

	private List<UUID> parseTargetIds(String json) {
		try {
			return json != null ? objectMapper.readValue(json, new TypeReference<>() {
			}) : List.of();
		} catch (JsonProcessingException e) {
			return List.of();
		}
	}
	
	@Override
	public String saveFile(MultipartFile file) {

		Path path = Paths.get(uploadDir, file.getOriginalFilename());
		try {
			Files.createDirectories(path.getParent());
			Files.write(path, file.getBytes());
		} catch (IOException e) {
			throw new RuntimeException("Problem while saving the file.", e);
		}

		return file.getOriginalFilename();
	}
}
