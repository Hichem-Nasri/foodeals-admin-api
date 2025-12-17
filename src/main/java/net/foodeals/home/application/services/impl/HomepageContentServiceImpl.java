package net.foodeals.home.application.services.impl;

import net.foodeals.home.application.dtos.FeaturedDealDto;
import net.foodeals.home.application.dtos.HomepageAnnouncementDto;
import net.foodeals.home.application.dtos.HomepageCategoryDto;
import net.foodeals.home.application.dtos.HomepageHeroDto;
import net.foodeals.home.application.dtos.HomepageTestimonialDto;
import net.foodeals.home.application.services.HomepageContentService;
import net.foodeals.home.domain.entities.HomePageFeaturedDeal;
import net.foodeals.home.domain.entities.HomepageAnnouncement;
import net.foodeals.home.domain.entities.HomepageHero;
import net.foodeals.home.domain.entities.HomepageTestimonial;
import net.foodeals.home.domain.repositories.HomePageFeaturedDealRepository;
import net.foodeals.home.domain.repositories.HomepageAnnouncementRepository;
import net.foodeals.home.domain.repositories.HomepageCategoryRepository;
import net.foodeals.home.domain.repositories.HomepageHeroRepository;
import net.foodeals.home.domain.repositories.HomepageTestimonialRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional
public class HomepageContentServiceImpl implements HomepageContentService {

	private final HomepageHeroRepository heroRepository;
	private final HomePageFeaturedDealRepository featuredDealRepository;
	private final HomepageCategoryRepository categoryRepository;
	private final HomepageTestimonialRepository testimonialRepository;
	private final HomepageAnnouncementRepository announcementRepository;

	@Value("${upload.directory}")
	private String uploadDir;

	@Override
	public HomepageHeroDto getHero() {
		return heroRepository.findTopByOrderByUpdatedAtDesc().map(this::toDto).orElse(null);
	}

	@Override
	public HomepageHeroDto updateHero(HomepageHeroDto dto) {
		HomepageHero hero = heroRepository.findTopByOrderByUpdatedAtDesc().orElse(new HomepageHero());

		hero.setTitle(dto.getTitle());
		hero.setSubtitle(dto.getSubtitle());
		hero.setBackgroundImage(dto.getBackgroundImage());
		hero.setCtaText(dto.getCtaText());
		hero.setCtaLink(dto.getCtaLink());
		hero.setActive(dto.isActive());
		hero.setUpdatedAt(Instant.now());

		return toDto(heroRepository.save(hero));
	}

	@Override
	public List<FeaturedDealDto> getFeaturedDeals() {
		return featuredDealRepository.findAllByIsActiveTrueOrderByClassementAsc().stream().map(this::toDto)
				.collect(Collectors.toList());
	}

	@Override

	public void updateFeaturedDeals(List<UUID> dealIds, List<Integer> order) {
		IntStream.range(0, dealIds.size()).forEach(i -> {
			UUID dealId = dealIds.get(i);
			Integer sortOrder = order.get(i);

			featuredDealRepository.findById(dealId).ifPresent(deal -> {
				deal.setClassement(sortOrder);
				featuredDealRepository.save(deal);
			});
		});
	}

	@Override
	public List<HomepageCategoryDto> getCategories() {
		return categoryRepository.findAllByIsActiveTrueOrderByClassementAsc().stream().map(category -> {
			HomepageCategoryDto dto = new HomepageCategoryDto();
			dto.setId(category.getId());
			dto.setName(category.getName());
			dto.setIcon(category.getIcon());
			dto.setActive(category.isActive());
			dto.setOrder(category.getClassement());
			dto.setDealCount(category.getDealCount());
			return dto;
		}).sorted(Comparator.comparingInt(HomepageCategoryDto::getOrder)).collect(Collectors.toList());
	}

	@Override
	public void updateCategoriesOrder(List<HomepageCategoryDto> orderedCategories) {
		for (HomepageCategoryDto dto : orderedCategories) {
			categoryRepository.findById(dto.getId()).ifPresent(category -> {
				category.setClassement(dto.getOrder());
				categoryRepository.save(category);
			});
		}
	}

	@Override
	public List<HomepageTestimonialDto> getTestimonials() {
		return testimonialRepository.findAllByIsActiveTrueOrderByClassementAsc().stream().map(this::toDto)
				.collect(Collectors.toList());
	}

