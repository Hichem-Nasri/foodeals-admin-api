package net.foodeals.statistics.application.responses;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsageStatisticsData {
    private String solutionType;
    private int dailyActivePartners;
    private int weeklyActivePartners;
    private int monthlyActivePartners;
    private double averageSessionDuration;
}
