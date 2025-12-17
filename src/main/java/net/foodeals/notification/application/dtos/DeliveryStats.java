package net.foodeals.notification.application.dtos;

public record DeliveryStats(int total, int delivered, int failed, int read, double deliveryRate, double readRate) {}