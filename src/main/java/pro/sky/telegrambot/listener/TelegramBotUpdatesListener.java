package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.Repository.NotificationTaskRepository;
import pro.sky.telegrambot.Service.TelegramBotSender;
import pro.sky.telegrambot.model.NotificationModel;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final Pattern INCOMING_MESSAGE_PATTERN = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");
    private final DateTimeFormatter NOTIFICATION_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final TelegramBot telegramBot;

    private final TelegramBotSender telegramBotSender;

    private final NotificationTaskRepository notificationTaskRepository;

    private final String WELCOME_MESSAGE = "Welcome!";
    private final String SUCCESSFULLY_SAVED_RESPONSE = "Message saved";

    public TelegramBotUpdatesListener(TelegramBot telegramBot, TelegramBotSender telegramBotSender, NotificationTaskRepository notificationTaskRepository) {
        this.telegramBot = telegramBot;
        this.telegramBotSender = telegramBotSender;
        this.notificationTaskRepository = notificationTaskRepository;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);

            String message = update.message().text();
            Long chatId = update.message().chat().id();

            if (message.equals("/start")) {
                logger.info("Initial message: {}", message);
                telegramBotSender.send(chatId, WELCOME_MESSAGE);
            } else {
                Matcher matcher = INCOMING_MESSAGE_PATTERN.matcher(message);
                if (matcher.matches()) {
                    logger.info("New Message: {}", message);

                    String rawDateTime = matcher.group(1);
                    String notificationText = matcher.group(3);

                    NotificationModel notificationTask = new NotificationModel(
                            chatId,
                            notificationText,
                            LocalDateTime.parse(rawDateTime, NOTIFICATION_DATE_TIME_FORMAT)
                    );

                    notificationTaskRepository.save(notificationTask);

                    telegramBotSender.send(chatId, SUCCESSFULLY_SAVED_RESPONSE);
                }
            }

        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}
