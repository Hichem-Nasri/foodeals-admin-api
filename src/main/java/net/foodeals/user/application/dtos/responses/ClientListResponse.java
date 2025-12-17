package net.foodeals.user.application.dtos.responses;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.foodeals.order.application.dtos.responses.ProductInOrderDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientListResponse {
	
	  private Integer id;
	    private String fullName;
	    private String email;
	    private String phone;
	    private String source; // OrderSource
	    private long totalOrders;

}
