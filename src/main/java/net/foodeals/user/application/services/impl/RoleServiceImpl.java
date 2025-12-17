package net.foodeals.user.application.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.user.application.dtos.requests.RoleRequest;
import net.foodeals.user.application.dtos.responses.RoleBasePermissionsDto;
import net.foodeals.user.application.services.RoleService;
import net.foodeals.user.application.services.FindAllAuthoritiesByIdsUseCase;
import net.foodeals.user.domain.entities.Authority;
import net.foodeals.user.domain.entities.Role;
import net.foodeals.user.domain.exceptions.AuthorityNotFoundException;
import net.foodeals.user.domain.exceptions.RoleNotFoundException;
import net.foodeals.user.domain.repositories.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;
    private final FindAllAuthoritiesByIdsUseCase findAllAuthoritiesByIdsUseCase;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public List<Role> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public Page<Role> findAll(Integer pageNumber, Integer pageSize) {
        return null;
    }

    @Override
    @Transactional
    public Page<Role> findAll(Pageable pageable) {
        return null;
    }

    @Override
    @Transactional
    public Role findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException(id));
    }

    @Override
    @Transactional
    public Role findByName(String name) {
        return repository.findByName(name.toUpperCase())
                .orElseThrow(() -> new RoleNotFoundException(name));
    }

    @Override
    @Transactional
    public Role create(RoleRequest request) {
        List<Authority> authorities = findAllAuthoritiesByIdsUseCase.execute(request.authorityIds());
        Role role = modelMapper.map(request, Role.class);
        role.setAuthorities(authorities);
        return repository.save(role);
    }

    @Override
    @Transactional
    public Role update(UUID id, RoleRequest request) {
        List<Authority> authorities = findAllAuthoritiesByIdsUseCase.execute(request.authorityIds());
        Role role = findById(id);
        modelMapper.map(request, role);
        role.setAuthorities(authorities);
        return repository.save(role);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (repository.existsById(id))
            throw new AuthorityNotFoundException(id);
        repository.softDelete(id);
    }
    
    @Override
    public RoleBasePermissionsDto getRolePermissions(String roleKey) {
        Map<String, List<String>> rolePermissions = Map.of(
            "SUPER_ADMIN", List.of(
                "dashboard.access", "statistics.access", "statistics.global", "statistics.partners", "statistics.clients",
                "statistics.finance", "statistics.associations", "payment.access", "payment.business", "payment.delivery",
                "partners.access", "delivery.access", "associations.access", "clients.access", "crm.access",
                "crm.prospects", "crm.requests", "marketing.access", "marketing.coupons", "marketing.notifications",
                "hr.access", "hr.permissions", "website.access", "website.blog", "website.press", "website.support",
                "settings.access", "settings.products", "settings.categories", "settings.supplements", "settings.locations",
                "settings.orders", "create", "read", "update", "delete"
            ),
            "ADMIN", List.of(
                "dashboard.access", "statistics.access", "statistics.global", "statistics.partners", "statistics.clients",
                "statistics.finance", "payment.access", "payment.business", "payment.delivery", "partners.access",
                "delivery.access", "associations.access", "clients.access", "crm.access", "crm.prospects",
                "crm.requests", "marketing.access", "marketing.coupons", "marketing.notifications", "hr.access",
                "website.access", "website.blog", "website.press", "settings.access", "settings.products",
                "settings.categories", "create", "read", "update"
            ),
            "MANAGER", List.of(
                "dashboard.access", "statistics.access", "statistics.global", "statistics.partners", "partners.access",
                "delivery.access", "clients.access", "crm.access", "crm.prospects", "hr.access", "hr.permissions",
                "read", "update"
            ),
            "MANAGER_REGIONALE", List.of(
                "dashboard.access", "statistics.access", "partners.access", "clients.access", "read", "update"
            ),
            "SALES_MANAGER", List.of(
                "dashboard.access", "clients.access", "crm.access", "crm.prospects", "read", "update"
            ),
            "COLLABORATOR", List.of(
                "dashboard.access", "partners.access", "clients.access", "hr.access", "read"
            ),
            "DELIVERY_MAN", List.of(
                "dashboard.access", "delivery.access", "read"
            ),
            "LEAD", List.of(
                "dashboard.access", "clients.access", "read"
            ),
            "CLIENT", List.of(
                "read"
            )
        );

        Map<String, String> labels = Map.of(
            "SUPER_ADMIN", "Super Administrateur",
            "ADMIN", "Administrateur",
            "MANAGER", "Responsable",
            "MANAGER_REGIONALE", "Responsable Régional",
            "SALES_MANAGER", "Responsable Commercial",
            "COLLABORATOR", "Employé",
            "DELIVERY_MAN", "Livreur",
            "LEAD", "Lead",
            "CLIENT", "Client"
        );

        if (!rolePermissions.containsKey(roleKey)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rôle non trouvé : " + roleKey);
        }

        return RoleBasePermissionsDto.builder()
            .role(roleKey)
            .label(labels.getOrDefault(roleKey, roleKey))
            .basePermissions(rolePermissions.get(roleKey))
            .build();
    }

}