package net.foodeals.home.application.services;

import java.util.List;

import net.foodeals.home.application.dtos.HomeSortingDto;

public interface HomeSortingService {
    List<HomeSortingDto> getSorting(String country, String state, String city);
    void saveSorting(List<HomeSortingDto> sortings, String country, String state, String city);
}
