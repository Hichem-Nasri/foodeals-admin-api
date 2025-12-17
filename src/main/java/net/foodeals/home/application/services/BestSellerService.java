package net.foodeals.home.application.services;

import net.foodeals.home.application.dtos.BestSellerDto;

import java.util.List;

public interface BestSellerService {
    List<BestSellerDto> getBestSellers(String country, String state, String city, String sortedBy);
    void savePersonalized(List<BestSellerDto> dtos, String country, String state, String city);
}
