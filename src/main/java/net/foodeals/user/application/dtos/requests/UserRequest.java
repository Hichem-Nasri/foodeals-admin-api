package net.foodeals.user.application.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import net.foodeals.user.domain.valueObjects.Name;
import org.springframework.lang.Nullable;

import java.util.UUID;

public record UserRequest(
        @NotNull Name name,
        @NotBlank @Email String email,
        @NotBlank String phone,
        @NotBlank String password,
        Boolean isEmailVerified,
        @NotNull String roleName,
        @Nullable UUID organizationEntityId
) {
}
