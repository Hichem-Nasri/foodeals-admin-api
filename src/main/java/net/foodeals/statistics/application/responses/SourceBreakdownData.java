package net.foodeals.statistics.application.responses;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SourceBreakdownData {
    private String source;
    private int count;
    private double cost;
    private double conversionRate;
}
