package net.foodeals.user.application.dtos.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollaboratorDetailsResponseDto {
	private Integer id;
	private String avatar;
	private String civility; // Civilité (Mr, Mme...)
	private String firstName;
	private String lastName;
	private String nationality;
	private String cin;
	private String role;
	private String department;
	private String phone;
	private String email;
	private String country ;
	private String region;
	private String state ;
	private String city;
	private String address;
	private Double grossSalary;
	private Double netSalary;
	private String contractType;
	private CollaboratorRefDto responsible;

	private List<WorkingHoursDTO> workingHours;
	private List<DocumentDto> personalDocuments;
	private List<DocumentDto> internalDocuments;

}
