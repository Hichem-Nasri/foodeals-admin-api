package net.foodeals.statistics.domain.repositories;

import net.foodeals.statistics.application.responses.PartnerQueryParams;
import net.foodeals.statistics.application.responses.PartnerResponse;

public interface PartnerStatisticsRepository {
	
	PartnerResponse getPartnerStatistics(PartnerQueryParams filter);

}
