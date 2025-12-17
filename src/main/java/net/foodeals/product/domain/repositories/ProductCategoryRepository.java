package net.foodeals.product.domain.repositories;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.common.contracts.SlugRepository;
import net.foodeals.product.domain.entities.ProductCategory;
import net.foodeals.product.domain.entities.ProductSubCategory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductCategoryRepository extends BaseRepository<ProductCategory, UUID>, SlugRepository {

	
	Optional<ProductCategory> findByName(String name);
	
	@Query("SELECT MAX(c.classement) FROM ProductCategory c")
	Optional<Integer> findMaxClassement();

	@Query("SELECT c FROM ProductCategory c WHERE c.activity.id IN :activityIds")
	Page<ProductCategory> findByActivityIds(@Param("activityIds") List<UUID> activityIds, Pageable pageable);
}
