package net.foodeals.statistics.application.responses;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryMetricsData {
    private String storeId;
    private String storeName;
    private double inventoryTurnover;
    private int stockouts;
    private double wasteReduction;
    private boolean optimalStockLevel;
}