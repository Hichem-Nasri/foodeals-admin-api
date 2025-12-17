package net.foodeals.notification.application.service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import net.foodeals.notification.application.dtos.NotificationDetailResponse;
import net.foodeals.notification.application.dtos.NotificationRequest;
import net.foodeals.notification.application.dtos.NotificationResponse;
import net.foodeals.notification.application.dtos.NotificationStatsResponse;
import net.foodeals.notification.application.dtos.NotificationTestRequest;
import net.foodeals.payment.domain.entities.Enum.PartnerType;

public interface NotificationService {

	public UUID create(NotificationRequest request);

	public Page<NotificationResponse> getNotifications(Pageable pageable);

	public NotificationDetailResponse getDetails(UUID id);

	void update(UUID id, NotificationRequest request);

	public void delete(UUID id);

	public void send(UUID id,String partnerType);

	public void cancel(UUID id);

	public NotificationStatsResponse getStats(String period, Optional<LocalDate> dateFrom, Optional<LocalDate> dateTo);

	public void testNotification(NotificationTestRequest request);
	
	public String saveFile(MultipartFile file);

}
