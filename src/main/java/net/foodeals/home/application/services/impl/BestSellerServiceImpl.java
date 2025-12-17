package net.foodeals.home.application.services.impl;

import lombok.RequiredArgsConstructor;
import net.foodeals.home.application.dtos.BestSellerDto;
import net.foodeals.home.application.services.BestSellerService;
import net.foodeals.home.domain.entities.PersonalizedBestSeller;
import net.foodeals.home.domain.repositories.PersonalizedBestSellerRepository;
import net.foodeals.location.domain.entities.City;
import net.foodeals.location.domain.entities.Country;
import net.foodeals.location.domain.entities.State;
import net.foodeals.location.domain.repositories.CityRepository;
import net.foodeals.location.domain.repositories.CountryRepository;
import net.foodeals.location.domain.repositories.StateRepository;
import net.foodeals.offer.domain.entities.Box;
import net.foodeals.offer.domain.entities.Deal;
import net.foodeals.offer.domain.entities.Offer;
import net.foodeals.offer.domain.repositories.BoxRepository;
import net.foodeals.offer.domain.repositories.DealRepository;
import net.foodeals.order.domain.entities.Order;
import net.foodeals.order.domain.enums.OrderStatus;
import net.foodeals.order.domain.repositories.OrderRepository;
import net.foodeals.user.domain.entities.User;
import net.foodeals.user.domain.repositories.UserRepository;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BestSellerServiceImpl implements BestSellerService {

	private final CountryRepository countryRepo;
	private final StateRepository stateRepo;
	private final CityRepository cityRepo;
	private final PersonalizedBestSellerRepository personalizedRepo;
	private final OrderRepository orderRepository;
	private final DealRepository dealRepository;
	private final BoxRepository boxRepository;
	private final UserRepository userRepository;

	@Transactional
	public List<BestSellerDto> getBestSellers(String countryName, String stateName, String cityName, String sortedBy) {
		List<Order> completedOrders = orderRepository.findByStatus(OrderStatus.COMPLETED);
		Map<String, BestSellerDto> bestSellers = new HashMap<>();

		for (Order order : completedOrders) {
			//User client =userRepository.find
			if (order.getClient() == null || order.getClient().getAddress() == null) continue;

			// --- Filtrage géographique
			if (countryName != null && (order.getClient().getAddress().getRegion() == null
					|| order.getClient().getAddress().getRegion().getCity() == null
					|| order.getClient().getAddress().getRegion().getCity().getState() == null
					|| order.getClient().getAddress().getRegion().getCity().getState().getCountry() == null
					|| !countryName.equals(order.getClient().getAddress().getRegion().getCity().getState().getCountry().getName()))) {
				continue;
			}
			if (stateName != null && !stateName.equals(order.getClient().getAddress().getRegion().getCity().getState().getName())) continue;
			if (cityName != null && !cityName.equals(order.getClient().getAddress().getRegion().getCity().getName())) continue;

			Offer offer = order.getOffer();
			String key = null;
			String name = null;
			String image = null;
			int quantity = order.getQuantity();

			Deal deal = dealRepository.getDealByOfferId(offer.getId());
			if (deal != null) {
				key = "deal_" + deal.getId();
				name = deal.getProduct().getName();
				image = deal.getProduct().getProductImagePath();
			} else {
				Box box = boxRepository.getBoxByOfferId(offer.getId());
				if (box != null) {
					key = "box_" + box.getId();
					name = "Box #" + box.getId();
					image = box.getBoxItems().isEmpty() ? null : box.getBoxItems().get(0).getProduct().getProductImagePath();
				} else {
					continue; // ni box ni deal trouvé
				}
			}

			BestSellerDto dto = bestSellers.getOrDefault(
				key,
				BestSellerDto.builder()
					.name(name)
					.image(image)
					.totalSells(0)
					.completedOrders(0)
					.rating(0f)
					.build()
			);

			dto.setTotalSells(dto.getTotalSells() + quantity);
			dto.setCompletedOrders(dto.getCompletedOrders() + 1);
			bestSellers.put(key, dto);
		}

		return bestSellers.values().stream()
			.sorted((a, b) -> {
				if ("sellers".equalsIgnoreCase(sortedBy)) {
					return Integer.compare(b.getTotalSells(), a.getTotalSells());
				} else if ("completed".equalsIgnoreCase(sortedBy)) {
					return Integer.compare(b.getCompletedOrders(), a.getCompletedOrders());
				}
				return 0;
			})
			.collect(Collectors.toList());

	}

	@Override
	public void savePersonalized(List<BestSellerDto> dtos, String countryName, String stateName, String cityName) {
		Country country = countryName != null ? countryRepo.findByName(countryName) : null;
		State state = stateName != null ? stateRepo.findByName(stateName) : null;
		City city = cityName != null ? cityRepo.findByName(cityName) : null;

		personalizedRepo.deleteAll(personalizedRepo.findByCountryAndStateAndCity(country, state, city));

		List<PersonalizedBestSeller> list = dtos.stream().map(dto -> {
			PersonalizedBestSeller p = new PersonalizedBestSeller();
			p.setName(dto.getName());
			p.setImage(dto.getImage());
			p.setTotalSales(dto.getTotalSells());
			p.setCompletedOrders(dto.getCompletedOrders());
			p.setRating(dto.getRating());
			p.setCountry(country);
			p.setState(state);
			p.setCity(city);
			return p;
		}).collect(Collectors.toList());

		personalizedRepo.saveAll(list);
	}

	private BestSellerDto toDto(PersonalizedBestSeller e) {
		return BestSellerDto.builder().name(e.getName()).image(e.getImage()).totalSells(e.getTotalSales())
				.completedOrders(e.getCompletedOrders()).rating(e.getRating()).build();
	}
}
