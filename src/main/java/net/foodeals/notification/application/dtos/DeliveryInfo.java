package net.foodeals.notification.application.dtos;

import java.time.LocalDateTime;

public record DeliveryInfo(Integer userId, String userName, String deliveryMethod, String status, LocalDateTime deliveredAt, LocalDateTime readAt) {}