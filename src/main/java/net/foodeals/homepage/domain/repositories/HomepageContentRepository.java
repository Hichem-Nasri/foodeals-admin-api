package net.foodeals.homepage.domain.repositories;

import net.foodeals.homepage.domain.entities.HomepageContentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface HomepageContentRepository extends JpaRepository<HomepageContentEntity, UUID> {
    Optional<HomepageContentEntity> findFirstByCountryAndStateAndCity(String country, String state, String city);

    Optional<HomepageContentEntity> findFirstByCountryIsNullAndStateIsNullAndCityIsNull();
}
