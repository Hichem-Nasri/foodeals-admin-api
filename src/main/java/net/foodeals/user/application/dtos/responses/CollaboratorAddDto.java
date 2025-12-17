package net.foodeals.user.application.dtos.responses;



import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.foodeals.user.domain.entities.enums.Gender;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollaboratorAddDto {

   
    private String avatar;
    private Gender civility;

    private String firstName;
    private String lastName;
    private String nationality;
    private String cin;

    private String role;
    private Integer responsible;
    private String department;

    private String phone;
    private String email;

    private String country;
    private String state ;
    private String region ; 
    private String city;
    private String address;

    private Double grossSalary;
    private Double netSalary;
    private String contractType;

    private List<WorkingHoursDTO> workingHours;
}

