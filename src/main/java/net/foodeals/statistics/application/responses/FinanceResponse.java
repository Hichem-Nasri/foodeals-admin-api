package net.foodeals.statistics.application.responses;


import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinanceResponse {

    private Summary summary;
    private TimeSeries timeSeries;
    private Subscriptions subscriptions;
    private PartnerFinancials partnerFinancials;
    private Transactions transactions;
    private Delivery delivery;
    private Operational operational;
    private FilterOptions filterOptions;

    // --- Summary cards ---
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Summary {
        private SummaryCard totalRevenue;
        private SummaryCard monthlyRecurringRevenue;
        private SummaryCard totalTransactionFees;
        private SummaryCard partnerCommissions;
        private SummaryCard operationalCosts;
        private SummaryCard netProfit;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SummaryCard {
        private String title;
        private long value;
        private String iconType;
        private String className;
        private boolean currency;
        private Double change;
        private String trend; // "up" | "down"
    }

    // --- Time Series ---
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeSeries {
        private List<TimeSeriesDataPoint> revenueGrowth;
        private List<TimeSeriesDataPoint> revenueStreams;
        private List<TimeSeriesDataPoint> subscriptionMetrics;
        private List<TimeSeriesDataPoint> partnerFinancials;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeSeriesDataPoint {
        private String name;
        private double value;
        private Double subscription;
        private Double commission;
        private Double delivery;
        private Double fees;
    }

    // --- Subscriptions ---
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Subscriptions {
        private List<SubscriptionMetrics> activeSubscriptions;
        private List<ChurnDataPoint> churnAnalysis;
        private List<RevenueDataPoint> subscriptionRevenue;
        private List<PlanDistributionData> planDistribution;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubscriptionMetrics {
        private String id;
        private String planName;
        private int activeCount;
        private double monthlyRevenue;
        private double churnRate;
        private double averageLifetime;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChurnDataPoint {
        private String name;
        private double churnRate;
        private int newSubscriptions;
        private int canceledSubscriptions;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RevenueDataPoint {
        private String name;
        private double recurring;
        private double oneTime;
        private double total;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlanDistributionData {
        private String planName;
        private int subscriberCount;
        private double revenue;
        private double percentage;
    }

    // --- Partner Financials ---
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PartnerFinancials {
        private List<PartnerFinancialData> topEarningPartners;
        private List<CommissionData> commissionBreakdown;
        private List<PaymentProcessingData> paymentProcessing;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PartnerFinancialData {
        private String id;
        private String name;
        private String location;
        private double totalCommissions;
        private double transactionVolume;
        private double averageOrderValue;
        private double profitMargin;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommissionData {
        private String name;
        private double amount;
        private double percentage;
        private String trend; // "up" | "down" | "stable"
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentProcessingData {
        private String method;
        private double volume;
        private double fees;
        private double successRate;
    }

    // --- Transactions ---
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Transactions {
        private List<TransactionVolumeData> volumeMetrics;
        private List<ProcessingCostData> processingCosts;
        private List<SuccessRateData> successRates;
        private List<PaymentMethodData> paymentMethods;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransactionVolumeData {
        private String name;
        private int volume;
        private double amount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProcessingCostData {
        private String name;
        private double cost;
        private double percentage;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SuccessRateData {
        private String name;
        private double successRate;
        private int failedTransactions;
        private int totalTransactions;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentMethodData {
        private String method;
        private double usage;
        private double revenue;
        private double averageAmount;
    }

    // --- Delivery ---
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Delivery {
        private List<DeliveryCostData> deliveryCosts;
        private List<CostPerDeliveryData> costPerDelivery;
        private List<DeliveryRevenueData> deliveryRevenue;
        private List<PartnerDeliveryData> partnerDeliveryEconomics;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeliveryCostData {
        private String name;
        private double totalCost;
        private int deliveryCount;
        private double averageCost;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CostPerDeliveryData {
        private String region;
        private double cost;
        private int volume;
        private double efficiency;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeliveryRevenueData {
        private String name;
        private double revenue;
        private double costs;
        private double profit;
        private double margin;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PartnerDeliveryData {
        private String partnerId;
        private String partnerName;
        private int deliveryVolume;
        private double deliveryRevenue;
        private double deliveryCosts;
        private double profitability;
    }

    // --- Operational Costs ---
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Operational {
        private List<OperationalCostData> infrastructureCosts;
        private List<PlatformCostData> platformCosts;
        private List<ProfitMarginData> profitMargins;
        private List<CostBreakdownData> costBreakdown;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OperationalCostData {
        private String name;
        private double infrastructure;
        private double personnel;
        private double marketing;
        private double other;
        private double total;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlatformCostData {
        private String category;
        private double amount;
        private double percentage;
        private String trend;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfitMarginData {
        private String name;
        private double revenue;
        private double costs;
        private double profit;
        private double margin;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CostBreakdownData {
        private String category;
        private double amount;
        private double percentage;
    }

    // --- Filters ---
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FilterOptions {
        private List<PartnerOption> partners;
        private List<RevenueStreamOption> revenueStreams;
        private List<CurrencyOption> currencies;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PartnerOption {
        private String id;
        private String name;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RevenueStreamOption {
        private String id;
        private String name;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CurrencyOption {
        private String code;
        private String name;
        private String symbol;
    }
}
