package net.foodeals.user.application.dtos.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoleBasePermissionsDto {
    private String role;
    private String label;
    private List<String> basePermissions;
}