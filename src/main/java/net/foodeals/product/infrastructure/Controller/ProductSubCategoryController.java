package net.foodeals.product.infrastructure.Controller;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.foodeals.product.application.dtos.responses.ProductSubCategoryResponse;
import net.foodeals.product.application.services.ProductSubCategoryService;

@RestController
@RequestMapping("v1/product-subcategories")
@RequiredArgsConstructor
public class ProductSubCategoryController {

    private final ProductSubCategoryService service;
    private final ModelMapper mapper;

    @GetMapping
    public ResponseEntity<Page<ProductSubCategoryResponse>> getAll(Pageable pageable) {
        final Page<ProductSubCategoryResponse> response = service.findAll(pageable)
                .map(category -> mapper.map(category, ProductSubCategoryResponse.class));
        return ResponseEntity.ok(response);
    }
}
