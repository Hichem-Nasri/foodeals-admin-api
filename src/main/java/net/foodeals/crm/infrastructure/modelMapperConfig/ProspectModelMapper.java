package net.foodeals.crm.infrastructure.modelMapperConfig;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import net.foodeals.crm.application.dto.requests.AddressDto;
import net.foodeals.crm.application.dto.requests.EventRequest;
import net.foodeals.crm.application.dto.responses.*;
import net.foodeals.crm.domain.entities.Event;
import net.foodeals.crm.domain.entities.Prospect;
import net.foodeals.crm.domain.entities.enums.ProspectStatus;
import net.foodeals.location.application.dtos.responses.CityResponse;
import net.foodeals.location.application.dtos.responses.CountryResponse;
import net.foodeals.location.application.dtos.responses.RegionResponse;
import net.foodeals.location.application.dtos.responses.StateResponse;
import net.foodeals.organizationEntity.application.dtos.requests.ContactDto;
import net.foodeals.organizationEntity.domain.entities.Contact;
import net.foodeals.organizationEntity.domain.entities.Solution;
import net.foodeals.payment.application.dto.response.PartnerInfoDto;
import net.foodeals.user.domain.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ProspectModelMapper {
    private final ModelMapper modelMapper;

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void config() {

        modelMapper.addConverter(mappingContext -> {
            final User user = mappingContext.getSource();

            return new CreatorInfoDto(user.getName(), user.getAvatarPath(), user.getId());

        }, User.class, CreatorInfoDto.class);

        modelMapper.addConverter(mappingContext -> {
            final Prospect prospect = mappingContext.getSource();

            String cityName = "";
            if (prospect.getAddress() != null
                    && prospect.getAddress().getRegion() != null
                    && prospect.getAddress().getRegion().getCity() != null) {
                cityName = prospect.getAddress().getRegion().getCity().getName();
            }

            return new PartnerInfoDto(prospect.getId(), prospect.getName(), "", cityName);
        }, Prospect.class, PartnerInfoDto.class);

        modelMapper.addConverter(mappingContext -> {
            final User user = mappingContext.getSource();

            return new ManagerInfoDto(user.getName(), user.getAvatarPath(), user.getId());

        }, User.class, ManagerInfoDto.class);

        this.modelMapper.addConverter(mappingContext -> {
            final Prospect prospect = mappingContext.getSource();
            String createdDate = null;
            if (prospect.getCreatedAt() != null) {
                OffsetDateTime dateTime = OffsetDateTime.parse(prospect.getCreatedAt().toString());
                LocalDate date = dateTime.toLocalDate();
                createdDate = date.toString();
            }

            String category = (prospect.getActivities() != null && prospect.getActivities().size() > 0)
                    ? prospect.getActivities().iterator().next().getName()
                    : "";

            ContactDto contactInfo = null;
            if (prospect.getContacts() != null && !prospect.getContacts().isEmpty()) {
                Contact contact = prospect.getContacts().get(0);
                if (contact != null) {
                    contactInfo = new ContactDto(contact.getName(), contact.getEmail(), contact.getPhone());
                }
            }

            ProspectAddress addressDto = null;
            if (prospect.getAddress() != null
                    && prospect.getAddress().getRegion() != null
                    && prospect.getAddress().getRegion().getCity() != null
                    && prospect.getAddress().getRegion().getCity().getState() != null
                    && prospect.getAddress().getRegion().getCity().getState().getCountry() != null) {
                CityResponse cityResponse = this.modelMapper.map(prospect.getAddress().getRegion().getCity(), CityResponse.class);
                StateResponse stateResponse = this.modelMapper.map(prospect.getAddress().getRegion().getCity().getState(), StateResponse.class);
                CountryResponse countryResponse = this.modelMapper.map(prospect.getAddress().getRegion().getCity().getState().getCountry(), CountryResponse.class);
                RegionResponse regionResponse = this.modelMapper.map(prospect.getAddress().getRegion(), RegionResponse.class);
                String address = prospect.getAddress().getAddress();
                addressDto = new ProspectAddress(address, countryResponse, cityResponse, stateResponse, regionResponse, null);
            }

            final User creator = prospect.getCreator();
            final User lead = prospect.getLead();

            CreatorInfoDto creatorInfoDto = creator != null ? this.modelMapper.map(creator, CreatorInfoDto.class) : null;
            ManagerInfoDto managerInfoDto = lead != null ? this.modelMapper.map(lead, ManagerInfoDto.class) : null;

            List<String> solutionNames = prospect.getSolutions() != null
                    ? prospect.getSolutions().stream().map(Solution::getName).collect(Collectors.toList())
                    : new ArrayList<>();

            ProspectStatus status =  prospect.getStatus();
            List<EventResponse> eventResponses = prospect.getEvents() != null && prospect.getEvents().size() != 0
                    ? prospect.getEvents().stream()
                    .filter(event -> event.getDeletedAt() == null)
                    .sorted(Comparator.comparing(Event::getCreatedAt).reversed())
                    .map((Event event) -> this.modelMapper.map(event, EventResponse.class))
                    .toList()
                    : null;
            return new ProspectResponse(prospect.getId(), createdDate, prospect.getName(), category, contactInfo, addressDto, creatorInfoDto, managerInfoDto, status, eventResponses, solutionNames, prospect.getType());
        }, Prospect.class, ProspectResponse.class);
    }
}
