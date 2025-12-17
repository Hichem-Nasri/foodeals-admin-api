package net.foodeals.user.domain.repositories;

import java.util.List;
import java.util.UUID;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.user.domain.entities.UserPersonalDocument;

public interface UserPersonalDocumentRepository extends BaseRepository<UserPersonalDocument, UUID> {

	List<UserPersonalDocument> findByUserId(Integer id);

}
