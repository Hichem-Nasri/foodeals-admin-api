package net.foodeals.blog.application.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class BlogCategoryResponse {

    private UUID id;
    private String name;
}
