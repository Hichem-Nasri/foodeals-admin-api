package net.foodeals.order.application.dtos.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderListResponseDto {
    private long totalSeenOrders;
    private long totalNotSeenOrders;
    private long totalDeliveredOrders;
    private long totalNotDeliveredOrders;
    private List<OrderSummaryDto> orders;
    private int currentPage;
    private int totalPages;
    private long totalItems;
    
    
}