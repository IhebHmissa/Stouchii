package stochi.app.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import stochi.app.domain.Notification;

/**
 * Spring Data MongoDB repository for the Notification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByUserLogin(String login);
    List<Notification> findByUserLoginAndTime(String logi, LocalDate date);
}
