package net.foodeals.blog.application.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class BlogResponse {

    private UUID id;
    private String title;
    private String author;
    private UUID categoryId;
    private String categoryName;
    private boolean published;
    private LocalDateTime createdAt;
}

