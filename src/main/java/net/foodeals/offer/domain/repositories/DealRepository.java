package net.foodeals.offer.domain.repositories;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.offer.domain.entities.Deal;

import java.util.UUID;

import org.springframework.data.jpa.repository.Query;

public interface DealRepository extends BaseRepository<Deal, UUID> {
	@Query("SELECT d FROM Deal d WHERE d.offer.id = :offerId")
	public Deal getDealByOfferId(UUID offerId);
}
