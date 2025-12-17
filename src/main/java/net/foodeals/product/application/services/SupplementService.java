package net.foodeals.product.application.services;

import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityNotFoundException;
import net.foodeals.product.application.dtos.responses.SupplementPageResponse;
import net.foodeals.product.domain.entities.Supplement;
import net.foodeals.product.domain.enums.CreatedBy;
import net.foodeals.product.domain.enums.SupplementType;

public interface SupplementService {
	
    public Supplement create(Supplement supplement);

    public Supplement update(UUID id, Supplement updated) ;

    public void delete(UUID id,String reasion,String motif) ;

    public Map<SupplementType, Page<Supplement>> listByTypeAndCreatedBy(
            CreatedBy createdBy,
            Pageable saucePageable,
            Pageable boissonPageable) ;

    public Supplement getById(UUID id);
    
    
    public Map<String, SupplementPageResponse> listArchivedByType(
    	    Pageable saucePageable,
    	    Pageable boissonPageable
    	);

}
