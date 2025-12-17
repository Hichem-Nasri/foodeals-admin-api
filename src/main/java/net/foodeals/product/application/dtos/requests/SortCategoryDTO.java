package net.foodeals.product.application.dtos.requests;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SortCategoryDTO {

	 private UUID id;
	    private Integer classement;
}
