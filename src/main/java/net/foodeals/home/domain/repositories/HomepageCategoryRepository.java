package net.foodeals.home.domain.repositories;

import java.util.List;
import java.util.UUID;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.home.domain.entities.HomepageCategory;

public interface HomepageCategoryRepository extends BaseRepository<HomepageCategory, UUID> {

	  List<HomepageCategory> findAllByIsActiveTrueOrderByClassementAsc();
}
