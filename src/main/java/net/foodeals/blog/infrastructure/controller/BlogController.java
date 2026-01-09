package net.foodeals.blog.infrastructure.controller;


import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.foodeals.blog.application.dtos.BlogResponse;
import net.foodeals.blog.application.dtos.CreateBlogRequest;
import net.foodeals.blog.application.service.BlogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/blogs")
@RequiredArgsConstructor
@Transactional
public class BlogController {

    private final BlogService blogService;

    @GetMapping
    public Page<BlogResponse> getBlogs(Pageable pageable) {
        return blogService.getBlogs(pageable);
    }

    @GetMapping("/filter")
    public Page<BlogResponse> filterBlogs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) Boolean published,
            Pageable pageable
    ) {
        return blogService.filterBlogs(title, categoryId, published, pageable);
    }

    @GetMapping("/archived")
    public Page<BlogResponse> getArchivedBlogs(Pageable pageable) {
        return blogService.getArchivedBlogs(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BlogResponse createBlog(@Valid @RequestBody CreateBlogRequest request) {
        return blogService.createBlog(request);
    }
}

