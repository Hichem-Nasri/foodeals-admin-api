package net.foodeals.notification.application.dtos;

public record Overview(int totalSent, int totalDelivered, int totalFailed, int totalRead, double deliveryRate, double readRate) {}
