package net.foodeals.statistics.infrastructure.interfaces.web;

import lombok.RequiredArgsConstructor;
import net.foodeals.statistics.application.responses.ClientQueryParams;
import net.foodeals.statistics.application.responses.ClientResponse;
import net.foodeals.statistics.application.service.ClientStatisticsService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/statistique/clients")
@RequiredArgsConstructor
public class ClientStatisticsController {

    private final ClientStatisticsService clientStatisticsService;

    @GetMapping
    public ClientResponse getClientStatistics(@ModelAttribute ClientQueryParams filter) {
        return clientStatisticsService.getClientStatistics(filter);
    }
}
