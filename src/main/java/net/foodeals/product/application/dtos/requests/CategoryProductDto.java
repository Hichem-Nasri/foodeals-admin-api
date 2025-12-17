package net.foodeals.product.application.dtos.requests;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.foodeals.common.valueOjects.Price;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class CategoryProductDto {
	
	 private UUID id;
     private String name;
     private String slug;

}
