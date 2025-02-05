package net.foodeals.user.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.foodeals.organizationEntity.domain.entities.enums.SubEntityType;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchSubentityFilters {
    private String query;
    private UUID organizationId;
}
