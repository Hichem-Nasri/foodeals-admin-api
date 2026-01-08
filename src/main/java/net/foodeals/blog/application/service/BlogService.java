package net.foodeals.blog.application.service;

import net.foodeals.blog.application.dtos.BlogResponse;
import net.foodeals.blog.application.dtos.CreateBlogRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.UUID;

public interface BlogService {

    Page<BlogResponse> getBlogs(Pageable pageable);

    Page<BlogResponse> filterBlogs(
            String title,
            UUID categoryId,
            Boolean published,
            Pageable pageable
    );

    Page<BlogResponse> getArchivedBlogs(Pageable pageable);

    BlogResponse createBlog(CreateBlogRequest request);
}

