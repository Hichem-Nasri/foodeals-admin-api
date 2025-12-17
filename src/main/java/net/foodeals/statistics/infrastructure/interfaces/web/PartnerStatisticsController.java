package net.foodeals.statistics.infrastructure.interfaces.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.foodeals.statistics.application.responses.PartnerQueryParams;
import net.foodeals.statistics.application.responses.PartnerResponse;
import net.foodeals.statistics.application.service.PartnerStatisticsService;

@RestController
@RequestMapping("/v1/statistique/partners")
@RequiredArgsConstructor
public class PartnerStatisticsController {

    private final PartnerStatisticsService partnerStatisticsService;

    @GetMapping
    public PartnerResponse getPartnerStats(@ModelAttribute PartnerQueryParams filter) {
        return partnerStatisticsService.getPartnerStatistics(filter);
    }
}