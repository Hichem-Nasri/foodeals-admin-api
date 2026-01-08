package net.foodeals.blog.application.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBlogCategoryRequest {

    @NotBlank
    private String name;
}