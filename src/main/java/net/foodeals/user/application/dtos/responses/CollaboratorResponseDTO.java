package net.foodeals.user.application.dtos.responses;

import java.time.LocalDate;

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
public class CollaboratorResponseDTO {

	private Integer id;
    private String ref;
    private LocalDate hireDate;
    private String fullName;
    private String nationality;
    private String jobTitle;
    private String phone;
    private String email;
    private String country;
    private String city;
    private Double grossSalary;
    private Double netSalary;
    private Integer totalAbsence;
    private Integer totalLeave;
}
