package net.foodeals.product.application.dtos.responses;


import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductSubCategoryResponse {
        private UUID id;
        private  String name;
        private String  slug;

}
