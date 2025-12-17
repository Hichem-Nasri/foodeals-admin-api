package net.foodeals.statistics.application.service.impl;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.foodeals.statistics.application.responses.FinanceQueryParams;
import net.foodeals.statistics.application.responses.FinanceResponse;
import net.foodeals.statistics.application.service.FinanceStatisticsService;
import net.foodeals.statistics.domain.repositories.FinanceStatisticsRepository;

@Service
@RequiredArgsConstructor
public class FinanceStatisticsServiceImpl implements FinanceStatisticsService {

    private final FinanceStatisticsRepository repository;

    @Override
    public FinanceResponse getFinanceStatistics(FinanceQueryParams filter) {
        // Convert dates
        LocalDate start = filter.getStartDate() != null ? LocalDate.parse(filter.getStartDate()) : LocalDate.now().minusMonths(6);
        LocalDate end = filter.getEndDate() != null ? LocalDate.parse(filter.getEndDate()) : LocalDate.now();

        // Get raw numbers
        long totalRevenue = (long)repository.getTotalRevenue(start, end, filter);
        long mrr = repository.getMonthlyRecurringRevenue(start, end, filter);
        long transactionFees = repository.getTransactionFees(start, end, filter);
        long commissions = repository.getPartnerCommissions(start, end, filter);
        long costs = repository.getOperationalCosts(start, end, filter);
        long profit = totalRevenue - costs;

        // Return DTO
        return FinanceResponse.builder()
            .summary(FinanceResponse.Summary.builder()
                .totalRevenue(buildCard("Revenu total", totalRevenue, "euro", "bg-green-100 text-green-800"))
                .monthlyRecurringRevenue(buildCard("MRR", mrr, "refresh-cw", "bg-blue-100 text-blue-800"))
                .totalTransactionFees(buildCard("Frais", transactionFees, "credit-card", "bg-red-100 text-red-800"))
                .partnerCommissions(buildCard("Commissions", commissions, "percent", "bg-purple-100 text-purple-800"))
                .operationalCosts(buildCard("Coûts", costs, "activity", "bg-orange-100 text-orange-800"))
                .netProfit(buildCard("Profit net", profit, "trending-up", "bg-teal-100 text-teal-800"))
                .build()
            )
            .build();
    }

    private FinanceResponse.SummaryCard buildCard(String title, long value, String icon, String cssClass) {
        return FinanceResponse.SummaryCard.builder()
            .title(title)
            .value(value)
            .iconType(icon)
            .className(cssClass)
            .currency(true)
            .build();
    }
}
