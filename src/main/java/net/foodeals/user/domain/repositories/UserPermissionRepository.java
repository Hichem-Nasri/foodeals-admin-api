package net.foodeals.user.domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.user.domain.entities.UserPermission;

public interface UserPermissionRepository extends BaseRepository<UserPermission, UUID> {
	
	List<UserPermission> findByUserId(Integer userId);

    List<UserPermission> findByUserIdAndPermission(Integer userId, String permission);

    void deleteByUserIdAndPermission(Integer userId, String permission);

}
