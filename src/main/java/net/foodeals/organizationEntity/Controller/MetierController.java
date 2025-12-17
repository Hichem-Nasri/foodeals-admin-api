package net.foodeals.organizationEntity.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
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

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import net.foodeals.organizationEntity.application.dtos.requests.ActivityDTO;
import net.foodeals.organizationEntity.application.dtos.requests.MetierClassementRequest;
import net.foodeals.organizationEntity.application.services.ActivityService;
import net.foodeals.organizationEntity.domain.entities.Activity;
import net.foodeals.product.application.services.ProductCategoryService;
import net.foodeals.product.domain.entities.ProductCategory;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/metiers")
public class MetierController {

	private final ActivityService activityService;
	private final ProductCategoryService categoryService ;

	@PostMapping
	public ResponseEntity<Activity> create(@RequestBody ActivityDTO dto) {
		Activity a = activityService.create(dto.getName(), dto.getObservation());
		return ResponseEntity.status(HttpStatus.CREATED).body(a);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Activity> updateClassement(@PathVariable UUID id,@RequestBody ActivityDTO dto) {
		
		return ResponseEntity.ok(activityService.updateActivity(id, dto));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Activity>get(@PathVariable UUID id) {
		
		return ResponseEntity.ok(activityService.findById(id));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable UUID id,@RequestParam String reason,
			@RequestParam String motif) {
		activityService.archiveActivity(id, reason, motif);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/delete-categories/{id}")
	public ResponseEntity<Void> deleteCategoriesFromMetier(@PathVariable UUID id,@RequestBody List<UUID>idCategories) {
		
		List<ProductCategory>categories =new ArrayList<>();
		for(UUID idCat :idCategories) {
			categories.add(categoryService.findById(idCat));
		}
		
		activityService.deleteCategoriesFromMetier(categories, id);
		return ResponseEntity.noContent().build();
	}
	
	 
    @GetMapping
    @Transactional
    public ResponseEntity<Map<String, Object>> list(Pageable pageable) {
        Page<Activity> page = activityService.getAllActive(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("data", page.getContent());
        response.put("total", page.getTotalElements());
        response.put("page", page.getNumber() + 1);
        response.put("limit", page.getSize());

        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/sort")
    public ResponseEntity<Void> sortMetiers(@RequestBody MetierClassementRequest request) {
        activityService.sortMetiers(request.getMetiers());
        return ResponseEntity.noContent().build();
    }

}
