package net.foodeals.home.application.dtos;

import java.util.List;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BestSellerSaveRequest {
    private String sortedBy;
    private List<BestSellerDto> personalized;
}
