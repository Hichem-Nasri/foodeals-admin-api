package net.foodeals.product.application.dtos.requests;

import java.util.UUID;

import net.foodeals.payment.application.dto.request.PriceDto;
import net.foodeals.product.domain.enums.ProductType;

public record ProductUpdateRequest(
	    String name,
	    String slug,
	    String description,
	    String title,
	    String barcode,
	    String brand,
	    ProductType type,
	    PriceDto price,
	    String productImagePath,
	    UUID categoryId,
	    UUID subcategoryId,
	    UUID rayonId
	) {}