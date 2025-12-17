package net.foodeals.user.domain.repositories;

import java.util.List;
import java.util.UUID;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.user.domain.entities.Absence;
import net.foodeals.user.domain.entities.UserInternalDocument;

public interface AbsenceRepository extends BaseRepository<Absence, UUID> {

	List<Absence> findByUserId(Integer id);

}
