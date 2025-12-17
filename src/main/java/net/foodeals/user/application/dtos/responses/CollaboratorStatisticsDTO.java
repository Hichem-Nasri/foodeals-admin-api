package net.foodeals.user.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class CollaboratorStatisticsDTO {
    private long totalCollaborators;
    private long totalPrestataires;
    private long totalRecrutement;
}