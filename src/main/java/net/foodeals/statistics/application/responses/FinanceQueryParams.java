package net.foodeals.statistics.application.responses;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinanceQueryParams {
    private String startDate; // format YYYY-MM-DD
    private String endDate;
    private String currency; // MAD, EUR, USD
    private String partner;  // Partner ID or 'all'
    private String revenueStream; // subscription | commission | delivery | all
}