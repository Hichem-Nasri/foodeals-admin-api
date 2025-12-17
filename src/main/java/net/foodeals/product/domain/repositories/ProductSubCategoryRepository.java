package net.foodeals.product.domain.repositories;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.common.contracts.SlugRepository;
import net.foodeals.product.domain.entities.ProductCategory;
import net.foodeals.product.domain.entities.ProductSubCategory;
import net.foodeals.user.domain.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductSubCategoryRepository extends BaseRepository<ProductSubCategory, UUID>, SlugRepository {

	Optional<ProductSubCategory> findByNameAndCategory(String name, ProductCategory category);

}
