package net.foodeals.home.application.services.impl;


import lombok.RequiredArgsConstructor;
import net.foodeals.home.application.dtos.HomeSortingDto;
import net.foodeals.home.application.services.HomeSortingService;
import net.foodeals.home.domain.entities.HomeSorting;
import net.foodeals.home.domain.repositories.HomeSortingRepository;
import net.foodeals.location.domain.entities.City;
import net.foodeals.location.domain.entities.Country;
import net.foodeals.location.domain.entities.State;
import net.foodeals.location.domain.repositories.CityRepository;
import net.foodeals.location.domain.repositories.CountryRepository;
import net.foodeals.location.domain.repositories.StateRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeSortingServiceImpl implements HomeSortingService {

    private final HomeSortingRepository sortingRepository;
    private final CountryRepository countryRepo;
    private final StateRepository stateRepo;
    private final CityRepository cityRepo;

    @Override
    public List<HomeSortingDto> getSorting(String country, String state, String city) {
        Country c = country != null ? countryRepo.findByName(country) : null;
        State s = state != null ? stateRepo.findByName(state) : null;
        City ci = city != null ? cityRepo.findByName(city) : null;

        return sortingRepository.findByCountryAndStateAndCity(c, s, ci)
                .stream()
                .map(h -> new HomeSortingDto(h.getId(), h.getName(), h.getOrderClass()))
                .collect(Collectors.toList());
    }

    @Override
    public void saveSorting(List<HomeSortingDto> sortings, String country, String state, String city) {
        Country c = country != null ? countryRepo.findByName(country) : null;
        State s = state != null ? stateRepo.findByName(state) : null;
        City ci = city != null ? cityRepo.findByName(city) : null;

        sortingRepository.deleteAll(sortingRepository.findByCountryAndStateAndCity(c, s, ci));

        List<HomeSorting> newSortings = sortings.stream().map(dto -> {
            HomeSorting entity = new HomeSorting();
            entity.setName(dto.getName());
            entity.setOrderClass(dto.getOrderClass());
            entity.setRank(dto.getOrderClass());
            entity.setCountry(c);
            entity.setState(s);
            entity.setCity(ci);
            return entity;
        }).collect(Collectors.toList());

        sortingRepository.saveAll(newSortings);
    }
}
