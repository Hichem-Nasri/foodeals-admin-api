package net.foodeals.location.infrastructure.interfaces.web;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.foodeals.location.application.dtos.responses.CityResponse;
import net.foodeals.location.application.dtos.responses.CountryHierarchyDto;
import net.foodeals.location.application.dtos.responses.CountryResponse;
import net.foodeals.location.application.dtos.responses.LocationHierarchyResponse;
import net.foodeals.location.application.services.LocationService;

@RestController
@RequestMapping("v1/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping("/hierarchy")
    public LocationHierarchyResponse getHierarchyWithStats() {
        return locationService.getFullHierarchyWithStats(); // la méthode fournie précédemment
    }
    
    @GetMapping("/cities")
    public Page<CityResponse> getCities(
           Pageable pageable) {
        return locationService.getCities(pageable);
    }

    @GetMapping("/countries")
    public Page<CountryResponse> getCountries(
            Pageable pageable) {
        return locationService.getCountries(pageable);
    }
}