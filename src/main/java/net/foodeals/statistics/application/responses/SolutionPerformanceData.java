package net.foodeals.statistics.application.responses;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SolutionPerformanceData {
    private String solutionType;
    private double averageRevenue;
    private double customerSatisfaction;
    private double usageFrequency;
    private double successRate;
}