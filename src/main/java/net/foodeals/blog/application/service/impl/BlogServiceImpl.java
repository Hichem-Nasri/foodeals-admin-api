package net.foodeals.blog.application.service.impl;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import net.foodeals.blog.application.dtos.BlogResponse;
import net.foodeals.blog.application.dtos.CreateBlogRequest;
import net.foodeals.blog.application.service.BlogService;
import net.foodeals.blog.application.specifications.BlogSpecification;
import net.foodeals.blog.domain.entity.Blog;
import net.foodeals.blog.domain.entity.BlogCategory;
import net.foodeals.blog.domain.repository.BlogCategoryRepository;
import net.foodeals.blog.domain.repository.BlogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final BlogCategoryRepository categoryRepository;


    @Override
    public Page<BlogResponse> getBlogs(Pageable pageable) {
        return blogRepository.findByDeletedAtIsNull(pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<BlogResponse> filterBlogs(
            String title,
            UUID categoryId,
            Boolean published,
            Pageable pageable
    ) {
        Specification<Blog> spec =
                BlogSpecification.filter(title, categoryId, published);

        return blogRepository.findAll(spec, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<BlogResponse> getArchivedBlogs(Pageable pageable) {
        return blogRepository.findByDeletedAtIsNotNull(pageable)
                .map(this::mapToResponse);
    }

    @Override
    public BlogResponse createBlog(CreateBlogRequest request) {

        BlogCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Blog category not found"));

        Blog blog = Blog.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(request.getAuthor())
                .category(category)
                .published(request.isPublished())
                .build();

        return mapToResponse(blogRepository.save(blog));
    }

    private BlogResponse mapToResponse(Blog blog) {
        return BlogResponse.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .author(blog.getAuthor())
                .categoryId(blog.getCategory().getId())
                .categoryName(blog.getCategory().getName())
                .published(blog.isPublished())
                .createdAt(null)
                .build();
    }
}

