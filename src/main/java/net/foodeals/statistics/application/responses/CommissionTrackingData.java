package net.foodeals.statistics.application.responses;

import lombok.*
;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommissionTrackingData {
    private String name;
    private double totalCommissions;
    private double averageCommissionRate;
    private double commissionsGrowth;
}
