package net.foodeals.notification.domain.repositories;

import java.util.UUID;



import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.notification.domain.entity.Notification;

public interface NotificationRepository  extends  BaseRepository<Notification, UUID> {

}
