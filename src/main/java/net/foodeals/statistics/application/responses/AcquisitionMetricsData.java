package net.foodeals.statistics.application.responses;

import java.util.List;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AcquisitionMetricsData {
    private String name;
    private int newPartners;
    private double acquisitionCost;
    private double conversionRate;
    private List<SourceBreakdownData> sourceBreakdown;
}

