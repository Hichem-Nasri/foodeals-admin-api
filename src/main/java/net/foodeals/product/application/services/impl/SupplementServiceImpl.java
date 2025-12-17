package net.foodeals.product.application.services.impl;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.websocket.server.ServerEndpoint;
import lombok.AllArgsConstructor;
import net.foodeals.product.application.dtos.responses.SupplementPageResponse;
import net.foodeals.product.application.services.SupplementService;
import net.foodeals.product.domain.entities.Supplement;
import net.foodeals.product.domain.enums.CreatedBy;
import net.foodeals.product.domain.enums.SupplementType;
import net.foodeals.product.domain.repositories.SupplementRepository;
import net.foodeals.user.domain.entities.User;

@AllArgsConstructor
@Service
public class SupplementServiceImpl implements SupplementService {
	
	private final SupplementRepository supplementRepository;

    public Supplement create(Supplement supplement) {
        return supplementRepository.save(supplement);
    }

    public Supplement update(UUID id, Supplement updated) {
        Supplement existing = supplementRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Supplement not found"));
        
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setPrice(updated.getPrice());
        existing.setSupplementImagePath(updated.getSupplementImagePath());
        return supplementRepository.save(existing);
    }

    public void delete(UUID id,String reason,String motif) {
    	Supplement supplement = supplementRepository.findById(id).orElse(null);
		if (supplement != null) {
            supplement.setReason(reason);
            supplement.setMotif(motif);
			supplement.setDeletedAt(Instant.now());
		supplementRepository.save(supplement);
		}
    }

    public Map<SupplementType, Page<Supplement>> listByTypeAndCreatedBy(
            CreatedBy createdBy,
            Pageable saucePageable,
            Pageable boissonPageable) {
        
        Page<Supplement> sauces = supplementRepository.findByCreatedByAndType(createdBy, SupplementType.Sauce, saucePageable);
        Page<Supplement> boissons = supplementRepository.findByCreatedByAndType(createdBy, SupplementType.Boisson, boissonPageable);

        Map<SupplementType, Page<Supplement>> result = new HashMap();
        result.put(SupplementType.Sauce, sauces);
        result.put(SupplementType.Boisson, boissons);

        return result;
    }

    public Supplement getById(UUID id) {
        return supplementRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Supplement not found"));
    }

	@Override
	public Map<String, SupplementPageResponse> listArchivedByType(
		    Pageable saucePageable,
		    Pageable boissonPageable
		) {
		    Page<Supplement> sauces = supplementRepository.findArchivedByType(SupplementType.Sauce, saucePageable);
		    Page<Supplement> boissons = supplementRepository.findArchivedByType(SupplementType.Boisson, boissonPageable);

		    long totalSauce = supplementRepository.countByTypeAndDeletedAtIsNotNull(SupplementType.Sauce);
		    long totalBoisson = supplementRepository.countByTypeAndDeletedAtIsNotNull(SupplementType.Boisson);
		    
		    Map<String, SupplementPageResponse> result = new HashMap<>();
		    result.put("saucesArchived", new SupplementPageResponse(sauces,totalSauce));
		    result.put("boissonsArchived", new SupplementPageResponse(boissons,totalBoisson));

		    return result;
		}

}
