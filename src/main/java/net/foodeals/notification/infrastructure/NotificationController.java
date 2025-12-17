package net.foodeals.notification.infrastructure;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import net.foodeals.notification.application.dtos.NotificationDetailResponse;
import net.foodeals.notification.application.dtos.NotificationRequest;
import net.foodeals.notification.application.dtos.NotificationResponse;
import net.foodeals.notification.application.dtos.NotificationStatsResponse;
import net.foodeals.notification.application.dtos.NotificationTestRequest;
import net.foodeals.notification.application.service.NotificationService;
import net.foodeals.notification.domain.entity.Notification;

@RestController
@RequestMapping("v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;

	private final SimpMessagingTemplate messagingTemplate;

	@GetMapping
	public Page<NotificationResponse> getAll(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		return notificationService.getNotifications(pageable);
	}

	@PostMapping
	public ResponseEntity<UUID> create(@RequestPart NotificationRequest request,
			@RequestPart(value = "file", required = false) MultipartFile file) {
		String path = null;
		if (file != null) {
			path = notificationService.saveFile(file);
			request=new NotificationRequest(request.title(), request.message(),
					request.type(), request.targetType(), request.targetIds(), path,
					request.scheduledAt(), request.sendImmediately());
		}
		UUID id = notificationService.create(request);
		return ResponseEntity.ok(id);
	}

	@GetMapping("/{id}")
	public NotificationDetailResponse getById(@PathVariable UUID id) {
		return notificationService.getDetails(id);
	}

	@PutMapping("/{id}")
	public void update(@PathVariable UUID id, @RequestPart NotificationRequest request,
			@RequestPart(value = "file", required = false) MultipartFile file) {
		
		String path = null;
		if (file != null) {
			path = notificationService.saveFile(file);
			request=new NotificationRequest(request.title(), request.message(),
					request.type(), request.targetType(), request.targetIds(), path,
					request.scheduledAt(), request.sendImmediately());
		}
		
		notificationService.update(id, request);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable UUID id) {
		notificationService.delete(id);
	}

	@PostMapping("/{id}/send")

	public void sendNow(@PathVariable UUID id,
			@RequestParam(name = "partnerType", required = false, defaultValue = "ALL") 
	String partnerType) {
		notificationService.send(id, partnerType);
	}

	@PostMapping("/{id}/cancel")
	public void cancel(@PathVariable UUID id) {
		notificationService.cancel(id);
	}

	@GetMapping("/analytics")
	public NotificationStatsResponse getStats(@RequestParam String period, @RequestParam Optional<LocalDate> date_from,
			@RequestParam Optional<LocalDate> date_to) {
		return notificationService.getStats(period, date_from, date_to);
	}

	@PostMapping("/test")
	public void sendWebSocketTest(@RequestBody NotificationTestRequest request) {
		messagingTemplate.convertAndSend("/topic/notifications", request);
	}

}
