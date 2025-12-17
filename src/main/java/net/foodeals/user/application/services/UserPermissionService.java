package net.foodeals.user.application.services;

import java.util.List;

import net.foodeals.user.application.dtos.requests.PermissionChangeDto;
import net.foodeals.user.application.dtos.requests.UserPermissionDto;
import net.foodeals.user.application.dtos.requests.UserWithPermissionsDto;
import net.foodeals.user.application.dtos.responses.BulkPermissionUpdateResponse;

public interface UserPermissionService {
	
	public UserPermissionDto updatePermission(Integer userId, String permission, boolean isGranted);
	
	public UserWithPermissionsDto getUserWithPermissions(Integer userId);
	
	public BulkPermissionUpdateResponse bulkUpdate(Integer userId, List<PermissionChangeDto> changes) ;

}
