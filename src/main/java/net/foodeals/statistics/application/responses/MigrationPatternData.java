package net.foodeals.statistics.application.responses;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MigrationPatternData {
    private String from;
    private String to;
    private int count;
    private String timeframe;
}