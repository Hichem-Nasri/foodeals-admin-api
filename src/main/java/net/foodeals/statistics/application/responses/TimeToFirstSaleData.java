package net.foodeals.statistics.application.responses;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeToFirstSaleData {
    private String partnerType;
    private double averageDays;
    private double medianDays;
    private double successRate;
}
