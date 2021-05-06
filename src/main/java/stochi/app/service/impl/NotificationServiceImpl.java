package stochi.app.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import stochi.app.domain.Notification;
import stochi.app.repository.NotificationRepository;
import stochi.app.service.NotificationService;

/**
 * Service Implementation for managing {@link Notification}.
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    private final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Notification save(Notification notification) {
        log.debug("Request to save Notification : {}", notification);
        return notificationRepository.save(notification);
    }

    @Override
    public Optional<Notification> partialUpdate(Notification notification) {
        log.debug("Request to partially update Notification : {}", notification);

        return notificationRepository
            .findById(notification.getId())
            .map(
                existingNotification -> {
                    if (notification.getAmount() != null) {
                        existingNotification.setAmount(notification.getAmount());
                    }
                    if (notification.getUserLogin() != null) {
                        existingNotification.setUserLogin(notification.getUserLogin());
                    }
                    if (notification.getCategoryName() != null) {
                        existingNotification.setCategoryName(notification.getCategoryName());
                    }
                    if (notification.getTime() != null) {
                        existingNotification.setTime(notification.getTime());
                    }
                    if (notification.getType() != null) {
                        existingNotification.setType(notification.getType());
                    }

                    return existingNotification;
                }
            )
            .map(notificationRepository::save);
    }

    @Override
    public Page<Notification> findAll(Pageable pageable) {
        log.debug("Request to get all Notifications");
        return notificationRepository.findAll(pageable);
    }

    @Override
    public Optional<Notification> findOne(String id) {
        log.debug("Request to get Notification : {}", id);
        return notificationRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Notification : {}", id);
        notificationRepository.deleteById(id);
    }
}
