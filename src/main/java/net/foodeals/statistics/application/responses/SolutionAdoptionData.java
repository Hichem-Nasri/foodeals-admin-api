package net.foodeals.statistics.application.responses;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SolutionAdoptionData {
    private String solutionType;
    private double adoptionRate;
    private int partnerCount;
    private double revenueContribution;
    private double growthRate;
}