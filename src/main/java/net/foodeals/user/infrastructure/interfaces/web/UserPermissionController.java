package net.foodeals.user.infrastructure.interfaces.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.foodeals.user.application.dtos.requests.BulkPermissionUpdateRequest;
import net.foodeals.user.application.dtos.requests.PermissionUpdateRequest;
import net.foodeals.user.application.dtos.requests.UserPermissionDto;
import net.foodeals.user.application.dtos.requests.UserWithPermissionsDto;
import net.foodeals.user.application.dtos.responses.BulkPermissionUpdateResponse;
import net.foodeals.user.application.services.UserPermissionService;

@RestController
@RequestMapping("v1/users-permissions")
@RequiredArgsConstructor
public class UserPermissionController {

    private final UserPermissionService service;

    @PutMapping("/{userId}/permissions")
    public ResponseEntity<UserPermissionDto> updatePermission(
        @PathVariable Integer userId,
        @RequestBody PermissionUpdateRequest request) {
        UserPermissionDto dto = service.updatePermission(userId, request.permission(), request.isGranted());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserWithPermissionsDto> getUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(service.getUserWithPermissions(userId));
    }

    @PutMapping("/{userId}/permissions/bulk")
    public ResponseEntity<BulkPermissionUpdateResponse> bulkUpdate(
        @PathVariable Integer userId,
        @RequestBody BulkPermissionUpdateRequest request) {
        return ResponseEntity.ok(
            service.bulkUpdate(userId, request.permissions()));
    }
}
