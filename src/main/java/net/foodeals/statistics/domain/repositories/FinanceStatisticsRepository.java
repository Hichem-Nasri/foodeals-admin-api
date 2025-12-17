package net.foodeals.statistics.domain.repositories;

import java.time.LocalDate;

import net.foodeals.statistics.application.responses.FinanceQueryParams;

public interface FinanceStatisticsRepository {
    double getTotalRevenue(LocalDate start, LocalDate end, FinanceQueryParams filter);
    long getMonthlyRecurringRevenue(LocalDate start, LocalDate end, FinanceQueryParams filter);
    long getTransactionFees(LocalDate start, LocalDate end, FinanceQueryParams filter);
    long getPartnerCommissions(LocalDate start, LocalDate end, FinanceQueryParams filter);
    long getOperationalCosts(LocalDate start, LocalDate end, FinanceQueryParams filter);
}
