package net.foodeals.user.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientStatsDto {
    private Long totalUsers;
    private Long internalUsers;
    private Long externalUsers;
}