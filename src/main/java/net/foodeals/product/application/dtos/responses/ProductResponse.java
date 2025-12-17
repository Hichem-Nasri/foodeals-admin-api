package net.foodeals.product.application.dtos.responses;

import net.foodeals.common.valueOjects.Price;
import net.foodeals.payment.application.dto.request.PriceDto;
import net.foodeals.product.domain.enums.ProductType;

import java.util.UUID;

public record ProductResponse(
	    UUID id,
	    String name,
	    String slug,
	    String description,
	    String title,
	    String barcode,
	    String brand,
	    ProductType type,
	    PriceDto price,
	    String productImagePath,
	    // infos minimales de relation
	    UUID categoryId, String categoryName,
	    UUID subcategoryId, String subcategoryName,
	    UUID rayonId, String rayonName
	) {}