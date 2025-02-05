package net.foodeals.organizationEntity.application.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.foodeals.user.domain.valueObjects.Name;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactDto {
    @NotNull
    private Name name;

    @Email
    private String email;

    @NotBlank
    private String phone;
}
