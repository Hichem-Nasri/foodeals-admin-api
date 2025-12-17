package net.foodeals.statistics.application.responses;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartnerQueryParams {
    private String startDate;    // format ISO yyyy-MM-dd
    private String endDate;
    private String region;
    private String solutionType;
    private String partnerType;
    private String status;
}