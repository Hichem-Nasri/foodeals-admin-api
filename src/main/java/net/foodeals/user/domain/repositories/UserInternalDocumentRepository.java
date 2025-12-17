package net.foodeals.user.domain.repositories;

import java.util.List;
import java.util.UUID;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.user.domain.entities.UserInternalDocument;

public interface UserInternalDocumentRepository extends BaseRepository<UserInternalDocument, UUID> {

	List<UserInternalDocument> findByUserId(Integer id);

}
