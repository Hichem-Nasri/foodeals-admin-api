package net.foodeals.blog.application.service;

import net.foodeals.blog.application.dtos.BlogCategoryResponse;
import net.foodeals.blog.application.dtos.CreateBlogCategoryRequest;

import java.util.List;

public interface BlogCategoryService {

    List<BlogCategoryResponse> getAll();

    BlogCategoryResponse create(CreateBlogCategoryRequest request);
}