package net.foodeals.statistics.application.responses;

import lombok.*;

@Getter
@Setter
public class ClientQueryParams {
    private String startDate;            // Format ISO: "YYYY-MM-DD"
    private String endDate;
    private String region;               // ex: "casa", "rabat", "all"
    private String userType;             // "new", "returning", "vip", "all"
    private String ageGroup;             // "18-24", "25-34", ..., "all"
    private String acquisitionSource;    // "organic", "paid", "referral", "social", "all"
}