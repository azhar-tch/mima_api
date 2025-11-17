package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.Notifications;
import com.helpysoft.mima_api.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotificationsRepository extends JpaRepository<Notifications, Long> {

    @Query("SELECT n FROM Notifications n WHERE n.trackingId = :trackingId")
    Optional<Notifications> findByTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT n FROM Notifications n WHERE n.recipient = :recipient ORDER BY n.createDate DESC")
    List<Notifications> findByRecipient(@Param("recipient") Users recipient);

    @Query("SELECT n FROM Notifications n WHERE n.isRead = :isRead ORDER BY n.createDate DESC")
    List<Notifications> findByIsRead(@Param("isRead") Boolean isRead);

    @Query("SELECT n FROM Notifications n ORDER BY n.createDate DESC")
    List<Notifications> findAllOrderByCreateDateDesc();

    @Query("SELECT n FROM Notifications n WHERE n.recipient = :recipient AND n.isRead = :isRead ORDER BY n.createDate DESC")
    List<Notifications> findByRecipientAndIsRead(@Param("recipient") Users recipient, @Param("isRead") Boolean isRead);

    @Query("SELECT n FROM Notifications n WHERE n.notificationType = :notificationType ORDER BY n.createDate DESC")
    List<Notifications> findByNotificationType(@Param("notificationType") String notificationType);
}
