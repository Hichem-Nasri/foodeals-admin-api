package net.foodeals.statistics.application.service;

import net.foodeals.statistics.application.responses.ClientQueryParams;
import net.foodeals.statistics.application.responses.ClientResponse;

public interface ClientStatisticsService {
	 public ClientResponse getClientStatistics(ClientQueryParams filter);
}
