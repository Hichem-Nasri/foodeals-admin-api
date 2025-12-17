package net.foodeals.user.application.services;

import net.foodeals.common.contracts.CrudService;
import net.foodeals.common.dto.request.UpdateReason;
import net.foodeals.common.dto.response.UpdateDetails;
import net.foodeals.location.domain.entities.City;
import net.foodeals.location.domain.entities.Region;
import net.foodeals.organizationEntity.domain.entities.enums.EntityType;
import net.foodeals.user.application.dtos.requests.UserFilter;
import net.foodeals.user.application.dtos.requests.UserRequest;
import net.foodeals.user.application.dtos.responses.*;
import net.foodeals.user.domain.entities.Role;
import net.foodeals.user.domain.entities.User;
import net.foodeals.user.domain.entities.WorkingHours;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService extends CrudService<User, Integer, UserRequest> {

    User findByEmail(String email);

    User save(User manager);
    
    Page<User> findByRoleName(String roleName,Pageable page);

    Page<User> getClientsData(Pageable page);

    Page<User> filterUsersOrganization(UserSearchOrganizationFilter filter, Pageable pageable);

    ClientDto toClientDto(User user);

    Long countDeliveryUsersByOrganizationId(UUID id);

    Page<User> getOrganizationUsers(Pageable pageable, UUID id);

    User createOrganizationEntityUser(UserRequest userRequest);

    Integer countByRole(Role role);

    void delete(Integer id, UpdateReason reason);
    
    CollaboratorStatisticsDTO getCollaboratorStatistics();
    
    Page<CollaboratorResponseDTO> getCollaborators(Pageable pageable);
    
    Page<CollaboratorResponseDTO> getArchivedCollaborators(Pageable pageable);

    UserProfileDTO mapUserToUserProfileDTO(User user);

    List<WorkingHoursDTO> mapWorkingHoursToDTO(List<WorkingHours> workingHours);

    Page<User> getSellsManagers(String name, Pageable pageable);

    Page<Object> getUsersByOrganization(UUID organizationId, UserFilter filter, Pageable pageable);

    Page<UserInfoDto> getUsersBySubEntity(UUID subEntityId, UserFilter filter, Pageable pageable);

    void deleteUser(Integer id, UpdateReason request);

    Page<UpdateDetails> getDeletionDetails(Integer uuid, Pageable pageable);

    Page<City> findCitiesUsersByEntityTypeAndCityName(UUID organizationId, String cityName,  Pageable pageable);

    Page<City> findCitiesUsersBySubentityAndCityName(UUID subentityId, String cityName, Pageable pageable);

    Page<Region> findRegionsUsersByEntityTypeAndRegionName(UUID organizationId, String regionName, Pageable pageable);

    Page<Region> findRegionsUsersBySubentityAndRegionName(UUID subEntityId, String regionName, Pageable pageable);

    Page<User> filterUsersSubentity(UserSearchSubentityFilters filter, Pageable pageable);
    
    ClientPageResponseDto getClients(Pageable pageable);
    
    ClientResponseDto getClient(Integer id);
    
    void archiveClient(Integer id,String reason,String motif);
    
    void archiveCollaborator(Integer id,String reason,String motif);
    
    CollaboratorDetailsResponseDto getCollaboratorDetails(Integer id);
    
    CollaboratorDetailsResponseDto createCollaborator(CollaboratorAddDto dto,List<String>internalDocuments,List<String>personalDocuments);
    
    CollaboratorDetailsResponseDto updateCollaborator(Integer id, CollaboratorAddDto dto,List<String>internalDocuments,List<String>personalDocuments) ;
    
    AbsenceDto createAbsence(Integer userId, Integer validateBy,AbsenceDto dto);
    
    List<AbsenceDto> getAbsences(Integer userId) ;
    
    String saveFile(MultipartFile file);
   User getConnectedUser();

	OrderTrackingResponseDto getOrderTrackingDetails(String clientId, String commandId);

	DriverLocationResponseDto getDriverLocation(Integer clientId, UUID commandId);
}
