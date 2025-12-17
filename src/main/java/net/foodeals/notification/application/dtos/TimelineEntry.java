package net.foodeals.notification.application.dtos;

import java.time.LocalDate;

public record TimelineEntry(LocalDate date, int sent, int delivered, int read) {}
