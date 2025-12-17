package net.foodeals.organizationEntity.application.services.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.foodeals.organizationEntity.application.dtos.requests.ActivityDTO;
import net.foodeals.organizationEntity.application.dtos.requests.ActivityRequest;
import net.foodeals.organizationEntity.application.dtos.requests.MetierClassementDTO;
import net.foodeals.organizationEntity.application.services.ActivityService;
import net.foodeals.organizationEntity.domain.entities.Activity;
import net.foodeals.organizationEntity.domain.exceptions.ActivityNotFoundException;
import net.foodeals.organizationEntity.domain.repositories.ActivityRepository;
import net.foodeals.processors.classes.DtoProcessor;
import net.foodeals.product.domain.entities.ProductCategory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor

public class ActivityServiceImpl implements ActivityService {

	private final ActivityRepository repository;
	private final DtoProcessor dtoProcessor;

	@Override
	@Transactional
	public List<Activity> findAll() {
		return this.repository.findAll();
	}

	@Override
	@Transactional
	public Page<Activity> findAll(Integer pageNumber, Integer pageSize) {
		return null;
	}

	@Override
	@Transactional
	public Page<Activity> findAll(Pageable pageable) {
		return this.repository.findAll(pageable);
	}

	@Override
	@Transactional
	public Page<Activity> findAllByTypes(List<String> types, Pageable pageable) {
		return this.repository.findByTypeIn(types, pageable); // Assuming you have this method in your repository
	}

	@Override
	@Transactional
	public Activity findById(UUID id) {
		return repository.findById(id).orElseThrow(() -> new ActivityNotFoundException(id));
	}

	@Override
	@Transactional
	public Activity create(ActivityRequest dto) {
		Activity activity = Activity.builder().name(dto.name().toLowerCase()).type(dto.type()).build();
		return this.repository.save(activity);
	}

	@Override
	@Transactional
	public Activity update(UUID id, ActivityRequest dto) {
		Activity activity = repository.findById(id).orElseThrow(() -> new ActivityNotFoundException(id));

		activity.setName(dto.name().toLowerCase());
		activity.setType(dto.type());
		return this.repository.save(activity);
	}

	@Override
	@Transactional
	public void delete(UUID id) {
		Activity activity = repository.findById(id).orElseThrow(() -> new ActivityNotFoundException(id));

		this.repository.softDelete(activity.getId());
	}

	@Override
	@Transactional
	public Set<Activity> getActivitiesByName(List<String> activitiesNames) {
		return this.repository
				.findByNameIn(activitiesNames.stream().map(String::toLowerCase).collect(Collectors.toList()));
	}

	@Override
	@Transactional
	public Activity getActivityByName(String name) {
		return this.repository.findByName(name.toLowerCase());
	}

	@Override
	@Transactional
	public List<Activity> saveAll(Set<Activity> activities) {
		return this.repository.saveAll(activities);
	}

	@Override
	@Transactional
	public Activity save(Activity activity) {
		return this.repository.save(activity);
	}

	@Override
	public Activity create(String name, String observation) {
		int maxClassement = repository.findMaxClassement().orElse(0);
		Activity activity = new Activity();
		activity.setName(name);
		activity.setObservation(observation);
		activity.setClassement(maxClassement + 1);
		return repository.save(activity);
	}

	@Override
	public Activity updateClassement(UUID id, int newClassement) {

		Activity current = repository.findById(id).orElseThrow(() -> new RuntimeException("Activity not found"));

		List<Activity> all = repository.findAll(Sort.by("classement"));
		all.remove(current);

		all.add(newClassement - 1, current);

		// Réindexer
		for (int i = 0; i < all.size(); i++) {
			all.get(i).setClassement(i + 1);
		}

		return repository.saveAll(all).stream().filter(a -> a.getId().equals(id)).findFirst().orElseThrow();
	}

	@Override
	public void archiveActivity(UUID id, String reason, String motif) {

		Activity current = repository.findById(id).orElseThrow(() -> new RuntimeException("Activity not found"));
		current.setMotif(motif);
		current.setReason(reason);
		current.setDeletedAt(Instant.now());

	}

	@Override
	public Page<Activity> getAllActive(Pageable pageable) {
		return repository.findByDeletedAtIsNull(pageable);
	}

	@Override
	public Activity updateActivity(UUID id, ActivityDTO dto) {
		Activity activity = repository.findById(id).orElseThrow(() -> new ActivityNotFoundException(id));

		activity.setName(dto.getName());
		activity.setObservation(dto.getObservation());
		activity.setClassement(dto.getClassement());
		return this.repository.save(activity);
	}

	public void sortMetiers(List<MetierClassementDTO> classementList) {
		// Charger tous les métiers concernés
		List<UUID> ids = classementList.stream().map(MetierClassementDTO::getId).toList();

		List<Activity> activities = repository.findAllById(ids);

		// Appliquer le nouveau classement
		Map<UUID, Integer> classementMap = classementList.stream()
				.collect(Collectors.toMap(MetierClassementDTO::getId, MetierClassementDTO::getClassement));

		for (Activity activity : activities) {
			Integer newClassement = classementMap.get(activity.getId());
			if (newClassement != null) {
				activity.setClassement(newClassement);
			}
		}

		repository.saveAll(activities);
	}

	@Override
	public void deleteCategoriesFromMetier(List<ProductCategory> categories, UUID id) {
		Activity activity = repository.findById(id)
	            .orElseThrow(() -> new EntityNotFoundException("Activity not found"));

	    // Supprimer uniquement les catégories présentes
	    activity.getCategories().removeAll(categories);

	    // Sauvegarde (comme c’est une relation @ManyToMany, Hibernate va mettre à jour la table de jointure)
	    repository.save(activity);
		
	}
}
