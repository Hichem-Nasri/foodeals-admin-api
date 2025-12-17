package net.foodeals.statistics.application.service;

import net.foodeals.statistics.application.responses.FinanceQueryParams;
import net.foodeals.statistics.application.responses.FinanceResponse;

public interface FinanceStatisticsService {
    FinanceResponse getFinanceStatistics(FinanceQueryParams filter);
}