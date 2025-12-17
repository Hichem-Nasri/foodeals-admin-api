package net.foodeals.statistics.domain.repositories;

import java.util.List;

import net.foodeals.statistics.application.responses.GlobalStatisticsFilterDto;
import net.foodeals.statistics.application.responses.GlobalStatisticsResponseDto.FilterOptions;
import net.foodeals.statistics.application.responses.GlobalStatisticsResponseDto.PartnerPerformance;
import net.foodeals.statistics.application.responses.GlobalStatisticsResponseDto.Summary;
import net.foodeals.statistics.application.responses.GlobalStatisticsResponseDto.TimeSeriesDataPoint;
import net.foodeals.statistics.application.responses.GlobalStatisticsResponseDto.UserEngagement;


public interface StatisticsRepository {
	
	Summary fetchSummaryData(GlobalStatisticsFilterDto filter);
	
	List<TimeSeriesDataPoint> fetchWasteReductionData(GlobalStatisticsFilterDto filter);

	List<TimeSeriesDataPoint> fetchPartnerEngagementData(GlobalStatisticsFilterDto filter);
	
	List<TimeSeriesDataPoint> fetchSolutionComparison(GlobalStatisticsFilterDto filter);
	
	PartnerPerformance fetchPartnerPerformance(GlobalStatisticsFilterDto filter);
	
	UserEngagement fetchUserEngagement(GlobalStatisticsFilterDto filter);
	
	FilterOptions fetchFilterOptions(GlobalStatisticsFilterDto filter);

}
