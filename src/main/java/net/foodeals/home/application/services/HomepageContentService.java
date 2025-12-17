package net.foodeals.home.application.services;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import net.foodeals.home.application.dtos.FeaturedDealDto;
import net.foodeals.home.application.dtos.HomepageAnnouncementDto;
import net.foodeals.home.application.dtos.HomepageCategoryDto;
import net.foodeals.home.application.dtos.HomepageHeroDto;
import net.foodeals.home.application.dtos.HomepageTestimonialDto;


public interface HomepageContentService {

    HomepageHeroDto getHero();
    HomepageHeroDto updateHero(HomepageHeroDto dto);

    List<FeaturedDealDto> getFeaturedDeals();
    void updateFeaturedDeals(List<UUID> dealIds, List<Integer> order);

    List<HomepageCategoryDto> getCategories();
    void updateCategoriesOrder(List<HomepageCategoryDto> orderedCategories);

    List<HomepageTestimonialDto> getTestimonials();
    HomepageTestimonialDto createOrUpdateTestimonial(HomepageTestimonialDto dto);
    void deleteTestimonial(UUID id);

    List<HomepageAnnouncementDto> getAnnouncements();
    HomepageAnnouncementDto createOrUpdateAnnouncement(HomepageAnnouncementDto dto);
    void deleteAnnouncement(UUID id);
    
    public String saveImage(MultipartFile file);
}
