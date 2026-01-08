package net.foodeals.blog.application.service.impl;

import lombok.RequiredArgsConstructor;
import net.foodeals.blog.application.dtos.BlogCategoryResponse;
import net.foodeals.blog.application.dtos.CreateBlogCategoryRequest;
import net.foodeals.blog.application.service.BlogCategoryService;
import net.foodeals.blog.domain.entity.BlogCategory;
import net.foodeals.blog.domain.repository.BlogCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogCategoryServiceImpl implements BlogCategoryService {

    private final BlogCategoryRepository repository;

    @Override
    public List<BlogCategoryResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(cat -> BlogCategoryResponse.builder()
                        .id(cat.getId())
                        .name(cat.getName())
                        .build())
                .toList();
    }

    @Override
    public BlogCategoryResponse create(CreateBlogCategoryRequest request) {

        if (repository.existsByNameIgnoreCase(request.getName())) {
            throw new IllegalArgumentException("Category name already exists");
        }

        BlogCategory category = BlogCategory.builder()
                .name(request.getName().trim())
                .build();

        return mapToResponse(repository.save(category));
    }

    private BlogCategoryResponse mapToResponse(BlogCategory cat) {
        return BlogCategoryResponse.builder()
                .id(cat.getId())
                .name(cat.getName())
                .build();
    }
}