	@Override
	public HomepageTestimonialDto createOrUpdateTestimonial(HomepageTestimonialDto dto) {
		HomepageTestimonial testimonial = dto.getId() != null
				? testimonialRepository.findById(dto.getId()).orElse(new HomepageTestimonial())
				: new HomepageTestimonial();

		testimonial.setCustomerName(dto.getCustomerName());
		testimonial.setRating(dto.getRating());
		testimonial.setComment(dto.getComment());
		testimonial.setAvatar(dto.getAvatar());
		testimonial.setActive(dto.isActive());
		testimonial.setClassement(dto.getOrder());

		return toDto(testimonialRepository.save(testimonial));
	}

	@Override
	public void deleteTestimonial(UUID id) {
		testimonialRepository.deleteById(id);
	}

	@Override
	public List<HomepageAnnouncementDto> getAnnouncements() {
		return announcementRepository.findAllByIsActiveTrueAndExpiresAtAfterOrderByCreatedAtDesc(Instant.now())
				.stream().map(this::toDto).collect(Collectors.toList());
	}

	@Override
	public HomepageAnnouncementDto createOrUpdateAnnouncement(HomepageAnnouncementDto dto) {
		HomepageAnnouncement announcement = dto.getId() != null
				? announcementRepository.findById(dto.getId()).orElse(new HomepageAnnouncement())
				: new HomepageAnnouncement();

		announcement.setTitle(dto.getTitle());
		announcement.setMessage(dto.getMessage());
		announcement.setType(dto.getType());
		announcement.setActive(dto.isActive());
		announcement.setExpiresAt(dto.getExpiresAt());

		return toDto(announcementRepository.save(announcement));
	}

	@Override
	public void deleteAnnouncement(UUID id) {
		announcementRepository.deleteById(id);
	}

	// ==== MAPPING METHODS ====

	private HomepageHeroDto toDto(HomepageHero hero) {
		return HomepageHeroDto.builder().id(hero.getId()).title(hero.getTitle()).subtitle(hero.getSubtitle())
				.backgroundImage(hero.getBackgroundImage()).ctaText(hero.getCtaText()).ctaLink(hero.getCtaLink())
				.isActive(hero.isActive()).updatedAt(hero.getUpdatedAt()).build();
	}

	private FeaturedDealDto toDto(HomePageFeaturedDeal deal) {
		return FeaturedDealDto.builder().id(deal.getId()).title(deal.getTitle()).description(deal.getDescription())
				.image(deal.getImage()).originalPrice(deal.getOriginalPrice())
				.discountedPrice(deal.getDiscountedPrice()).restaurant(deal.getRestaurant())
				.restaurantId(deal.getRestaurantId()).isActive(deal.isActive()).order(deal.getClassement())
				.createdAt(deal.getCreatedAt()).build();
	}

	private HomepageTestimonialDto toDto(HomepageTestimonial testimonial) {
		return HomepageTestimonialDto.builder().id(testimonial.getId()).customerName(testimonial.getCustomerName())
				.rating(testimonial.getRating()).comment(testimonial.getComment()).avatar(testimonial.getAvatar())
				.isActive(testimonial.isActive()).order(testimonial.getClassement())
				.createdAt(testimonial.getCreatedAt()).build();
	}

	private HomepageAnnouncementDto toDto(HomepageAnnouncement announcement) {
		return HomepageAnnouncementDto.builder().id(announcement.getId()).title(announcement.getTitle())
				.message(announcement.getMessage()).type(announcement.getType()).isActive(announcement.isActive())
				.expiresAt(announcement.getExpiresAt()).createdAt(announcement.getCreatedAt()).build();
	}

	@Override
	public String saveImage(MultipartFile file) {
		try {

			File directory = new File(this.uploadDir);
			if (!directory.exists()) {
				directory.mkdirs(); // Crée les dossiers si nécessaire
			}

			String originalFilename = file.getOriginalFilename();
			String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
			String newFilename = UUID.randomUUID() + extension;

			Path filePath = Paths.get(uploadDir, newFilename);
			Files.write(filePath, file.getBytes());

			// Retourne un chemin accessible depuis le front (à adapter selon config
			// serveur)
			return this.uploadDir + "/" + newFilename;

		} catch (IOException e) {
			throw new RuntimeException("Erreur lors de l’enregistrement de l’image", e);
		}
	}

}
