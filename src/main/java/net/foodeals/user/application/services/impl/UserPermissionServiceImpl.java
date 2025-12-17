package net.foodeals.user.application.services.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import net.foodeals.user.application.dtos.requests.PermissionChangeDto;
import net.foodeals.user.application.dtos.requests.UserPermissionDto;
import net.foodeals.user.application.dtos.requests.UserWithPermissionsDto;
import net.foodeals.user.application.dtos.responses.BulkPermissionUpdateResponse;
import net.foodeals.user.application.dtos.responses.PermissionChangeResult;
import net.foodeals.user.application.services.UserPermissionService;
import net.foodeals.user.domain.entities.User;
import net.foodeals.user.domain.entities.UserPermission;
import net.foodeals.user.domain.repositories.UserPermissionRepository;
import net.foodeals.user.domain.repositories.UserRepository;

@Service
@AllArgsConstructor
public class UserPermissionServiceImpl implements UserPermissionService {
	
    private final UserRepository userRepository;
    private final UserPermissionRepository permissionRepository;
    
	@Override
	@Transactional
	public UserPermissionDto updatePermission(Integer userId, String permission, boolean isGranted) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
       
		Optional<UserPermission> existing=null;
		if( permissionRepository.findByUserIdAndPermission(userId, permission).size()>0){
			existing=Optional.of(permissionRepository.findByUserIdAndPermission(userId, permission)
					.get(0));
		}
		UserPermission perm=null;
		if (isGranted) {
			if(existing==null) {
				perm = new UserPermission();	
			}
			else {
				perm=existing.get();
			}
			perm.setUser(user);
			perm.setPermission(permission);
			perm.setGranted(true);
			perm.setUpdatedAt(Instant.now());
			return map(permissionRepository.save(perm));
		} else {
			existing.ifPresent(permissionRepository::delete);
			return new UserPermissionDto(userId, permission, false, Instant.now());
		}
	}

	@Override
	@Transactional
	public UserWithPermissionsDto getUserWithPermissions(Integer userId) {
	    User user = userRepository.findById(userId).orElseThrow();
	    List<UserPermission> perms = permissionRepository.findByUserId(userId);

	    return new UserWithPermissionsDto(
	        user.getId(),
	        user.getName().firstName(),
	        user.getName().lastName(),
	        user.getEmail(),
	        user.getRole().getName(),
	        user.getStatus().name(),
	        perms.stream().map(UserPermission::getPermission).toList(),
	        user.getCreatedAt(),
	        user.getUpdatedAt(),
	        null
	    );
	}

	@Override
	@Transactional
	public BulkPermissionUpdateResponse bulkUpdate(Integer userId, List<PermissionChangeDto> changes) {
	    List<PermissionChangeResult> result = new ArrayList();

	    for (PermissionChangeDto dto : changes) {
	        boolean exists = permissionRepository.findByUserIdAndPermission(userId, dto.permission()).size()>0;
	        String action;

	        if (dto.isGranted()) {
	            if (!exists) {
	                updatePermission(userId, dto.permission(), true);
	                action = "added";
	            } else {
	                action = "unchanged";
	            }
	        } else {
	            if (exists) {
	                permissionRepository.deleteByUserIdAndPermission(userId, dto.permission());
	                action = "removed";
	            } else {
	                action = "unchanged";
	            }
	        }

	        result.add(new PermissionChangeResult(dto.permission(), dto.isGranted(), action));
	    }

	    return new BulkPermissionUpdateResponse(userId, result, Instant.now());
	}

	private UserPermissionDto map(UserPermission entity) {
	    return new UserPermissionDto(
	        entity.getUser().getId(),
	        entity.getPermission(),
	        entity.isGranted(),
	        entity.getUpdatedAt()
	    );
	}


}
