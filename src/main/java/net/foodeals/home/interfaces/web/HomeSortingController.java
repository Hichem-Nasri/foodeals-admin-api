package net.foodeals.home.interfaces.web;


import lombok.RequiredArgsConstructor;
import net.foodeals.home.application.dtos.HomeSortingDto;
import net.foodeals.home.application.services.HomeSortingService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/home-sorting")
@RequiredArgsConstructor
public class HomeSortingController {

    private final HomeSortingService homeSortingService;

    @GetMapping("/content")
    public ResponseEntity<?> getSorting(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String city
    ) {
        return ResponseEntity.ok(homeSortingService.getSorting(country, state, city));
    }

    @PostMapping
    public ResponseEntity<?> saveSorting(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String city,
            @RequestBody List<HomeSortingDto> sortings
    ) {
        homeSortingService.saveSorting(sortings, country, state, city);
        return ResponseEntity.ok().build();
    }
}
