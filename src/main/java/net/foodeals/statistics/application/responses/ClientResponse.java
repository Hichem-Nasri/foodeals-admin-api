package net.foodeals.statistics.application.responses;



import lombok.*;
import net.foodeals.statistics.application.responses.Option.RegionOption;

import java.util.List;

@Getter @Setter @Builder
public class ClientResponse {

    private Summary summary;
    private TimeSeries timeSeries;
    private UserBase userBase;
    private AppUsage appUsage;
    private OrderBehavior orderBehavior;
    private Geographic geographic;
    private Satisfaction satisfaction;
    private CustomerJourney customerJourney;
    private FilterOptions filterOptions;

    @Getter @Setter @Builder
    public static class Summary {
        private Card totalUsers;
        private Card activeUsers;
        private Card dailyActiveUsers;
        private Card userRetentionRate;
        private Card averageSessionDuration;
        private Card customerLifetimeValue;
    }

    @Getter @Setter @Builder
    public static class TimeSeries {
        private List<TimeSeriesDataPoint> userGrowth;
        private List<TimeSeriesDataPoint> engagementTrends;
        private List<TimeSeriesDataPoint> orderBehavior;
        private List<TimeSeriesDataPoint> retentionMetrics;
    }

    @Getter @Setter @Builder
    public static class Card {
        private String title;
        private long value;
        private String iconType;
        private String className;
        private boolean currency;
        private Double change;
        private String trend;
    }

    @Getter @Setter @Builder
    public static class UserBase {
        private List<AcquisitionMetricsData> acquisitionMetrics;
        private List<UserSegmentationData> userSegmentation;
        private List<CohortAnalysisData> cohortAnalysis;
        private List<RetentionAnalysisData> retentionAnalysis;
    }

    @Getter @Setter @Builder
    public static class AppUsage {
        private List<SessionMetricsData> sessionMetrics;
        private List<FeatureUsageData> featureUsage;
        private List<EngagementPatternData> engagementPatterns;
        private List<UsabilityMetricsData> usabilityMetrics;
    }

    @Getter @Setter @Builder
    public static class OrderBehavior {
        private List<OrderFrequencyData> orderFrequency;
        private List<SpendingPatternData> spendingPatterns;
        private List<PreferenceAnalysisData> preferenceAnalysis;
        private List<BasketAnalysisData> basketAnalysis;
    }

    @Getter @Setter @Builder
    public static class Geographic {
        private List<UserDistributionData> userDistribution;
        private List<RegionalPreferenceData> regionalPreferences;
        private List<MarketPenetrationData> marketPenetration;
        private List<LocationInsightData> locationInsights;
    }

    @Getter @Setter @Builder
    public static class Satisfaction {
        private List<RatingAnalysisData> ratingAnalysis;
        private List<FeedbackAnalysisData> feedbackAnalysis;
        private List<NPSMetricsData> npsMetrics;
        private List<SupportMetricsData> supportMetrics;
    }

    @Getter @Setter @Builder
    public static class CustomerJourney {
        private List<AcquisitionFunnelData> acquisitionFunnel;
        private List<OnboardingMetricsData> onboardingMetrics;
        private List<LifecycleStageData> lifecycleStages;
        private List<ChurnAnalysisData> churnAnalysis;
    }

    @Getter @Setter @Builder
    public static class FilterOptions {
        private List<RegionOption> regions;
        private List<UserTypeOption> userTypes;
        private List<AgeGroupOption> ageGroups;
        private List<AcquisitionSourceOption> acquisitionSources;
    }
    @Getter @Setter @Builder
    public static class PartnerPerformanceData {
        private String id;
        private String name;
        private String type;
        private String location;
        private double totalRevenue;
        private int ordersProcessed;
        private double satisfactionScore;
        private List<String> solutionsUsed;
        private double performanceScore;
        private String trend; // 'up' | 'down' | 'stable'
    }

    @Getter @Setter @Builder
    public static class PerformanceDistributionData {
        private String performanceRange; // e.g., "80-100", "60-79"
        private int partnerCount;
        private double percentage;
    }

    @Getter @Setter @Builder
    public static class EngagementMetricsData {
        private String name; // month or metric name
        private int loginFrequency;
        private double featureUsage;
        private int supportTickets;
        private double engagementScore;
    }

    @Getter @Setter @Builder
    public static class SatisfactionScoreData {
        private String name;
        private double score;
        private double responseRate;
        private double npsScore;
    }
    
    @Getter @Setter @Builder
    public static class UserSegmentationData {
        private String segment;
        private int userCount;
        private double percentage;
        private double averageValue;
        private double retentionRate;
        private List<String> characteristics;
    }

    @Getter @Setter @Builder
    public static class CohortAnalysisData {
        private String cohort;
        private int initialSize;
        private List<Double> retentionRates;
        private double cumulativeRevenue;
        private int averageLifetime;
    }

    @Getter @Setter @Builder
    public static class RetentionAnalysisData {
        private String period;
        private double day1Retention;
        private double day7Retention;
        private double day30Retention;
        private double day90Retention;
    }

