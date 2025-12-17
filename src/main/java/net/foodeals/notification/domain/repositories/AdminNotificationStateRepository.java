package net.foodeals.notification.domain.repositories;


import java.time.Instant;
import java.util.*;

import net.foodeals.notification.domain.entity.AdminNotificationState;
import net.foodeals.notification.domain.enums.AdminNotificationType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface AdminNotificationStateRepository extends JpaRepository<AdminNotificationState, UUID> {

    Optional<AdminNotificationState> findByNotificationIdAndAdminId(UUID notificationId, Integer adminId);

    @Query("""
      select count(s) from AdminNotificationState s
      where s.admin.id = :adminId and s.isRead = false
    """)
    long countUnread(@Param("adminId") Integer adminId);

    @Query("""
      select s.notification.type as type, count(s) as cnt
      from AdminNotificationState s
      where s.admin.id = :adminId and s.isRead = false
      group by s.notification.type
    """)
    List<Object[]> countUnreadByType(@Param("adminId") Integer adminId);

    @Modifying
    @Query("""
      update AdminNotificationState s
      set s.isRead = true, s.readAt = CURRENT_TIMESTAMP
      where s.admin.id = :adminId
        and s.isRead = false
        and (:type is null or s.notification.type = :type)
    """)
    int markAllRead(@Param("adminId") Integer adminId, @Param("type") AdminNotificationType type);

    @Modifying
    @Query("""
      delete from AdminNotificationState s
      where s.admin.id = :adminId and s.notification.id in :notificationIds
    """)
    int deleteStates(@Param("adminId") Integer adminId, @Param("notificationIds") List<UUID> notificationIds);
}
