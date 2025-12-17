package net.foodeals.statistics.application.responses;



import lombok.*;
import net.foodeals.statistics.application.responses.GlobalStatisticsResponseDto.StorePerformanceData;
import net.foodeals.statistics.application.responses.GlobalStatisticsResponseDto.TimeSeriesDataPoint;
import net.foodeals.statistics.application.responses.Option.PartnerTypeOption;
import net.foodeals.statistics.application.responses.Option.RegionOption;
import net.foodeals.statistics.application.responses.Option.SolutionTypeOption;
import net.foodeals.statistics.application.responses.Option.StatusOption;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartnerResponse {

    private Summary summary;
    private TimeSeries timeSeries;
    private Performance performance;
    private Stores stores;
    private Solutions solutions;
    private Geographic geographic;
    private Onboarding onboarding;
    private Revenue revenue;
    private FilterOptions filterOptions;
    
    @Getter
    @Setter
    @Builder
    public static class PartnerPerformanceData {
        private String id;
        private String name;
        private String type;
        private String location;
        private long totalRevenue;
        private int ordersProcessed;
        private double satisfactionScore;
        private List<String> solutionsUsed;
        private int performanceScore;
        private String trend;
    }

    @Getter
    @Setter
    @Builder
    public static class PerformanceDistributionData {
        private String performanceRange;
        private int partnerCount;
        private double percentage;
    }

    @Getter
    @Setter
    @Builder
    public static class EngagementMetricsData {
        private String name;
        private double loginFrequency;
        private double featureUsage;
        private double supportTickets;
        private double engagementScore;
    }

    @Getter
    @Setter
    @Builder
    public static class SatisfactionScoreData {
        private String name;
        private double score;
        private double responseRate;
        private double npsScore;
    }

    

    @Getter
    @Setter
    @Builder
    public static class Summary {
        private Card totalPartners;
        private Card activePartners;
        private Card totalStores;
        private Card averageRevenuePerPartner;
        private Card partnerSatisfactionScore;
        private Card solutionAdoptionRate;
    }

    @Getter
    @Setter
    @Builder
    public static class Card {
        private String title;
        private long value;
        private String iconType;
        private String className;
        private boolean currency;
        private Double change;
        private String trend;
    }

    @Getter
    @Setter
    @Builder
    public static class TimeSeries {
        private List<TimeSeriesDataPoint> partnerGrowth;
        private List<TimeSeriesDataPoint> solutionAdoption;
        private List<TimeSeriesDataPoint> partnerPerformance;
        private List<TimeSeriesDataPoint> revenueGrowth;
    }

    @Getter
    @Setter
    @Builder
    public static class Performance {
        private List<PartnerPerformanceData> topPerformingPartners;
        private List<PerformanceDistributionData> performanceDistribution;
        private List<EngagementMetricsData> engagementMetrics;
        private List<SatisfactionScoreData> satisfactionScores;
    }

    @Getter
    @Setter
    @Builder
    public static class Stores {
        private List<StorePerformanceData> storePerformance;
        private List<InventoryMetricsData> inventoryMetrics;
        private List<SalesAnalyticsData> salesAnalytics;
        private List<OperationalEfficiencyData> operationalEfficiency;
    }

    @Getter
    @Setter
    @Builder
    public static class Solutions {
        private List<SolutionAdoptionData> adoptionRates;
        private List<SolutionPerformanceData> solutionPerformance;
        private List<MigrationPatternData> migrationPatterns;
        private List<UsageStatisticsData> usageStatistics;
    }

    @Getter
    @Setter
    @Builder
    public static class Geographic {
        private List<RegionalDistributionData> regionalDistribution;
        private List<CityPerformanceData> cityPerformance;
        private List<DensityAnalysisData> densityAnalysis;
        private List<ExpansionOpportunityData> expansionOpportunities;
    }

    @Getter
    @Setter
    @Builder
    public static class Onboarding {
        private List<AcquisitionMetricsData> acquisitionMetrics;
        private List<ActivationRateData> activationRates;
        private List<TimeToFirstSaleData> timeToFirstSale;
        private List<OnboardingFunnelData> onboardingFunnel;
    }

    @Getter
    @Setter
    @Builder
    public static class Revenue {
        private List<RevenueByPartnerData> revenueByPartner;
        private List<CommissionTrackingData> commissionTracking;
        private List<GrowthMetricsData> growthMetrics;
        private List<ProfitabilityAnalysisData> profitabilityAnalysis;
    }

    @Getter
    @Setter
    @Builder
    public static class FilterOptions {
        private List<RegionOption> regions;
        private List<PartnerTypeOption> partnerTypes;
        private List<SolutionTypeOption> solutionTypes;
        private List<StatusOption> statuses;
    }
}
