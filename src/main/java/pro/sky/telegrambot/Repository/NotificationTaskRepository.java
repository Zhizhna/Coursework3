package pro.sky.telegrambot.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.NotificationModel;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationTaskRepository extends JpaRepository<NotificationModel, Long> {

    List<NotificationModel> findAllByNotificationDateTime(LocalDateTime notificationDateTime);
}
