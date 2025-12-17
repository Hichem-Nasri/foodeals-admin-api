package net.foodeals.statistics.application.responses;

import lombok.*;

@Builder
public class Option {
	
	
   
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeSeriesDataPoint {
        private String name;
        private long value;
       
        
    }
	
	@Getter
	@Setter
	
	@AllArgsConstructor
	@NoArgsConstructor
	public class RegionOption {
	    private String id;
	    private String name;
	}

	@Getter
	@Setter
	
	@AllArgsConstructor
	@NoArgsConstructor
	public class PartnerTypeOption {
	    private String id;
	    private String name;
	}

	@Getter
	@Setter
	
	@AllArgsConstructor
	@NoArgsConstructor
	public class SolutionTypeOption {
	    private String id;
	    private String name;
	}

	@Getter
	@Setter
	
	@AllArgsConstructor
	@NoArgsConstructor
	public class StatusOption {
	    private String id;
	    private String name;
	}

}