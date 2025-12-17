package net.foodeals.user.infrastructure.interfaces.web;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.foodeals.user.application.dtos.responses.RoleBasePermissionsDto;
import net.foodeals.user.application.services.RoleService;

@RestController
@RequestMapping("v1/roles-permissions")
@RequiredArgsConstructor

public class RolePermissionController {

	 private final RoleService roleService;
	@GetMapping("/{roleKey}/permissions")
	public ResponseEntity<?> getRolePermissions(@PathVariable String roleKey) {
		RoleBasePermissionsDto dto = roleService.getRolePermissions(roleKey);
		return ResponseEntity.ok(Map.of("success", true, "data", dto));
	}

}
