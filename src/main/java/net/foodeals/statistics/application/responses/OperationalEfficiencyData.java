package net.foodeals.statistics.application.responses;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperationalEfficiencyData {
    private String storeId;
    private String storeName;
    private double orderFulfillmentTime;
    private double errorRate;
    private double customerSatisfaction;
    private double efficiencyScore;
}
