package net.foodeals.blog.application.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UpdatedBlogRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private UUID categoryId;

    private boolean published;
}
