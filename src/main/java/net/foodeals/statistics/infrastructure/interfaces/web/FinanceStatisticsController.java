package net.foodeals.statistics.infrastructure.interfaces.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.foodeals.statistics.application.responses.FinanceQueryParams;
import net.foodeals.statistics.application.responses.FinanceResponse;
import net.foodeals.statistics.application.service.FinanceStatisticsService;

@RestController
@RequestMapping("/v1/statistique/finance")
@RequiredArgsConstructor
public class FinanceStatisticsController {

    private final FinanceStatisticsService financeService;

    @GetMapping
    public FinanceResponse getFinanceStats(@ModelAttribute FinanceQueryParams filter) {
        return financeService.getFinanceStatistics(filter);
    }}