package net.foodeals.home.domain.repositories;


import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.home.domain.entities.HomeSorting;
import net.foodeals.location.domain.entities.City;
import net.foodeals.location.domain.entities.Country;
import net.foodeals.location.domain.entities.State;

import java.util.List;
import java.util.UUID;

public interface HomeSortingRepository extends BaseRepository<HomeSorting, UUID> {
    List<HomeSorting> findByCountryAndStateAndCity(Country country, State state, City city);
}
