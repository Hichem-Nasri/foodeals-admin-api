package net.foodeals.product.infrastructure.Controller;

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

import lombok.RequiredArgsConstructor;
import net.foodeals.product.application.dtos.requests.CategoryDTO;
import net.foodeals.product.application.dtos.requests.SortCategoryRequest;
import net.foodeals.product.application.dtos.requests.UpdateCategoryDTO;
import net.foodeals.product.application.services.ProductCategoryService;
import net.foodeals.product.domain.entities.ProductCategory;

@RestController
@RequestMapping("/v1/categories")
@RequiredArgsConstructor
public class ProductCategoryController {

    private final ProductCategoryService service;

    @GetMapping
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(required = false) List<UUID> metierIds,
            Pageable pageable
    ) {
        Page<ProductCategory> page = service.listAll(metierIds, pageable);
        Map<String, Object> res = new HashMap();
        res.put("data", page.getContent());
        res.put("total", page.getTotalElements());
        res.put("page", page.getNumber() + 1);
        res.put("limit", page.getSize());
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductCategory> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProductCategory> create(@RequestBody CategoryDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createCategory(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductCategory> update(@PathVariable UUID id, @RequestBody UpdateCategoryDTO dto) {
        return ResponseEntity.ok(service.updateCategory(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/sort")
    public ResponseEntity<Void> sort(@RequestBody SortCategoryRequest request) {
        service.sort(request.getCategories());
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/categories-overview")
    public ResponseEntity<Map<String, Object>> getOverview() {
        return ResponseEntity.ok(Map.of("data", service.getCategoryStats()));
    }
}

