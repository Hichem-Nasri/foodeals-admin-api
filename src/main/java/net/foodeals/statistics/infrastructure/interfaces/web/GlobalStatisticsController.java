package net.foodeals.statistics.infrastructure.interfaces.web;


import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import net.foodeals.statistics.application.responses.GlobalStatisticsFilterDto;
import net.foodeals.statistics.application.responses.GlobalStatisticsResponseDto;
import net.foodeals.statistics.application.service.GlobalStatisticsService;

@RestController
@RequestMapping("/v1/statistique")
@RequiredArgsConstructor
public class GlobalStatisticsController {

    private final GlobalStatisticsService statisticsService;

    @GetMapping("/dashboard")
    public GlobalStatisticsResponseDto getDashboard(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false, defaultValue = "all") String solutionType,
            @RequestParam(required = false, defaultValue = "all") String partner,
            @RequestParam(required = false, defaultValue = "all") String region
    ) {
        GlobalStatisticsFilterDto filter = new GlobalStatisticsFilterDto();
        filter.setStartDate(startDate != null ? startDate : LocalDate.now().minusMonths(6));
        filter.setEndDate(endDate != null ? endDate : LocalDate.now());
        filter.setSolutionType(solutionType);
        filter.setPartner(partner);
        filter.setRegion(region);

        return statisticsService.getDashboard(filter);
    }
}

