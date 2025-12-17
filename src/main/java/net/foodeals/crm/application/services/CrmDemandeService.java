package net.foodeals.crm.application.services;


import lombok.RequiredArgsConstructor;
import net.foodeals.crm.application.dto.requests.CrmDemandeResponseDto;
import net.foodeals.crm.application.dto.requests.DemandeCreateRequest;
import net.foodeals.crm.application.dto.requests.DemandeSearchQuery;
import net.foodeals.crm.application.dto.requests.DemandeUpdateRequest;
import net.foodeals.crm.application.dto.requests.DocumentDto;
import net.foodeals.crm.application.dto.requests.HistoryEntryDto;
import net.foodeals.crm.application.dto.responses.DemandesResponse;
import net.foodeals.crm.application.specs.CrmDemandeSpecs;
import net.foodeals.crm.domain.entities.CrmDemande;
import net.foodeals.crm.domain.entities.CrmDemandeHistory;
import net.foodeals.crm.domain.entities.enums.DemandeStatus;
import net.foodeals.crm.domain.entities.enums.DemandeType;
import net.foodeals.crm.domain.repositories.CrmDemandeRepository;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class CrmDemandeService {
    private final CrmDemandeRepository repo;


    public DemandesResponse list(String type, Integer page, Integer limit, String sort, String order, String search){
        return search(new DemandeSearchQuery(type,null,null,null,null,null,null,search,page,limit,sort,order));
    }

    public CrmDemandeResponseDto get(UUID id){
        return toDto(repo.findById(id).orElseThrow());
    }

    @Transactional
    public CrmDemandeResponseDto create(DemandeCreateRequest r){
        var e = toEntity(r);
        var h = new CrmDemandeHistory();
        h.setId(UUID.randomUUID());
        h.setDemande(e);
        h.setAction("created");
        h.setPerformedBy("system");
        h.setTimestamp(OffsetDateTime.now(ZoneOffset.UTC));
        h.setDetails("Demande créée");
        e.getHistory().add(h);
        return toDto(repo.save(e));
    }

    @Transactional
    public CrmDemandeResponseDto update(UUID id, DemandeUpdateRequest r){
        var e = repo.findById(id).orElseThrow();
        update(e, r);
        var h = new CrmDemandeHistory();
        h.setId(UUID.randomUUID());
        h.setDemande(e);
        h.setAction("updated");
        h.setPerformedBy("system");
        h.setTimestamp(OffsetDateTime.now(ZoneOffset.UTC));
        h.setDetails("Demande mise à jour");
        e.getHistory().add(h);
        return toDto(repo.save(e));
    }

    @Transactional
    public void delete(UUID id){ repo.deleteById(id); }

    public DemandesResponse search(DemandeSearchQuery q){
        int p = (q.page()==null || q.page()<1) ? 1 : q.page();
        int size = (q.limit()==null || q.limit()<1) ? 10 : q.limit();

        String sortField = switch (q.sort()==null? "createdAt" : q.sort()){
            case "updatedAt" -> "updatedAt";
            case "companyName" -> "companyName";
            case "date" -> "date";
            default -> "createdAt";
        };
        Sort.Direction dir = "asc".equalsIgnoreCase(q.order()) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(p-1, size, Sort.by(dir, sortField));

        Specification<CrmDemande> spec = Specification
            .where(CrmDemandeSpecs.typeEq(q.type()))
            .and(CrmDemandeSpecs.countryEq(q.country()))
            .and(CrmDemandeSpecs.cityEq(q.city()))
            .and(CrmDemandeSpecs.statusEq(q.status()))
            .and(CrmDemandeSpecs.activityHas(q.activity()))
            .and(CrmDemandeSpecs.createdBetween(parseDate(q.dateFrom()), parseDate(q.dateTo())))
            .and(CrmDemandeSpecs.search(q.search()));

        Page<CrmDemande> pageRes = repo.findAll(spec, pageable);
        var list = pageRes.getContent().stream().map(this::toDto).toList();

        var pagination = new DemandesResponse.Pagination(
            p, pageRes.getTotalPages(), pageRes.getTotalElements(), size, pageRes.hasNext(), pageRes.hasPrevious()
        );

        var filters = new DemandesResponse.Filters(
            // TODO: remplacer par des DISTINCT DB si besoin dynamique
            List.of("France","Belgium","Switzerland"),
            List.of("Paris","Lyon","Marseille"),
            List.of("Restaurant","Traiteur","Boulangerie","Épicerie"),
            List.of("pending","approved","rejected")
        );

        return new DemandesResponse(list, pagination, filters);
    }

    private OffsetDateTime parseDate(String s){
        if (s==null || s.isBlank()) return null;
        return OffsetDateTime.parse(s);
    }
    
    
    public CrmDemande toEntity(DemandeCreateRequest r){
        var e = new CrmDemande();
        e.setId(UUID.randomUUID());
        e.setType(parseType(r.type()));
        e.setCompanyName(r.companyName());
        e.setActivity(new LinkedHashSet<>(r.activity()));
        e.setCountry(r.country());
        e.setCity(r.city());
        e.setResponsable(r.respansable());
        e.setAddress(r.address());
        e.setEmail(r.email());
        e.setPhone(r.phone());
        e.setStatus(DemandeStatus.PENDING);
        e.setNotes(r.notes());
      
        return e;
    }

    public void update(CrmDemande e, DemandeUpdateRequest r){
        if (r.type()!=null) e.setType(parseType(r.type()));
        if (r.companyName()!=null) e.setCompanyName(r.companyName());
        if (r.activity()!=null) e.setActivity(new LinkedHashSet<>(r.activity()));
        if (r.country()!=null) e.setCountry(r.country());
        if (r.city()!=null) e.setCity(r.city());
        if (r.respansable()!=null) e.setResponsable(r.respansable());
        if (r.address()!=null) e.setAddress(r.address());
        if (r.email()!=null) e.setEmail(r.email());
        if (r.phone()!=null) e.setPhone(r.phone());
        if (r.status()!=null) e.setStatus(parseStatus(r.status()));
        if (r.notes()!=null) e.setNotes(r.notes());
        e.setUpdatedAt(Instant.now());
    }

 // CrmDemandeMapper
    private String iso(OffsetDateTime dt) {
        return (dt == null) ? null : dt.toInstant().toString();
    }

    public CrmDemandeResponseDto toDto(CrmDemande e){
        return new CrmDemandeResponseDto(
            e.getId().toString(),
            e.getCompanyName(),
            new ArrayList<>(e.getActivity()),
            e.getCountry(),
            e.getCity(),
            iso(e.getDate()),            // <-- null-safe
            e.getResponsable(),
            e.getAddress(),
            e.getEmail(),
            e.getPhone(),
            e.getStatus()==null? null : e.getStatus().getValue(),
            iso(e.getCreatedAt()),
            iso(e.getUpdatedAt()),
            e.getNotes(),
            e.getDocuments().stream().map(d-> new DocumentDto(
                d.getId().toString(), d.getName(), d.getUrl(), d.getType()
            )).toList(),
            e.getHistory().stream()
                .sorted(Comparator.comparing(h -> Optional.ofNullable(h.getTimestamp()).orElse(OffsetDateTime.MIN)))
                .map(h-> new HistoryEntryDto(
                    h.getId().toString(), h.getAction(), h.getPerformedBy(), iso(h.getTimestamp()), h.getDetails()
                )).toList()
        );
    }

    private String iso(Instant dt){ return dt==null? null : dt.toString(); }

    private DemandeType parseType(String v){
        if (v==null) return null;
        return Arrays.stream(DemandeType.values()).filter(t->t.getValue().equalsIgnoreCase(v)).findFirst().orElse(null);
    }
    private DemandeStatus parseStatus(String v){
        if (v==null) return null;
        return Arrays.stream(DemandeStatus.values()).filter(t->t.getValue().equalsIgnoreCase(v)).findFirst().orElse(null);
    }
}

