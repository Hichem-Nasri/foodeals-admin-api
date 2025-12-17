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
public class ProductDto {

	private UUID id;
	private String name;
	private String title;
	private String imageUrl;
	private String description;
	private String barcode;
	private String type;
	private Instant creationDate;
	private Price price;

	private CategoryProductDto category;
	private CategoryProductDto subCategory;

	private String brand;
	private String rayon;

	private CreatedByDto createdBy;
}