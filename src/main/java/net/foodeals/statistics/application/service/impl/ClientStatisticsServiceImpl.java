package net.foodeals.statistics.application.service.impl;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import net.foodeals.statistics.application.responses.ClientQueryParams;
import net.foodeals.statistics.application.responses.ClientResponse;
import net.foodeals.statistics.application.service.ClientStatisticsService;
import net.foodeals.statistics.domain.repositories.ClientStatisticsRepository;

@AllArgsConstructor
@Service
public class ClientStatisticsServiceImpl implements ClientStatisticsService {
	
	
	private final ClientStatisticsRepository clientRepo;

    public ClientResponse getClientStatistics(ClientQueryParams filter) {
        return clientRepo.getClientStatistics(filter);
    }

}
