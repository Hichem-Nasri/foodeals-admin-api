package net.foodeals.blog.infrastructure.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.foodeals.blog.application.dtos.BlogCategoryResponse;
import net.foodeals.blog.application.dtos.CreateBlogCategoryRequest;
import net.foodeals.blog.application.service.BlogCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/blog-categories")
@RequiredArgsConstructor
public class BlogCategoryController {

    private final BlogCategoryService service;

    @GetMapping
    public List<BlogCategoryResponse> listCategories() {
        return service.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BlogCategoryResponse createCategory(
            @Valid @RequestBody CreateBlogCategoryRequest request
    ) {
        return service.create(request);
    }
}
