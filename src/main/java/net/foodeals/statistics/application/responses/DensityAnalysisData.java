package net.foodeals.statistics.application.responses;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DensityAnalysisData {
    private String area;
    private double partnerDensity;
    private String competitionLevel; // "low", "medium", "high"
    private double marketSaturation;
}
