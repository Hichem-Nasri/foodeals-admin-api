package net.foodeals.statistics.application.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GlobalStatisticsResponseDto {

    private Summary summary;
    private TimeSeries timeSeries;
    private PartnerPerformance partnerPerformance;
    private UserEngagement userEngagement;
    private FilterOptions filterOptions;

    // --- Summary cards ---
    @Data
    @Builder
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Summary {
        private Card totalFoodWastePrevented;
        private Card totalTransactions;
        private Card commandsProcessed;
        private Card clientAdoption;
        private Card proAdoption;
        private Card totalPartners;
    }

    @Data
    @Builder
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Card {
        private String title;
        private long value;
        private String iconType;
        private String className;
        private Boolean currency;
    }

    // --- Time Series ---
    @Data
    @Builder
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeSeries {
        private List<TimeSeriesDataPoint> wasteReduction;
        private List<TimeSeriesDataPoint> solutionComparison;
        private List<TimeSeriesDataPoint> partnerEngagement;
    }

    @Data
    @Builder
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeSeriesDataPoint {
        private String name;
        private long value;
        private Long DLC;
        private Long MARKET;
        private Long DONATE;
        private Integer partners;
    }

    // --- Partner Performance ---
    @Data
    @Builder
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PartnerPerformance {
        private List<StorePerformanceData> topPerformingStores;
        private List<GeographicData> geographicDistribution;
        private List<ParticipationRateData> participationRate;
    }

    
    @Builder
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StorePerformanceData {
        private String name;
        private String location;
        private int items;
        private double wastePrevented;
    }

    @Data
    @Builder
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GeographicData {
        private String name;
        private long value;
    }

    
    @Builder
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParticipationRateData {
        private String name;
        private double value;
        private String solutionType;
    }

    // --- User Engagement ---
    @Data
    @Builder
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserEngagement {
        private List<EngagementDataPoint> clientAppUsage;
        private List<EngagementDataPoint> proAppAdoption;
        private List<EngagementDataPoint> popularCategories;
    }

    @Data
    @Builder
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EngagementDataPoint {
        private String name;
        private long value;
    }

    // --- Filters ---
    @Data
    @Builder
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FilterOptions {
        private List<PartnerOption> partners;
        private List<RegionOption> regions;
    }

    @Data
    @Builder
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PartnerOption {
        private String id;
        private String name;
    }

    @Data
    @Builder
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegionOption {
        private String id;
        private String name;
    }
}
