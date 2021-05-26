package stochi.app.web.rest;

import static stochi.app.security.SecurityUtils.getCurrentUserLoginn;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import stochi.app.domain.Notification;
import stochi.app.domain.User;
import stochi.app.repository.CategoryRepository;
import stochi.app.repository.NotificationRepository;
import stochi.app.repository.UserRepository;
import stochi.app.service.NotificationService;
import stochi.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link stochi.app.domain.Notification}.
 */
@RestController
@RequestMapping("/api")
public class NotificationResource {

    private final Logger log = LoggerFactory.getLogger(NotificationResource.class);

    private static final String ENTITY_NAME = "notification";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NotificationService notificationService;

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public NotificationResource(
        NotificationService notificationService,
        NotificationRepository notificationRepository,
        UserRepository userRepository,
        CategoryRepository categoryRepository
    ) {
        this.notificationService = notificationService;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * {@code POST  /notifications} : Create a new notification.
     *
     * @param notification the notification to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new notification, or with status {@code 400 (Bad Request)} if the notification has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/notifications")
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification) throws URISyntaxException {
        log.debug("REST request to save Notification : {}", notification);
        if (notification.getId() != null) {
            throw new BadRequestAlertException("A new notification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Notification result = notificationService.save(notification);
        return ResponseEntity
            .created(new URI("/api/notifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /notifications/:id} : Updates an existing notification.
     *
     * @param id the id of the notification to save.
     * @param notification the notification to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notification,
     * or with status {@code 400 (Bad Request)} if the notification is not valid,
     * or with status {@code 500 (Internal Server Error)} if the notification couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/notifications/{id}")
    public ResponseEntity<Notification> updateNotification(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Notification notification
    ) throws URISyntaxException {
        log.debug("REST request to update Notification : {}, {}", id, notification);
        if (notification.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notification.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Notification result = notificationService.save(notification);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, notification.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /notifications/:id} : Partial updates given fields of an existing notification, field will ignore if it is null
     *
     * @param id the id of the notification to save.
     * @param notification the notification to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notification,
     * or with status {@code 400 (Bad Request)} if the notification is not valid,
     * or with status {@code 404 (Not Found)} if the notification is not found,
     * or with status {@code 500 (Internal Server Error)} if the notification couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/notifications/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Notification> partialUpdateNotification(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Notification notification
    ) throws URISyntaxException {
        log.debug("REST request to partial update Notification partially : {}, {}", id, notification);
        if (notification.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notification.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Notification> result = notificationService.partialUpdate(notification);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, notification.getId())
        );
    }

    /**
     * {@code GET  /notifications} : get all the notifications.
     *
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notifications in body.
     */
    @GetMapping("/notifications")
    public List<Notification> getAllNotifications() {
        log.debug("REST request to get a page of Notifications");
        List<Notification> var = notificationRepository.findByUserLogin(getCurrentUserLoginn());
        return var;
    }

    /**
     * {@code GET  /notifications/:id} : get the "id" notification.
     *
     * @param id the id of the notification to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notification, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/notifications/{id}")
    public ResponseEntity<Notification> getNotification(@PathVariable String id) {
        log.debug("REST request to get Notification : {}", id);
        Optional<Notification> notification = notificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(notification);
    }

    /**
     * {@code DELETE  /notifications/:id} : delete the "id" notification.
     *
     * @param id the id of the notification to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/notifications/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable String id) {
        log.debug("REST request to delete Notification : {}", id);
        notificationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }

    //private displaynotifs
    @GetMapping("/notifications/displaynotif")
    public String displaynotifications() {
        List<Notification> notications = notificationRepository.findByUserLogin(getCurrentUserLoginn());
        List<LocalDate> dates = new ArrayList<LocalDate>();
        List<LocalDate> finale = new ArrayList<LocalDate>();
        for (Notification notif : notications) {
            if (notif.getTime() != null) dates.add(notif.getTime());
        }
        for (LocalDate element : dates) {
            // If this element is not present in newList
            // then add it
            if (!finale.contains(element)) {
                finale.add(element);
            }
        }
        JSONObject jo = new JSONObject();

        try {
            for (LocalDate date : finale) {
                JSONArray dataR = new JSONArray();
                List<Notification> notif = notificationRepository.findByUserLoginAndTime(getCurrentUserLoginn(), date);
                try {
                    for (Notification notif1 : notif) {
                        JSONObject jo1 = new JSONObject();
                        jo1.put("name", notif1.getType());
                        jo1.put("montant", notif1.getAmount());
                        jo1.put("category", notif1.getCategoryName());
                        jo1.put(
                            "nameIcon",
                            categoryRepository
                                .findOneByUserLoginAndNameCatego(getCurrentUserLoginn(), notif1.getCategoryName())
                                .getNameIcon()
                        );
                        jo1.put(
                            "color",
                            categoryRepository.findOneByUserLoginAndNameCatego(getCurrentUserLoginn(), notif1.getCategoryName()).getColor()
                        );
                        dataR.put(jo1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                jo.put(String.valueOf(date), dataR);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo.toString();
    }

    @Scheduled(cron = "0 0 7 * * *", zone = "Africa/Tunis")
    public void Scheduled_task2() {
        System.out.println("This is a Scheduled task to delete Notifications which they are older then 15 days");

        List<User> listaa = userRepository.findAll();
        for (User user : listaa) {
            List<Notification> notifs = notificationRepository.findByUserLogin(user.getLogin());

            for (Notification notif : notifs) {
                if (notif.getTime().isBefore(LocalDate.now().minusDays(15))) {
                    notificationService.delete(notif.getId());
                }
            }
        }
    }
}
