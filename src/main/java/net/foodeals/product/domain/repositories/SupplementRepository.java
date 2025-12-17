package net.foodeals.product.domain.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.product.domain.entities.Supplement;
import net.foodeals.product.domain.enums.CreatedBy;
import net.foodeals.product.domain.enums.SupplementType;

public interface SupplementRepository extends BaseRepository<Supplement, UUID> {
	
	@Query("SELECT s FROM Supplement s WHERE (:createdBy IS NULL OR s.createdBy = :createdBy) AND s.type = :type")
	Page<Supplement> findByCreatedByAndType(
	    @Param("createdBy") CreatedBy createdBy,
	    @Param("type") SupplementType type,
	    Pageable pageable
	);
	
	@Query("SELECT s FROM Supplement s WHERE s.deletedAt IS NOT NULL AND s.type = :type")
	Page<Supplement> findArchivedByType(@Param("type") SupplementType type, Pageable pageable);
	
	long countByTypeAndDeletedAtIsNull(SupplementType type);
	
	long countByTypeAndDeletedAtIsNotNull(SupplementType type);

}
