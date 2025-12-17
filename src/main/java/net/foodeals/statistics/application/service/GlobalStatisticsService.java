package net.foodeals.statistics.application.service;

import net.foodeals.statistics.application.responses.GlobalStatisticsFilterDto;
import net.foodeals.statistics.application.responses.GlobalStatisticsResponseDto;

public interface GlobalStatisticsService {
	GlobalStatisticsResponseDto getDashboard(GlobalStatisticsFilterDto filter);
}

