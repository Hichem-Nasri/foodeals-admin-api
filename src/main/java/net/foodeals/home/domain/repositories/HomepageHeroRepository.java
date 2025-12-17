package net.foodeals.home.domain.repositories;

import java.util.Optional;
import java.util.UUID;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.home.domain.entities.HomepageHero;

public interface HomepageHeroRepository extends BaseRepository<HomepageHero,UUID> {
	
	 Optional<HomepageHero> findTopByOrderByUpdatedAtDesc();
}