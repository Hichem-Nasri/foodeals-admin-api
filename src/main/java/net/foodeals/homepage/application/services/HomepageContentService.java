package net.foodeals.homepage.application.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.foodeals.homepage.application.dtos.*;
import net.foodeals.homepage.domain.entities.HomepageContentEntity;
import net.foodeals.homepage.domain.repositories.HomepageContentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomepageContentService {

    private static final List<String> DEFAULT_CONTENT_SORTING = List.of(
            "deal",
            "box",
            "hotel",
            "architecture",
            "boulangerie",
            "restaurant",
            "supermarket",
            "pharmacy",
            "clinic",
            "beauty",
            "sport",
            "education",
            "transport",
            "entertainment"
    );

    private final HomepageContentRepository homepageContentRepository;
    private final ObjectMapper objectMapper;

    public HomepageContentResponse getHomepageContent(String country, String state, String city) {
        HomepageContentEntity entity = resolveContent(country, state, city, false);
        if (entity == null) {
            return buildDefaultContent();
        }
        return mapToResponse(entity);
    }

    @Transactional
    public HeroSectionDto updateHero(UpdateHeroRequest request, String country, String state, String city) {
        HomepageContentEntity entity = resolveContent(country, state, city, true);
        HeroSectionDto existing = readHero(entity.getHeroJson());
        String heroId = existing != null ? existing.id() : UUID.randomUUID().toString();
        HeroSectionDto hero = new HeroSectionDto(
                heroId,
                request.title(),
                request.subtitle(),
                request.backgroundImage(),
                request.ctaText(),
                request.ctaLink(),
                Instant.now().toString(),
                Boolean.TRUE.equals(request.isActive())
        );
        entity.setHeroJson(writeValue(hero));
        homepageContentRepository.save(entity);
        return hero;
    }

    @Transactional
    public TestimonialDto createTestimonial(CreateTestimonialRequest request, String country, String state, String city) {
        HomepageContentEntity entity = resolveContent(country, state, city, true);
        List<TestimonialDto> testimonials = readTestimonials(entity.getTestimonialsJson());
        int order = request.order() != null ? request.order() : testimonials.size() + 1;
        TestimonialDto testimonial = new TestimonialDto(
                UUID.randomUUID().toString(),
                request.customerName(),
                request.rating(),
                request.comment(),
                request.avatar(),
                order,
                Instant.now().toString(),
                Boolean.TRUE.equals(request.isActive())
        );
        testimonials.add(testimonial);
        entity.setTestimonialsJson(writeValue(testimonials));
        homepageContentRepository.save(entity);
        return testimonial;
    }

    @Transactional
    public TestimonialDto updateTestimonial(String id, UpdateTestimonialRequest request, String country, String state, String city) {
        HomepageContentEntity entity = resolveContent(country, state, city, true);
        List<TestimonialDto> testimonials = readTestimonials(entity.getTestimonialsJson());
        int index = findIndexById(testimonials, id);
        if (index < 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Testimonial not found");
        }
        TestimonialDto current = testimonials.get(index);
        TestimonialDto updated = new TestimonialDto(
                current.id(),
                request.customerName(),
                request.rating(),
                request.comment(),
                request.avatar(),
                request.order() != null ? request.order() : current.order(),
                current.createdAt(),
                Boolean.TRUE.equals(request.isActive())
        );
        testimonials.set(index, updated);
        entity.setTestimonialsJson(writeValue(testimonials));
        homepageContentRepository.save(entity);
        return updated;
    }

    @Transactional
    public void deleteTestimonial(String id, String country, String state, String city) {
        HomepageContentEntity entity = resolveContent(country, state, city, true);
        List<TestimonialDto> testimonials = readTestimonials(entity.getTestimonialsJson());
        int index = findIndexById(testimonials, id);
        if (index < 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Testimonial not found");
        }
        testimonials.remove(index);
        entity.setTestimonialsJson(writeValue(testimonials));
        homepageContentRepository.save(entity);
    }

    @Transactional
    public AnnouncementDto createAnnouncement(CreateAnnouncementRequest request, String country, String state, String city) {
        HomepageContentEntity entity = resolveContent(country, state, city, true);
        List<AnnouncementDto> announcements = readAnnouncements(entity.getAnnouncementsJson());
        AnnouncementDto announcement = new AnnouncementDto(
                UUID.randomUUID().toString(),
                request.title(),
                request.message(),
                request.type(),
                request.expiresAt(),
                Instant.now().toString(),
                Boolean.TRUE.equals(request.isActive())
        );
        announcements.add(announcement);
        entity.setAnnouncementsJson(writeValue(announcements));
        homepageContentRepository.save(entity);
        return announcement;
    }

    @Transactional
    public AnnouncementDto updateAnnouncement(String id, UpdateAnnouncementRequest request, String country, String state, String city) {
        HomepageContentEntity entity = resolveContent(country, state, city, true);
        List<AnnouncementDto> announcements = readAnnouncements(entity.getAnnouncementsJson());
        int index = findIndexById(announcements, id);
        if (index < 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Announcement not found");
        }
        AnnouncementDto current = announcements.get(index);
        AnnouncementDto updated = new AnnouncementDto(
                current.id(),
                request.title(),
                request.message(),
                request.type(),
                request.expiresAt(),
                current.createdAt(),
                Boolean.TRUE.equals(request.isActive())
        );
        announcements.set(index, updated);
        entity.setAnnouncementsJson(writeValue(announcements));
        homepageContentRepository.save(entity);
        return updated;
    }

    @Transactional
    public void deleteAnnouncement(String id, String country, String state, String city) {
        HomepageContentEntity entity = resolveContent(country, state, city, true);
        List<AnnouncementDto> announcements = readAnnouncements(entity.getAnnouncementsJson());
        int index = findIndexById(announcements, id);
        if (index < 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Announcement not found");
        }
        announcements.remove(index);
        entity.setAnnouncementsJson(writeValue(announcements));
        homepageContentRepository.save(entity);
    }

    @Transactional
    public void updateFeaturedDeals(UpdateFeaturedDealsRequest request, String country, String state, String city) {
        if (request == null) {
            return;
        }
        HomepageContentEntity entity = resolveContent(country, state, city, true);
        List<FeaturedDealDto> featuredDeals = readFeaturedDeals(entity.getFeaturedDealsJson());
        if (featuredDeals.isEmpty()) {
            return;
        }
        Map<String, Integer> orderMap = buildOrderMap(request.dealIds(), request.order());
        List<FeaturedDealDto> updated = featuredDeals.stream()
                .map(deal -> orderMap.containsKey(deal.id())
                        ? new FeaturedDealDto(
                        deal.id(),
                        deal.title(),
                        deal.description(),
                        deal.image(),
                        deal.originalPrice(),
                        deal.discountedPrice(),
                        deal.restaurant(),
                        deal.restaurantId(),
                        orderMap.get(deal.id()),
                        deal.createdAt(),
                        deal.active()
                )
                        : deal)
                .collect(Collectors.toList());
        entity.setFeaturedDealsJson(writeValue(updated));
        homepageContentRepository.save(entity);
    }

    @Transactional
    public void updateCategoriesOrder(List<UpdateCategoriesOrderRequest> request, String country, String state, String city) {
        if (request == null || request.isEmpty()) {
            return;
        }
        HomepageContentEntity entity = resolveContent(country, state, city, true);
        List<CategoryDto> categories = readCategories(entity.getCategoriesJson());
        if (categories.isEmpty()) {
            return;
        }
        Map<String, Integer> orderMap = request.stream()
                .filter(item -> item.id() != null && item.order() != null)
                .collect(Collectors.toMap(UpdateCategoriesOrderRequest::id, UpdateCategoriesOrderRequest::order, (a, b) -> b));
        List<CategoryDto> updated = categories.stream()
                .map(category -> orderMap.containsKey(category.id())
                        ? new CategoryDto(
                        category.id(),
                        category.name(),
                        category.icon(),
                        orderMap.get(category.id()),
                        category.dealCount(),
                        category.active()
                )
                        : category)
                .collect(Collectors.toList());
        entity.setCategoriesJson(writeValue(updated));
        homepageContentRepository.save(entity);
    }

    public List<ContentSortingItem> getContentSorting(String country, String state, String city) {
        HomepageContentEntity entity = resolveContent(country, state, city, false);
        if (entity == null || entity.getContentSortingJson() == null || entity.getContentSortingJson().isBlank()) {
            return buildDefaultContentSorting();
        }
        List<ContentSortingItem> items = readContentSorting(entity.getContentSortingJson());
        return items.isEmpty() ? buildDefaultContentSorting() : items;
    }

    @Transactional
    public void updateContentSorting(List<ContentSortingItem> items, String country, String state, String city) {
        if (items == null) {
            return;
        }
        HomepageContentEntity entity = resolveContent(country, state, city, true);
        List<ContentSortingItem> normalized = new ArrayList<>();
        int index = 1;
        for (ContentSortingItem item : items) {
            String id = item.id() != null ? item.id() : item.name();
            Integer orderClass = item.orderClass() != null ? item.orderClass() : index;
            normalized.add(new ContentSortingItem(id, item.name(), orderClass));
            index++;
        }
        entity.setContentSortingJson(writeValue(normalized));
        homepageContentRepository.save(entity);
    }

    public List<BestSellerDto> getBestSellers(String country, String state, String city) {
        return Collections.emptyList();
    }

    private HomepageContentEntity resolveContent(String country, String state, String city, boolean createIfMissing) {
        boolean hasTarget = (country != null && !country.isBlank())
                || (state != null && !state.isBlank())
                || (city != null && !city.isBlank());

        HomepageContentEntity entity = null;
        if (hasTarget) {
            entity = homepageContentRepository.findFirstByCountryAndStateAndCity(country, state, city).orElse(null);
            if (entity == null && createIfMissing) {
                HomepageContentEntity base = homepageContentRepository.findFirstByCountryIsNullAndStateIsNullAndCityIsNull().orElse(null);
                entity = cloneContent(base, country, state, city);
                homepageContentRepository.save(entity);
            }
        }

        if (entity == null) {
            entity = homepageContentRepository.findFirstByCountryIsNullAndStateIsNullAndCityIsNull().orElse(null);
            if (entity == null && createIfMissing) {
                entity = createDefaultEntity(null, null, null);
                homepageContentRepository.save(entity);
            }
        }

        return entity;
    }

    private HomepageContentEntity cloneContent(HomepageContentEntity base, String country, String state, String city) {
        if (base == null) {
            return createDefaultEntity(country, state, city);
        }
        return HomepageContentEntity.builder()
                .country(country)
                .state(state)
                .city(city)
                .heroJson(base.getHeroJson())
                .testimonialsJson(base.getTestimonialsJson())
                .announcementsJson(base.getAnnouncementsJson())
                .categoriesJson(base.getCategoriesJson())
                .featuredDealsJson(base.getFeaturedDealsJson())
                .contentSortingJson(base.getContentSortingJson())
                .build();
    }

    private HomepageContentEntity createDefaultEntity(String country, String state, String city) {
        HeroSectionDto hero = buildDefaultHero();
        HomepageContentEntity entity = HomepageContentEntity.builder()
                .country(country)
                .state(state)
                .city(city)
                .heroJson(writeValue(hero))
                .testimonialsJson(writeValue(Collections.emptyList()))
                .announcementsJson(writeValue(Collections.emptyList()))
                .categoriesJson(writeValue(Collections.emptyList()))
                .featuredDealsJson(writeValue(Collections.emptyList()))
                .contentSortingJson(writeValue(buildDefaultContentSorting()))
                .build();
        return entity;
    }

    private HomepageContentResponse mapToResponse(HomepageContentEntity entity) {
        HeroSectionDto hero = readHero(entity.getHeroJson());
        if (hero == null) {
            hero = buildDefaultHero();
        }
        return new HomepageContentResponse(
                hero,
                readTestimonials(entity.getTestimonialsJson()),
                readAnnouncements(entity.getAnnouncementsJson()),
                readCategories(entity.getCategoriesJson()),
                readFeaturedDeals(entity.getFeaturedDealsJson())
        );
    }

    private HomepageContentResponse buildDefaultContent() {
        HeroSectionDto hero = buildDefaultHero();
        return new HomepageContentResponse(
                hero,
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList()
        );
    }

    private HeroSectionDto buildDefaultHero() {
        return new HeroSectionDto(
                "default-hero",
                "Foodeals",
                "Discover great local deals",
                "/icons/deal.png",
                "Explore",
                "/",
                Instant.now().toString(),
                true
        );
    }

    private HeroSectionDto readHero(String json) {
        if (json == null || json.isBlank()) {
            return null;
        }
        try {
            return objectMapper.readValue(json, HeroSectionDto.class);
        } catch (Exception ignored) {
            return null;
        }
    }

    private List<TestimonialDto> readTestimonials(String json) {
        return readList(json, new TypeReference<List<TestimonialDto>>() {});
    }

    private List<AnnouncementDto> readAnnouncements(String json) {
        return readList(json, new TypeReference<List<AnnouncementDto>>() {});
    }

    private List<CategoryDto> readCategories(String json) {
        return readList(json, new TypeReference<List<CategoryDto>>() {});
    }

    private List<FeaturedDealDto> readFeaturedDeals(String json) {
        return readList(json, new TypeReference<List<FeaturedDealDto>>() {});
    }

    private List<ContentSortingItem> readContentSorting(String json) {
        return readList(json, new TypeReference<List<ContentSortingItem>>() {});
    }

    private <T> List<T> readList(String json, TypeReference<List<T>> typeReference) {
        if (json == null || json.isBlank()) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (Exception ignored) {
            return new ArrayList<>();
        }
    }

    private String writeValue(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to serialize homepage content");
        }
    }

    private List<ContentSortingItem> buildDefaultContentSorting() {
        List<ContentSortingItem> items = new ArrayList<>();
        int order = 1;
        for (String name : DEFAULT_CONTENT_SORTING) {
            items.add(new ContentSortingItem(name, name, order));
            order++;
        }
        return items;
    }

    private Map<String, Integer> buildOrderMap(List<String> ids, List<Integer> orders) {
        if (ids == null || orders == null) {
            return Collections.emptyMap();
        }
        int size = Math.min(ids.size(), orders.size());
        Map<String, Integer> map = new java.util.HashMap<>();
        for (int i = 0; i < size; i++) {
            map.put(ids.get(i), orders.get(i));
        }
        return map;
    }

    private <T> int findIndexById(List<T> items, String id) {
        if (items == null || id == null) {
            return -1;
        }
        for (int i = 0; i < items.size(); i++) {
            Object item = items.get(i);
            if (item instanceof TestimonialDto testimonial && id.equals(testimonial.id())) {
                return i;
            }
            if (item instanceof AnnouncementDto announcement && id.equals(announcement.id())) {
                return i;
            }
        }
        return -1;
    }
}
