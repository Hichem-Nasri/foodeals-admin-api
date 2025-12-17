package net.foodeals.statistics.domain.repositories;



import net.foodeals.statistics.application.responses.ClientQueryParams;
import net.foodeals.statistics.application.responses.ClientResponse;

public interface ClientStatisticsRepository {
    ClientResponse getClientStatistics(ClientQueryParams filter);
}
