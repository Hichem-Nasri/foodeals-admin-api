package net.foodeals.organizationEntity.application.services;

import net.foodeals.common.contracts.CrudService;
import net.foodeals.organizationEntity.application.dtos.requests.ActivityDTO;
import net.foodeals.organizationEntity.application.dtos.requests.ActivityRequest;
import net.foodeals.organizationEntity.application.dtos.requests.MetierClassementDTO;
import net.foodeals.organizationEntity.domain.entities.Activity;
import net.foodeals.product.domain.entities.ProductCategory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ActivityService extends CrudService<Activity, UUID, ActivityRequest> {

    Set<Activity> getActivitiesByName(List<String> activitiesNames);

    Activity save(Activity activity);

    Activity getActivityByName(String name);

    List<Activity> saveAll(Set<Activity> activities);

    Page<Activity> findAllByTypes(List<String> types, Pageable pageable);
    
    Activity create(String name, String observation);
    
    Activity updateClassement(UUID id, int newClassement);
    
    Activity updateActivity(UUID id, ActivityDTO activityDTO);
    
    void archiveActivity(UUID id, String reason ,String motif);
    
    Page<Activity> getAllActive(Pageable pageable);
    
    void sortMetiers(List<MetierClassementDTO> classementList);
    
    void deleteCategoriesFromMetier(List<ProductCategory>categories,UUID id );
}