    @Getter @Setter @Builder
    public static class SessionMetricsData {
        private String name;
        private double averageSessionDuration;
        private double sessionsPerUser;
        private double bounceRate;
        private int screenViews;
    }

    @Getter @Setter @Builder
    public static class FeatureUsageData {
        private String feature;
        private int usageCount;
        private int userCount;
        private double adoptionRate;
        private double engagementScore;
    }

    @Getter @Setter @Builder
    public static class EngagementPatternData {
        private String timeSlot;
        private int activeUsers;
        private int sessionsCount;
        private double averageEngagement;
        private double conversionRate;
    }

    @Getter @Setter @Builder
    public static class UsabilityMetricsData {
        private String metric;
        private double value;
        private double benchmark;
        private String performance; // 'above' | 'at' | 'below'
    }

    @Getter @Setter @Builder
    public static class OrderFrequencyData {
        private String frequency;
        private int userCount;
        private double percentage;
        private double averageOrderValue;
        private double totalRevenue;
    }

    @Getter @Setter @Builder
    public static class SpendingPatternData {
        private String spendingRange;
        private int userCount;
        private double percentage;
        private double totalSpending;
        private double averageFrequency;
    }

    @Getter @Setter @Builder
    public static class PreferenceAnalysisData {
        private String category;
        private int userCount;
        private double percentage;
        private double averageSpending;
        private double growthRate;
    }

    @Getter @Setter @Builder
    public static class BasketAnalysisData {
        private String item;
        private int frequency;
        private double averageQuantity;
        private double revenueContribution;
        private double crossSellPotential;
    }

    @Getter @Setter @Builder
    public static class UserDistributionData {
        private String location;
        private int userCount;
        private double percentage;
        private double averageOrderValue;
        private double marketPenetration;
    }

    @Getter @Setter @Builder
    public static class RegionalPreferenceData {
        private String region;
        private List<String> topCategories;
        private double averageSpending;
        private List<SeasonalTrendData> seasonalTrends;
    }

    @Getter @Setter @Builder
    public static class SeasonalTrendData {
        private String season;
        private List<String> popularItems;
        private double spendingMultiplier;
    }

    @Getter @Setter @Builder
    public static class MarketPenetrationData {
        private String market;
        private int totalAddressable;
        private int currentUsers;
        private double penetrationRate;
        private double growthPotential;
    }

    @Getter @Setter @Builder
    public static class LocationInsightData {
        private String location;
        private List<String> insights;
        private List<String> opportunities;
        private List<String> challenges;
    }

    @Getter @Setter @Builder
    public static class RatingAnalysisData {
        private String name;
        private double averageRating;
        private List<RatingDistributionData> ratingDistribution;
        private int totalRatings;
        private double sentimentScore;
    }

    @Getter @Setter @Builder
    public static class RatingDistributionData {
        private int stars;
        private int count;
        private double percentage;
    }

    @Getter @Setter @Builder
    public static class FeedbackAnalysisData {
        private String category;
        private int positiveCount;
        private int negativeCount;
        private int neutralCount;
        private List<String> topIssues;
        private List<String> improvements;
    }

    @Getter @Setter @Builder
    public static class NPSMetricsData {
        private String name;
        private double npsScore;
        private double promoters;
        private double passives;
        private double detractors;
        private double responseRate;
    }

    @Getter @Setter @Builder
    public static class SupportMetricsData {
        private String name;
        private int ticketCount;
        private double averageResolutionTime;
        private double satisfactionScore;
        private double firstContactResolution;
    }

    @Getter @Setter @Builder
    public static class AcquisitionFunnelData {
        private String stage;
        private int users;
        private double conversionRate;
        private double dropoffRate;
    }

    @Getter @Setter @Builder
    public static class OnboardingMetricsData {
        private String step;
        private double completionRate;
        private double timeToComplete;
        private double dropoffRate;
    }

    @Getter @Setter @Builder
    public static class LifecycleStageData {
        private String stage;
        private int userCount;
        private double percentage;
        private double averageValue;
        private double transitionRate;
    }

    @Getter @Setter @Builder
    public static class ChurnAnalysisData {
        private String name;
        private double churnRate;
        private int churnedUsers;
        private List<ChurnReasonData> churnReasons;
        private List<String> retentionActions;
    }

    @Getter @Setter @Builder
    public static class ChurnReasonData {
        private String reason;
        private int count;
        private double percentage;
    }

    @Getter @Setter @Builder
    public static class RegionOption {
        private String id;
        private String name;
    }

    @Getter @Setter @Builder
    public static class UserTypeOption {
        private String id;
        private String name;
    }

    @Getter @Setter @Builder
    public static class AgeGroupOption {
        private String id;
        private String name;
    }

    @Getter @Setter @Builder
    public static class AcquisitionSourceOption {
        private String id;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class TimeSeriesDataPoint {
        

		private String name;              // Nom du mois ou date
        private long value;            // Valeur globale (ex: total users, revenue, etc.)
        
        // Pour utilisateurs
        private long newUsers;
   

    
}  }

