package pro.sky.telegrambot.model;

import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class NotificationModel {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private Long chatId;

        private String message;

        private LocalDateTime notificationDateTime;

        public NotificationModel() {
        }

        public NotificationModel(Long chatId, String message, LocalDateTime notificationDateTime) {
            this.chatId = chatId;
            this.message = message;
            this.notificationDateTime = notificationDateTime;
        }

        public Long getId() {
            return id;
        }

        public Long getChatId() {
            return chatId;
        }

        public String getMessage() {
            return message;
        }

        public LocalDateTime getNotificationDateTime() {
            return notificationDateTime;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            NotificationModel that = (NotificationModel) o;
            return Objects.equals(id, that.id) && Objects.equals(chatId, that.chatId) && Objects.equals(message, that.message) && Objects.equals(notificationDateTime, that.notificationDateTime);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, chatId, message, notificationDateTime);
        }

        @Override
        public String toString() {
            return "NotificationTask{" +
                    "id=" + id +
                    ", chatId=" + chatId +
                    ", message='" + message + '\'' +
                    ", notificationDateTime=" + notificationDateTime +
                    '}';
        }
    }
