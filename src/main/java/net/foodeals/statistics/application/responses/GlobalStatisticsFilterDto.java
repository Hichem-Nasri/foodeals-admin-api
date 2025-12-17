package net.foodeals.statistics.application.responses;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Setter
@Getter
public class GlobalStatisticsFilterDto {

	private LocalDate startDate;
	private LocalDate endDate;

	private String solutionType; // "all", "DLC", "MARKET", "DONATE"
	private String partner; // partner name or "all"
	private String region; // region name or "all"
}
