package net.foodeals.product.application.dtos.requests;



import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import net.foodeals.payment.application.dto.request.PriceDto;
import net.foodeals.product.domain.enums.ProductType;

public record ProductCreateRequest(
	    @NotBlank String name,
	    String slug,                    // facultatif : généré si null
	    String description,
	    String title,
	    String barcode,
	    String brand,
	    ProductType type,
	    String productImagePath,        // correspond à ProductImagePath côté entity
	    @NotNull UUID categoryId,
	    UUID subcategoryId,
	    UUID rayonId
	) {}
