package net.foodeals.location.application.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.location.application.dtos.responses.CityHierarchyDto;
import net.foodeals.location.application.dtos.responses.CityResponse;
import net.foodeals.location.application.dtos.responses.CountryHierarchyDto;
import net.foodeals.location.application.dtos.responses.CountryResponse;
import net.foodeals.location.application.dtos.responses.LocationHierarchyResponse;
import net.foodeals.location.application.dtos.responses.LocationStatsDto;
import net.foodeals.location.application.dtos.responses.RegionDto;
import net.foodeals.location.application.dtos.responses.StateHierarchyDto;
import net.foodeals.location.domain.repositories.CityRepository;
import net.foodeals.location.domain.repositories.CountryRepository;
import net.foodeals.location.domain.repositories.RegionRepository;
import net.foodeals.location.domain.repositories.StateRepository;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final CountryRepository countryRepo;
    private final RegionRepository regionRepo;
    private final StateRepository stateRepo;
    private final CityRepository cityRepo;
    // private final RegionRepository regionRepo; // si tu veux compter les régions

    @Transactional
    public LocationHierarchyResponse getFullHierarchyWithStats() {
        // hiérarchie (ton code existant, inchangé)
        var countries = countryRepo.findAll();
        var hierarchy = countries.stream().map(country -> {
            var states = country.getStates().stream().map(state -> {
                var cities = state.getCities().stream().map(city -> {
                    List<RegionDto> regions = city.getRegions() != null
                        ? city.getRegions().stream()
                              .map(r -> new RegionDto(r.getId(), r.getName()))
                              .toList()
                        : List.of();
                    return new CityHierarchyDto(city.getId(), city.getName(), regions);
                }).toList();
                return new StateHierarchyDto(state.getId(), state.getName(), cities);
            }).toList();
            return new CountryHierarchyDto(country.getId(), country.getName(), hierarchySafe(states));
        }).toList();

        // stats rapides via COUNT(*)
        long totalCountries = countryRepo.count();
        long totalStates    = stateRepo.count();
        long totalCities    = cityRepo.count();
        long totalRegions   = regionRepo.count();

        LocationStatsDto stats = new LocationStatsDto(totalCountries, totalStates, totalCities , totalRegions);
        return new LocationHierarchyResponse(hierarchy, stats);
    }

    // petit helper si tu veux garantir non-null
    private static List<StateHierarchyDto> hierarchySafe(List<StateHierarchyDto> v) { return v == null ? List.of() : v; }

    public Page<CityResponse> getCities(Pageable pageable) {
        return cityRepo.findAll(pageable)
                .map(city -> new CityResponse(city.getId(),city.getName()));
    }

    public Page<CountryResponse> getCountries(Pageable pageable) {
        return countryRepo.findAll(pageable)
                .map(country -> new CountryResponse(country.getId(), country.getName()));
    }
}

