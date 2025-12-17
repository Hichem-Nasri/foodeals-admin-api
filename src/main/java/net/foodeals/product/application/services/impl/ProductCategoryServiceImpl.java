package net.foodeals.product.application.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.organizationEntity.application.services.ActivityService;
import net.foodeals.organizationEntity.domain.entities.Activity;
import net.foodeals.organizationEntity.domain.repositories.ActivityRepository;
import net.foodeals.product.application.dtos.requests.CategoryDTO;
import net.foodeals.product.application.dtos.requests.ProductCategoryRequest;
import net.foodeals.product.application.dtos.requests.SortCategoryDTO;
import net.foodeals.product.application.dtos.requests.UpdateCategoryDTO;
import net.foodeals.product.application.services.ProductCategoryService;
import net.foodeals.product.domain.entities.ProductCategory;
import net.foodeals.product.domain.exceptions.ProductCategoryNotFoundException;
import net.foodeals.product.domain.repositories.ProductCategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static net.foodeals.common.Utils.SlugUtil.makeUniqueSlug;
import static net.foodeals.common.Utils.SlugUtil.toSlug;

@Service
@Transactional
@RequiredArgsConstructor
class ProductCategoryServiceImpl implements ProductCategoryService {

    private final ProductCategoryRepository repository;
    private final ActivityService activityService;
    private final ActivityRepository activityRepository;

    @Override
    public List<ProductCategory> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<ProductCategory> findAll(Integer pageNumber, Integer pageSize) {
        return repository.findAll(PageRequest.of(pageNumber, pageSize));
    }

    @Override
    public Page<ProductCategory> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public ProductCategory findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductCategoryNotFoundException(id));
    }

    @Override
    public ProductCategory create(ProductCategoryRequest request) {
        final Activity activity = activityService.findById(request.activityId());

        final String slug = makeUniqueSlug(toSlug(request.name()), repository);
        final ProductCategory productCategory = ProductCategory.create(request.name(), slug, activity);

        return repository.save(productCategory);
    }

    @Override
    public ProductCategory update(UUID id, ProductCategoryRequest request) {
        final ProductCategory productCategory = findById(id);
        final Activity activity = activityService.findById(request.activityId());

        productCategory
                .setName(request.name())
                .setSlug(makeUniqueSlug(toSlug(request.name()), repository))
                .setActivity(activity);

        return repository.save(productCategory);
    }

    @Override
    public void delete(UUID id) {
        if (!repository.existsById(id))
            throw new ProductCategoryNotFoundException(id);

        repository.softDelete(id);
    }
    
    
    public Page<ProductCategory> listAll(List<UUID> metierIds, Pageable pageable) {
        if (metierIds == null || metierIds.isEmpty()) {
            return repository.findAll(pageable);
        }
        return repository.findByActivityIds(metierIds, pageable);
    }

    public ProductCategory getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public ProductCategory createCategory(CategoryDTO dto) {
        int nextClassement = repository.findMaxClassement().orElse(0) + 1;

        Activity activity = getFirstActivity(dto.getMetierIds());

        ProductCategory category = new ProductCategory();
        category.setName(dto.getName());
        category.setSlug((dto.getSlug() != null && !dto.getSlug().isEmpty())
                ? dto.getSlug() : slugify(dto.getName()));
        category.setObservation(dto.getObservation());
        category.setImageUrl(dto.getImageUrl());
        category.setClassement(nextClassement);
        category.setActivity(activity);

        return repository.save(category);
    }

    public ProductCategory updateCategory(UUID id, UpdateCategoryDTO dto) {
        ProductCategory category = getById(id);

        category.setName(dto.getName());
        category.setSlug((dto.getSlug() != null && !dto.getSlug().isEmpty())
                ? dto.getSlug() : slugify(dto.getName()));
        category.setObservation(dto.getObservation());
        category.setImageUrl(dto.getImageUrl());
        category.setClassement(dto.getClassement());

        Activity activity = getFirstActivity(dto.getMetierIds());
        category.setActivity(activity);

        return repository.save(category);
    }

   
    public void sort(List<SortCategoryDTO> sortList) {
        List<ProductCategory> categories = repository.findAllById(
                sortList.stream().map(SortCategoryDTO::getId).toList()
        );

        Map<UUID, Integer> map = sortList.stream()
                .collect(Collectors.toMap(SortCategoryDTO::getId, SortCategoryDTO::getClassement));

        for (ProductCategory c : categories) {
            Integer newClassement = map.get(c.getId());
            if (newClassement != null) {
                c.setClassement(newClassement);
            }
        }

        repository.saveAll(categories);
    }

    private Activity getFirstActivity(List<UUID> ids) {
        if (ids == null || ids.isEmpty()) throw new RuntimeException("Metier ID required");
        return activityRepository.findById(ids.get(0))
                .orElseThrow(() -> new RuntimeException("Activity not found"));
    }

    private String slugify(String input) {
        return input.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("-$", "");
    }

	@Override
	public Map<String, Object> getCategoryStats() {
		List<ProductCategory> allCategories = repository.findAll();

	    long totalCategories = allCategories.size();
	    Set<UUID> uniqueMetiers = allCategories.stream()
	            .map(cat -> cat.getActivity().getId())
	            .collect(Collectors.toSet());

	    Map<UUID, Long> countPerMetier = allCategories.stream()
	            .collect(Collectors.groupingBy(
	                cat -> cat.getActivity().getId(),
	                Collectors.counting()
	            ));

	    return Map.of(
	        "totalMetiers", uniqueMetiers.size(),
	        "totalCategories", totalCategories,
	        "categoriesPerMetier", countPerMetier
	    );
	}
}
