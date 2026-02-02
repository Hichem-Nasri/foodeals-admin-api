package net.foodeals.homepage.infrastructure.controllers;

import lombok.RequiredArgsConstructor;
import net.foodeals.homepage.application.dtos.*;
import net.foodeals.homepage.application.services.HomepageContentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class HomepageContentController {

    private final HomepageContentService homepageContentService;

    @Value("${upload.directory:photos}")
    private String uploadDirectory;

    @GetMapping("/homepage/content")
    public HomepageContentResponse getHomepageContent(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String city
    ) {
        return homepageContentService.getHomepageContent(country, state, city);
    }

    @PutMapping("/homepage/hero")
    public HeroSectionDto updateHeroSection(
            @RequestBody UpdateHeroRequest request,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String city
    ) {
        return homepageContentService.updateHero(request, country, state, city);
    }

    @PutMapping("/homepage/featured-deals")
    public void updateFeaturedDeals(
            @RequestBody UpdateFeaturedDealsRequest request,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String city
    ) {
        homepageContentService.updateFeaturedDeals(request, country, state, city);
    }

    @PutMapping("/homepage/categories/order")
    public void updateCategoriesOrder(
            @RequestBody List<UpdateCategoriesOrderRequest> request,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String city
    ) {
        homepageContentService.updateCategoriesOrder(request, country, state, city);
    }

    @PostMapping("/homepage/testimonials")
    public TestimonialDto createTestimonial(
            @RequestBody CreateTestimonialRequest request,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String city
    ) {
        return homepageContentService.createTestimonial(request, country, state, city);
    }

    @PutMapping("/homepage/testimonials/{id}")
    public TestimonialDto updateTestimonial(
            @PathVariable String id,
            @RequestBody UpdateTestimonialRequest request,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String city
    ) {
        return homepageContentService.updateTestimonial(id, request, country, state, city);
    }

    @DeleteMapping("/homepage/testimonials/{id}")
    public void deleteTestimonial(
            @PathVariable String id,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String city
    ) {
        homepageContentService.deleteTestimonial(id, country, state, city);
    }

    @PostMapping("/homepage/announcements")
    public AnnouncementDto createAnnouncement(
            @RequestBody CreateAnnouncementRequest request,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String city
    ) {
        return homepageContentService.createAnnouncement(request, country, state, city);
    }

    @PutMapping("/homepage/announcements/{id}")
    public AnnouncementDto updateAnnouncement(
            @PathVariable String id,
            @RequestBody UpdateAnnouncementRequest request,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String city
    ) {
        return homepageContentService.updateAnnouncement(id, request, country, state, city);
    }

    @DeleteMapping("/homepage/announcements/{id}")
    public void deleteAnnouncement(
            @PathVariable String id,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String city
    ) {
        homepageContentService.deleteAnnouncement(id, country, state, city);
    }

    @PostMapping(value = "/homepage/upload/homepage-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, String> uploadHomepageImage(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is required");
        }
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        }
        String filename = UUID.randomUUID() + extension;
        Path uploadPath = Paths.get(uploadDirectory);
        if (!uploadPath.isAbsolute()) {
            uploadPath = Paths.get(System.getProperty("user.dir")).resolve(uploadDirectory);
        }
        try {
            Files.createDirectories(uploadPath);
            Path targetPath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to store file");
        }
        return Map.of("url", "/photos/" + filename);
    }

    @GetMapping("/home-sorting/content")
    public List<ContentSortingItem> getContentSorting(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String city
    ) {
        return homepageContentService.getContentSorting(country, state, city);
    }

    @PostMapping("/home-sorting")
    public void updateContentSorting(
            @RequestBody List<ContentSortingItem> items,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String city
    ) {
        homepageContentService.updateContentSorting(items, country, state, city);
    }

    @GetMapping("/best-sellers")
    public List<BestSellerDto> getBestSellers(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String city
    ) {
        return homepageContentService.getBestSellers(country, state, city);
    }
}
