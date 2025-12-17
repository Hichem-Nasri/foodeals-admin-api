package net.foodeals.product.application.dtos.requests;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CategoryDTO {
	 private String name;
	    private String slug;
	    private String observation;
	    private String imageUrl;
	    private List<UUID> metierIds; 
}
