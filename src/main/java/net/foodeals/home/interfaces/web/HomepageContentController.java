package net.foodeals.home.interfaces.web;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import net.foodeals.home.application.dtos.FeaturedDealsOrderRequest;
import net.foodeals.home.application.dtos.HomepageAnnouncementDto;
import net.foodeals.home.application.dtos.HomepageCategoryDto;
import net.foodeals.home.application.dtos.HomepageHeroDto;
import net.foodeals.home.application.dtos.HomepageTestimonialDto;
import net.foodeals.home.application.services.HomepageContentService;

@RestController
@RequestMapping("v1/homepage")
@RequiredArgsConstructor
public class HomepageContentController {

    private final HomepageContentService homepageContentService;

    @GetMapping("/content")
    public ResponseEntity<?> getHomepageContent() {
        return ResponseEntity.ok(Map.of(
                "hero", homepageContentService.getHero(),
                "featuredDeals", homepageContentService.getFeaturedDeals(),
                "categories", homepageContentService.getCategories(),
                "testimonials", homepageContentService.getTestimonials(),
                "announcements", homepageContentService.getAnnouncements()
        ));
    }

    @PutMapping("/hero")
    public ResponseEntity<?> updateHero(@RequestBody HomepageHeroDto request) {
        return ResponseEntity.ok(homepageContentService.updateHero(request));
    }

    @PutMapping("/featured-deals")
    public ResponseEntity<?> updateFeaturedDeals(@RequestBody FeaturedDealsOrderRequest request) {
        homepageContentService.updateFeaturedDeals(request.getDealIds(), request.getOrder());
        return ResponseEntity.ok(Map.of("message", "Featured deals updated successfully"));
    }

    @PutMapping("/categories/order")
    public ResponseEntity<?> updateCategoryOrder(@RequestBody List<HomepageCategoryDto> request) {
        homepageContentService.updateCategoriesOrder(request);
        return ResponseEntity.ok(Map.of("message", "Categories order updated successfully"));
    }

    @PostMapping("/testimonials")
    public ResponseEntity<?> createTestimonial(@RequestBody HomepageTestimonialDto request) {
        return ResponseEntity.ok(homepageContentService.createOrUpdateTestimonial(request));
    }

    @PutMapping("/testimonials/{id}")
    public ResponseEntity<?> updateTestimonial(@PathVariable UUID id, @RequestBody HomepageTestimonialDto request) {
        request.setId(id);
        return ResponseEntity.ok(homepageContentService.createOrUpdateTestimonial(request));
    }

    @DeleteMapping("/testimonials/{id}")
    public ResponseEntity<?> deleteTestimonial(@PathVariable UUID id) {
        homepageContentService.deleteTestimonial(id);
        return ResponseEntity.ok(Map.of("message", "Testimonial deleted successfully"));
    }

    @PostMapping("/announcements")
    public ResponseEntity<?> createAnnouncement(@RequestBody HomepageAnnouncementDto request) {
        return ResponseEntity.ok(homepageContentService.createOrUpdateAnnouncement(request));
    }

    @PutMapping("/announcements/{id}")
    public ResponseEntity<?> updateAnnouncement(@PathVariable UUID id, @RequestBody HomepageAnnouncementDto request) {
        request.setId(id);
        return ResponseEntity.ok(homepageContentService.createOrUpdateAnnouncement(request));
    }

    @DeleteMapping("/announcements/{id}")
    public ResponseEntity<?> deleteAnnouncement(@PathVariable UUID id) {
        homepageContentService.deleteAnnouncement(id);
        return ResponseEntity.ok(Map.of("message", "Announcement deleted successfully"));
    }

    @PostMapping("/upload/homepage-images")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        String url = homepageContentService.saveImage(file);
        return ResponseEntity.ok(Map.of(
                "url", url,
                "filename", file.getOriginalFilename(),
                "size", file.getSize()
        ));
    }
}
