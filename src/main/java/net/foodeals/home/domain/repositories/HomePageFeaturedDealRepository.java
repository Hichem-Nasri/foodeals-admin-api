package net.foodeals.home.domain.repositories;

import java.util.List;
import java.util.UUID;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.home.domain.entities.HomePageFeaturedDeal;

public interface HomePageFeaturedDealRepository extends BaseRepository<HomePageFeaturedDeal, UUID> {
	
	List<HomePageFeaturedDeal> findAllByIsActiveTrueOrderByClassementAsc();

}
