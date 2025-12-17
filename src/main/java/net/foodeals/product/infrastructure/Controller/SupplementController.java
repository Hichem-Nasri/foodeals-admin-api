package net.foodeals.product.infrastructure.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.foodeals.product.application.dtos.responses.SupplementPageResponse;
import net.foodeals.product.application.services.SupplementService;
import net.foodeals.product.domain.entities.Supplement;
import net.foodeals.product.domain.enums.CreatedBy;
import net.foodeals.product.domain.enums.SupplementType;
import net.foodeals.product.domain.repositories.SupplementRepository;

@RestController
@RequestMapping("/v1/supplements")
@RequiredArgsConstructor
public class SupplementController {
    private final SupplementService supplementService;
    private final SupplementRepository supplementRepository;

    @PostMapping
    public ResponseEntity<Supplement> create(@RequestBody Supplement supplement) {
        return ResponseEntity.status(HttpStatus.CREATED).body(supplementService.create(supplement));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Supplement> update(@PathVariable UUID id, @RequestBody Supplement supplement) {
        return ResponseEntity.ok(supplementService.update(id, supplement));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, @RequestParam(name = "reason")String reason, 
    		 @RequestParam(name = "motif")String motif){
        supplementService.delete(id,reason ,motif);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Map<String, SupplementPageResponse>> list(
        @RequestParam(required = false) CreatedBy createdBy,
        @RequestParam(defaultValue = "0") int saucePage,
        @RequestParam(defaultValue = "10") int sauceSize,
        @RequestParam(defaultValue = "0") int boissonPage,
        @RequestParam(defaultValue = "10") int boissonSize
    ) {
        Pageable saucePageable = PageRequest.of(saucePage, sauceSize);
        Pageable boissonPageable = PageRequest.of(boissonPage, boissonSize);

        Map<SupplementType, Page<Supplement>> result = supplementService.listByTypeAndCreatedBy(
            createdBy, saucePageable, boissonPageable
        );

        Map<String, SupplementPageResponse> response = new HashMap<>();
        
        long totalSauce = supplementRepository.countByTypeAndDeletedAtIsNull(SupplementType.Sauce);
        long totalBoisson = supplementRepository.countByTypeAndDeletedAtIsNull( SupplementType.Boisson);
       
        response.put("sauces", new SupplementPageResponse(result.get(SupplementType.Sauce),totalSauce));
        response.put("boissons", new SupplementPageResponse(result.get(SupplementType.Boisson),totalBoisson));

        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/archived")
    public ResponseEntity<Map<String, SupplementPageResponse>> getArchivedSupplements(
        @RequestParam(defaultValue = "0") int saucePage,
        @RequestParam(defaultValue = "10") int sauceSize,
        @RequestParam(defaultValue = "0") int boissonPage,
        @RequestParam(defaultValue = "10") int boissonSize
    ) {
        Pageable saucePageable = PageRequest.of(saucePage, sauceSize);
        Pageable boissonPageable = PageRequest.of(boissonPage, boissonSize);

        
        Map<String, SupplementPageResponse> archived = supplementService.listArchivedByType(
            saucePageable, boissonPageable
        );

        return ResponseEntity.ok(archived);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Supplement> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(supplementService.getById(id));
    }
}
