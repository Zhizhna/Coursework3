package pro.sky.telegrambot.notif;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.Repository.NotificationTaskRepository;
import pro.sky.telegrambot.Service.TelegramBotSender;
import pro.sky.telegrambot.model.NotificationModel;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class NotSender {

    private final Logger logger = LoggerFactory.getLogger(NotSender.class);

    private final NotificationTaskRepository notificationTaskRepository;

    private final TelegramBotSender telegramBotSender;

    public NotSender(NotificationTaskRepository notificationTaskRepository, TelegramBotSender telegramBotSender) {
        this.notificationTaskRepository = notificationTaskRepository;
        this.telegramBotSender = telegramBotSender;
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void sendNotifications() {

        LocalDateTime currentDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        logger.info("Notification job started for date time {}", currentDateTime);

        List<NotificationModel> allByNotificationDateTime = notificationTaskRepository.findAllByNotificationDateTime(currentDateTime);
        logger.info("Notification job has found {} relevant notifications", allByNotificationDateTime.size());

        for (NotificationModel notificationTask : allByNotificationDateTime) {
            logger.info("Processing notification task {}", notificationTask);

            telegramBotSender.send(
                    notificationTask.getChatId(),
                    "Notification! " + notificationTask.getMessage()
            );
        }

        logger.info("Notification job finished for date time {}", currentDateTime);
    }
}