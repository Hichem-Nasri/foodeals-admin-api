package net.foodeals.product.domain.repositories;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.common.contracts.SlugRepository;
import net.foodeals.product.domain.entities.Product;
import net.foodeals.product.domain.enums.CreatedBy;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends BaseRepository<Product, UUID>, SlugRepository {

	Optional<Product> findByName(String name);

	Page<Product> findByCreatedBy(CreatedBy createdBy, Pageable pageable);

	Page<Product> findByCreatedBySubEntity_Id(UUID subEntityId, Pageable pageable);

	@Query("""
			  SELECT p FROM Product p
			  WHERE (:subEntityId IS NULL OR p.createdBySubEntity.id = :subEntityId)
			    AND (:createdBy IS NULL OR p.createdBy = :createdBy)
			""")
	Page<Product> search(@Param("subEntityId") UUID subEntityId, @Param("createdBy") CreatedBy 
			createdBy,
			Pageable pageable);
	
	boolean existsByBarcode(String barcode);
	Optional<Product> findByBarcode(String barcode);
	
	@Query("SELECT p FROM Product p WHERE p.deletedAt IS NOT NULL")
    public Page<Product> findDeletedProducts(Pageable pageable);
	
	@Query("""
		    SELECT p FROM Product p
		    WHERE (:name IS NULL OR p.name ILIKE CONCAT(:name, '%'))
		      AND (:brand IS NULL OR p.brand = :brand)
		      AND (:categoryId IS NULL OR p.category.id = :categoryId)
		      AND (:subCategoryId IS NULL OR p.subcategory.id = :subCategoryId)
		      AND (:barcode IS NULL OR p.barcode = :barcode)
		      AND (coalesce(:startDate, null) IS NULL OR p.createdAt >= :startDate)
		      AND (coalesce(:endDate, null) IS NULL OR p.createdAt >= :endDate)
		""")
		Page<Product> searchProducts(
		        @Param("name") String name,
		        @Param("brand") String brand,
		        @Param("categoryId") UUID categoryId,
		        @Param("subCategoryId") UUID subCategoryId,
		        @Param("barcode") String barcode,
		        @Param("startDate") Instant startDate,
		        @Param("endDate") Instant endDate,
		        Pageable pageable);


}

