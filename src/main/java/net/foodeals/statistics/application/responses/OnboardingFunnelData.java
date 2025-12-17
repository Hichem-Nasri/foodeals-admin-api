package net.foodeals.statistics.application.responses;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OnboardingFunnelData {
    private String step;
    private int completed;
    private double dropoffRate;
}
