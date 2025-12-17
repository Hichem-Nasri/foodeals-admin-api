package net.foodeals.statistics.application.service.impl;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.foodeals.statistics.application.responses.PartnerQueryParams;
import net.foodeals.statistics.application.responses.PartnerResponse;
import net.foodeals.statistics.application.service.PartnerStatisticsService;
import net.foodeals.statistics.domain.repositories.PartnerStatisticsRepository;

@Service
@RequiredArgsConstructor
public class PartnerStatisticsServiceImpl implements PartnerStatisticsService {

	private final PartnerStatisticsRepository partnerRepo;

    @Override
    public PartnerResponse getPartnerStatistics(PartnerQueryParams filter) {
     
        if (filter.getStartDate() == null) {
            filter.setStartDate(LocalDate.now().minusMonths(6).toString());
        }
        if (filter.getEndDate() == null) {
            filter.setEndDate(LocalDate.now().toString());
        }

        
        return partnerRepo.getPartnerStatistics(filter);
    }
    

    
}

