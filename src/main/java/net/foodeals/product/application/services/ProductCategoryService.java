package net.foodeals.product.application.services;

import net.foodeals.common.contracts.CrudService;
import net.foodeals.product.application.dtos.requests.CategoryDTO;
import net.foodeals.product.application.dtos.requests.ProductCategoryRequest;
import net.foodeals.product.application.dtos.requests.SortCategoryDTO;
import net.foodeals.product.application.dtos.requests.UpdateCategoryDTO;
import net.foodeals.product.domain.entities.ProductCategory;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductCategoryService extends CrudService<ProductCategory, UUID, ProductCategoryRequest> {
  
	ProductCategory createCategory(CategoryDTO dto);
	
	ProductCategory updateCategory(UUID id, UpdateCategoryDTO dto);
	
	Page<ProductCategory> listAll(List<UUID> metierIds, Pageable pageable);
	
	void sort(List<SortCategoryDTO> sortList);
	
	Map<String, Object> getCategoryStats();
}
