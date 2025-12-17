package net.foodeals.offer.domain.repositories;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.offer.domain.entities.Box;

import java.util.UUID;

import org.springframework.data.jpa.repository.Query;

public interface BoxRepository extends BaseRepository<Box, UUID> {
	
	@Query("SELECT b FROM Box b WHERE b.offer.id = :offerId")
	public Box getBoxByOfferId(UUID offerId);
}
