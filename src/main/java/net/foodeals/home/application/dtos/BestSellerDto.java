package net.foodeals.home.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class BestSellerDto {
    private String name;
    private String image;
    private int totalSells;
    private int completedOrders;
    private float rating;
}
