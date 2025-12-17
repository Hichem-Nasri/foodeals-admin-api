package net.foodeals.statistics.application.responses;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpansionOpportunityData {
    private String city;
    private String region;
    private double opportunityScore;
    private int estimatedPartners;
    private double marketSize;
    private String competitionLevel;
}

