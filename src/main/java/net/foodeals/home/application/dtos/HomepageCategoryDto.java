package net.foodeals.home.application.dtos;

import java.util.UUID;

import lombok.*;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomepageCategoryDto {
    private UUID id;
    private String name;
    private String icon;
    private boolean isActive;
    private Integer order;
    private Integer dealCount;
}

