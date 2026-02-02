package net.foodeals.homepage.application.dtos;

import java.util.List;

public record HomepageContentResponse(
        HeroSectionDto hero,
        List<TestimonialDto> testimonials,
        List<AnnouncementDto> announcements,
        List<CategoryDto> categories,
        List<FeaturedDealDto> featuredDeals
) {
}
