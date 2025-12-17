package net.foodeals.statistics.application.responses;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivationRateData {
    private String name;
    private int signups;
    private int activations;
    private double activationRate;
    private double averageActivationTime;
}
