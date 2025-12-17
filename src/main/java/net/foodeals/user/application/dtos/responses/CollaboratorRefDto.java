package net.foodeals.user.application.dtos.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.foodeals.user.domain.valueObjects.Name;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollaboratorRefDto {
	
	private Integer id ;
	
	private Name name ;

}
