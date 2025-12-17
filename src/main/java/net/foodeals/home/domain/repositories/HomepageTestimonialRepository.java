package net.foodeals.home.domain.repositories;

import java.util.List;
import java.util.UUID;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.home.domain.entities.HomepageTestimonial;

public interface HomepageTestimonialRepository extends BaseRepository<HomepageTestimonial, UUID> {
	
	List<HomepageTestimonial> findAllByIsActiveTrueOrderByClassementAsc();
}