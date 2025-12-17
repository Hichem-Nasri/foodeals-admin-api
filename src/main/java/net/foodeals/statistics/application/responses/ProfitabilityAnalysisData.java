package net.foodeals.statistics.application.responses;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfitabilityAnalysisData {
    private String partnerId;
    private String partnerName;
    private double revenue;
    private double costs;
    private double profit;
    private double profitMargin;
    private double roi;
}
