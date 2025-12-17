package net.foodeals.crm.infrastructure.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.foodeals.crm.application.dto.requests.CrmDemandeResponseDto;
import net.foodeals.crm.application.dto.requests.DemandeCreateRequest;
import net.foodeals.crm.application.dto.requests.DemandeSearchQuery;
import net.foodeals.crm.application.dto.requests.DemandeUpdateRequest;
import net.foodeals.crm.application.dto.responses.DemandesResponse;
import net.foodeals.crm.application.services.CrmDemandeService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("v1/crm/demandes")
@RequiredArgsConstructor
public class CrmDemandeController {
    private final CrmDemandeService service;

    @GetMapping
    public DemandesResponse list(
        @RequestParam(required=false) String type,
        @RequestParam(defaultValue="1") Integer page,
        @RequestParam(defaultValue="10") Integer limit,
        @RequestParam(required=false, defaultValue="createdAt") String sort,
        @RequestParam(required=false, defaultValue="desc") String order,
        @RequestParam(required=false) String search
    ){
        return service.list(type, page, limit, sort, order, search);
    }

    @GetMapping("/{id}")
    public CrmDemandeResponseDto get(@PathVariable UUID id){ return service.get(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CrmDemandeResponseDto create(@RequestBody @Valid DemandeCreateRequest body){
        return service.create(body);
    }

    @PutMapping("/{id}")
    public CrmDemandeResponseDto update(@PathVariable UUID id, @RequestBody DemandeUpdateRequest body){
        return service.update(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id){ service.delete(id); }

    @GetMapping("/search")
    public DemandesResponse search(
        @RequestParam(required=false) String type,
        @RequestParam(required=false) String country,
        @RequestParam(required=false) String city,
        @RequestParam(required=false) String activity,
        @RequestParam(required=false) String status,
        @RequestParam(required=false) String dateFrom,
        @RequestParam(required=false) String dateTo,
        @RequestParam(required=false) String search,
        @RequestParam(defaultValue="1") Integer page,
        @RequestParam(defaultValue="10") Integer limit,
        @RequestParam(required=false, defaultValue="createdAt") String sort,
        @RequestParam(required=false, defaultValue="desc") String order
    ){
        return service.search(new DemandeSearchQuery(type, country, city, activity, status, dateFrom, dateTo, search, page, limit, sort, order));
    }
}
