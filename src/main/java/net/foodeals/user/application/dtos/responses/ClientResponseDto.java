package net.foodeals.user.application.dtos.responses;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponseDto {
	
	private Integer id;
	private LocalDateTime lastOrderDate;
	private String ref ;
	private String avatar;
	private String client;
	private LocalDate inscription;
	private Double reduction;
	private String sources;
	private String country;
	private String city;
	private String address;
	private String email;
	private String phone;
	private List<OrdersClientResponseDto> commandes;
	private List<OrderItemDto> itemDtos;

}
