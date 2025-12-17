package net.foodeals.user.application.dtos.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientPageResponseDto {
	private ClientStatsDto stats;
    private List<ClientResponseDto> clients;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;


}
