package net.foodeals.statistics.application.service;

import net.foodeals.statistics.application.responses.PartnerQueryParams;
import net.foodeals.statistics.application.responses.PartnerResponse;

public interface PartnerStatisticsService {
	
	PartnerResponse getPartnerStatistics(PartnerQueryParams filter);

}
