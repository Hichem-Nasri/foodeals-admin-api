package net.foodeals.statistics.application.responses;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RevenueByPartnerData {
    private String partnerId;
    private String partnerName;
    private double totalRevenue;
    private double monthlyRevenue;
    private double growthRate;
    private double revenuePerOrder;
}
