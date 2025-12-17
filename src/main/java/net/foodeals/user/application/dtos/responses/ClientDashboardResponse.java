package net.foodeals.user.application.dtos.responses;

import java.time.Instant;
import java.util.List;
import java.util.Map;
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
public class ClientDashboardResponse {
	
	 private List<ClientListResponse> clients;
	 private long totalClients;
	 private Map<String, Long> countBySource;

}
