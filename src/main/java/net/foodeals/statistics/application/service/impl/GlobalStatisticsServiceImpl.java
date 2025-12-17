package net.foodeals.statistics.application.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.foodeals.statistics.application.responses.GlobalStatisticsFilterDto;
import net.foodeals.statistics.application.responses.GlobalStatisticsResponseDto;
import net.foodeals.statistics.application.responses.GlobalStatisticsResponseDto.FilterOptions;
import net.foodeals.statistics.application.responses.GlobalStatisticsResponseDto.PartnerPerformance;
import net.foodeals.statistics.application.responses.GlobalStatisticsResponseDto.Summary;
import net.foodeals.statistics.application.responses.GlobalStatisticsResponseDto.TimeSeries;
import net.foodeals.statistics.application.responses.GlobalStatisticsResponseDto.TimeSeriesDataPoint;
import net.foodeals.statistics.application.responses.GlobalStatisticsResponseDto.UserEngagement;
import net.foodeals.statistics.application.service.GlobalStatisticsService;
import net.foodeals.statistics.domain.repositories.StatisticsRepository;

@Service
@RequiredArgsConstructor
public class GlobalStatisticsServiceImpl implements GlobalStatisticsService {

    private final StatisticsRepository repository;

    @Override
    public GlobalStatisticsResponseDto getDashboard(GlobalStatisticsFilterDto filter) {
        Summary summary = repository.fetchSummaryData(filter);
        List<TimeSeriesDataPoint> wasteReduction = repository.fetchWasteReductionData(filter);
        List<TimeSeriesDataPoint> partnerEngagement = repository.fetchPartnerEngagementData(filter);

        // Implémenter ou compléter ces méthodes dans le repository
        List<TimeSeriesDataPoint> solutionComparison = repository.fetchSolutionComparison(filter);
        PartnerPerformance partnerPerformance = repository.fetchPartnerPerformance(filter);
        UserEngagement userEngagement = repository.fetchUserEngagement(filter);
        FilterOptions filterOptions = repository.fetchFilterOptions(filter);

        return GlobalStatisticsResponseDto.builder()
                .summary(summary)
                .timeSeries(GlobalStatisticsResponseDto.TimeSeries.builder()
                        .wasteReduction(wasteReduction)
                        .partnerEngagement(partnerEngagement)
                        .solutionComparison(solutionComparison)
                        .build())
                .partnerPerformance(partnerPerformance)
                .userEngagement(userEngagement)
                .filterOptions(filterOptions)
                .build();
    }

}


