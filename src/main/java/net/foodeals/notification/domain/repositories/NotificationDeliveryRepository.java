package net.foodeals.notification.domain.repositories;

import java.util.List;
import java.util.UUID;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.notification.domain.entity.NotificationDelivery;

public interface NotificationDeliveryRepository  extends  BaseRepository<NotificationDelivery, UUID>  {

	List<NotificationDelivery> findByNotificationId(UUID id);
}
