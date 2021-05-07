package stochi.app.web.rest;

import static stochi.app.security.SecurityUtils.getCurrentUserLoginn;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import stochi.app.domain.Category;
import stochi.app.domain.HistoryLine;
import stochi.app.domain.Notification;
import stochi.app.domain.User;
import stochi.app.repository.CategoryRepository;
import stochi.app.repository.HistoryLineRepository;
import stochi.app.repository.NotificationRepository;
import stochi.app.repository.UserRepository;
import stochi.app.service.CategoryService;
import stochi.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link stochi.app.domain.Category}.
 */
@RestController
@RequestMapping("/api")
public class CategoryResource {

    private final Logger log = LoggerFactory.getLogger(CategoryResource.class);

    private static final String ENTITY_NAME = "category";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CategoryService categoryService;

    private final CategoryRepository categoryRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final HistoryLineRepository historyLineRepository;

    @Autowired
    private final NotificationRepository notificationRepository;

    public CategoryResource(
        CategoryService categoryService,
        CategoryRepository categoryRepository,
        UserRepository userRepository,
        HistoryLineRepository historyLineRepository,
        NotificationRepository notificationRepository
    ) {
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.historyLineRepository = historyLineRepository;
        this.notificationRepository = notificationRepository;
    }

    /**
     * {@code POST  /categories} : Create a new category.
     *
     * @param category the category to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new category, or with status {@code 400 (Bad Request)} if the category has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/categories")
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) throws URISyntaxException {
        category.setUserLogin(getCurrentUserLoginn());

        System.out.println(categoryRepository.findByUserLoginAndNameCatego(getCurrentUserLoginn(), category.getNameCatego()));

        if (!categoryRepository.findByUserLoginAndNameCatego(getCurrentUserLoginn(), category.getNameCatego()).isEmpty()) {
            throw new BadRequestAlertException(
                "A new category cannot have the same name as existing category",
                ENTITY_NAME,
                " catego exists"
            );
        }
        Category result = categoryService.save(category);
        return ResponseEntity
            .created(new URI("/api/categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    private float calculSommeCategori(String login, String nomcatego) {
        float totale = 0;
        List<Category> listaa = categoryRepository.findByUserLoginAndOriginType(login, nomcatego);
        for (Category cat : listaa) {
            totale = totale + cat.getMontant();
        }
        return totale;
    }

    /**
     * {@code PUT  /categories/:id} : Updates an existing category.
     *
     * @param nomcatego the id of the category to save.
     * @param category the category to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated category,
     * or with status {@code 400 (Bad Request)} if the category is not valid,
     * or with status {@code 500 (Internal Server Error)} if the category couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/categories/{nomcatego}")
    public Category updateCategory(
        @PathVariable(value = "nomcatego", required = false) final String nomcatego,
        @Valid @RequestBody Category category
    ) throws URISyntaxException {
        log.debug("REST request to update Category : {}, {}", nomcatego, category);
        if (category.getNameCatego() == null) {
            throw new BadRequestAlertException("Invalid name of catego", ENTITY_NAME, "Name is null");
        }
        if (!Objects.equals(nomcatego, category.getNameCatego())) {
            throw new BadRequestAlertException("Invalid name of categ", ENTITY_NAME, "Name of catego invalid");
        }
        System.out.println();
        if (categoryRepository.findOneByUserLoginAndNameCatego(getCurrentUserLoginn(), nomcatego) == null) {
            throw new BadRequestAlertException("There is no categ with that name", ENTITY_NAME, "Catego not found");
        }
        Category catNEW = categoryRepository.findOneByUserLoginAndNameCatego(getCurrentUserLoginn(), category.getNameCatego());
        System.out.println(catNEW);

        if (category.getColor() != null) catNEW.setColor(category.getColor());
        if (category.getMaxMontant() != null) catNEW.setMaxMontant(category.getMaxMontant());
        if (category.getMinMontant() != null) catNEW.setMinMontant(category.getMinMontant());
        if (category.getAverage() != null) catNEW.setAverage(category.getAverage());
        if (category.getNameCatego() != null) catNEW.setNameCatego(category.getNameCatego());
        if (category.getNameIcon() != null) catNEW.setNameIcon(category.getNameIcon());
        if (category.getDest() != null) catNEW.setDest(category.getDest());
        if (category.getModifDate() != null) catNEW.setModifDate(category.getModifDate()); //
        if (category.getPeriodictyy() != null) {
            catNEW.setPeriodictyy(category.getPeriodictyy());
            if (catNEW.getPeriodictyy().getDateFin() != null) {
                if (catNEW.getPeriodictyy().getFrequancy().equals("semaine")) catNEW.setperiodicity(0L);
                if (catNEW.getPeriodictyy().getFrequancy().equals("2semaine")) catNEW.setperiodicity(0L);
                if (catNEW.getPeriodictyy().getFrequancy().equals("trimestre")) catNEW.setperiodicity(0L);
                if (catNEW.getPeriodictyy().getFrequancy().equals("semestre")) catNEW.setperiodicity(0L);
                if (catNEW.getPeriodictyy().getFrequancy().equals("annee")) catNEW.setperiodicity(0L);
            } else catNEW.setperiodicity(0L);
            System.out.println(category.getPeriodictyy());
        }

        if (category.getMontant() != null) {
            float montan = category.getMontant();
            System.out.println(montan);
            if (!catNEW.getOriginType().equals("Catego")) {
                Category Categooo = categoryRepository.findOneByUserLoginAndNameCatego(getCurrentUserLoginn(), catNEW.getOriginType());
                if (Categooo.getMontant() != null) Categooo.setMontant(
                    Categooo.getMontant() + category.getMontant()
                ); else Categooo.setMontant(category.getMontant());
                categoryRepository.save(Categooo);
                System.out.println(Categooo);
            }
            if (catNEW.getMontant() != null) catNEW.setMontant(category.getMontant() + catNEW.getMontant()); else catNEW.setMontant(
                category.getMontant()
            );
            System.out.println("from here ?");
            Optional<User> constants = userRepository.findOneByLogin(catNEW.getUserLogin());
            System.out.println(constants);
            User value = constants.orElseThrow(() -> new RuntimeException("No such data found"));
            System.out.println(value);
            System.out.println("tak tak ?");
            if (catNEW.getType().equals("Depense")) value.setSoldeUser(value.getSoldeUser() - category.getMontant());
            if (catNEW.getType().equals("Revenus")) value.setSoldeUser(value.getSoldeUser() + category.getMontant());
            System.out.println(value);
            userRepository.save(value);
            System.out.println(value);

            // History part !

            if ((category.getModifDate() != null) & (category.getNote() != null)) {
                HistoryLine hist = new HistoryLine(
                    catNEW.getNameCatego(),
                    category.getModifDate(),
                    montan,
                    catNEW.getUserLogin(),
                    catNEW.getType(),
                    value.getSoldeUser(),
                    category.getNote()
                );
                historyLineRepository.save(hist);
                System.out.println(hist);
            } else if ((category.getModifDate() == null) & (category.getNote() != null)) {
                HistoryLine hist = new HistoryLine(
                    catNEW.getNameCatego(),
                    ZonedDateTime.now(),
                    montan,
                    catNEW.getUserLogin(),
                    catNEW.getType(),
                    value.getSoldeUser(),
                    category.getNote()
                );
                historyLineRepository.save(hist);
                System.out.println(hist);
            } else if ((category.getModifDate() != null) & (category.getNote() == null)) {
                HistoryLine hist = new HistoryLine(
                    catNEW.getNameCatego(),
                    category.getModifDate(),
                    montan,
                    catNEW.getUserLogin(),
                    catNEW.getType(),
                    value.getSoldeUser()
                );
                historyLineRepository.save(hist);
                System.out.println(hist);
            } else {
                HistoryLine hist = new HistoryLine(
                    catNEW.getNameCatego(),
                    ZonedDateTime.now(),
                    montan,
                    catNEW.getUserLogin(),
                    catNEW.getType(),
                    value.getSoldeUser()
                );
                historyLineRepository.save(hist);
                System.out.println(hist);
            }

            // Notificiation part !
            if (
                catNEW.getAverage() != null &
                categoryRepository.findOneByUserLoginAndNameCatego(getCurrentUserLoginn(), "Salary").getPeriodictyy().getFixedMontant() !=
                null
            ) {
                Float salary = categoryRepository
                    .findOneByUserLoginAndNameCatego(getCurrentUserLoginn(), "Salary")
                    .getPeriodictyy()
                    .getFixedMontant();

                if (
                    (catNEW.getMontant() > 0.9 * salary * catNEW.getAverage()) & (catNEW.getMontant() < 0.95 * salary * catNEW.getAverage())
                ) {
                    Notification notif = new Notification(
                        catNEW.getMontant(),
                        value.getLogin(),
                        catNEW.getNameCatego(),
                        ZonedDateTime.now(),
                        "90% exceeded"
                    );
                    notificationRepository.save(notif);
                    System.out.println(notif);
                } else if (
                    (catNEW.getMontant() > 0.95 * salary * catNEW.getAverage()) & (catNEW.getMontant() < salary * catNEW.getAverage())
                ) {
                    Notification notif = new Notification(
                        catNEW.getMontant(),
                        value.getLogin(),
                        catNEW.getNameCatego(),
                        ZonedDateTime.now(),
                        "95% exceeded"
                    );
                    notificationRepository.save(notif);
                    System.out.println(notif);
                } else if (catNEW.getMontant() > salary * catNEW.getAverage()) {
                    Notification notif = new Notification(
                        catNEW.getMontant(),
                        value.getLogin(),
                        catNEW.getNameCatego(),
                        ZonedDateTime.now(),
                        "100% exceeded"
                    );
                    notificationRepository.save(notif);
                    System.out.println(notif);
                }
            }
        }

        Category result = categoryService.save(catNEW);
        return result;
    }

    /**
     * {@code PATCH  /categories/:id} : Partial updates given fields of an existing category, field will ignore if it is null
     *
     * @param id the id of the category to save.
     * @param category the category to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated category,
     * or with status {@code 400 (Bad Request)} if the category is not valid,
     * or with status {@code 404 (Not Found)} if the category is not found,
     * or with status {@code 500 (Internal Server Error)} if the category couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/categories/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Category> partialUpdateCategory(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody Category category
    ) throws URISyntaxException {
        log.debug("REST request to partial update Category partially : {}, {}", id, category);
        if (category.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, category.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Category> result = categoryService.partialUpdate(category);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, category.getId())
        );
    }

    /**
     * {@code GET  /categories} : get all the categories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categories in body.
     */
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories(Pageable pageable) {
        log.debug("REST request to get a page of Categories");
        Page<Category> page = categoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/categories/depenses")
    public List<Category> getDepenseCategories() {
        log.debug("REST request to get a page of Depense Categories");
        List<Category> var = categoryRepository.findByUserLoginAndType(getCurrentUserLoginn(), "Depense");
        return var;
    }

    @GetMapping("/categories/revenus")
    public List<Category> getRevenusCategories() {
        log.debug("REST request to get a page of revenus Categories");
        List<Category> var = categoryRepository.findByUserLoginAndType(getCurrentUserLoginn(), "Revenus");
        return var;
    }

    @GetMapping("/Categories/SousCategories/{nomcatego}")
    public List<Category> getSousCategorie(@PathVariable(value = "nomcatego") String nomcatego) {
        if (categoryRepository.findOneByUserLoginAndNameCatego(getCurrentUserLoginn(), nomcatego) == null) {
            throw new BadRequestAlertException("There is no categ with that name", ENTITY_NAME, "Catego not found");
        }
        return categoryRepository.findByUserLoginAndOriginType(getCurrentUserLoginn(), nomcatego);
    }

    @GetMapping("/Categories/specificCategorie/{nomcatego}")
    public Category getSpecificCategorie(@PathVariable(value = "nomcatego") String nomcatego) {
        if (categoryRepository.findOneByUserLoginAndNameCatego(getCurrentUserLoginn(), nomcatego) == null) {
            throw new BadRequestAlertException("There is no categ with that name", ENTITY_NAME, "Catego not found");
        }
        return categoryRepository.findOneByUserLoginAndNameCatego(getCurrentUserLoginn(), nomcatego);
    }

    /**
     * {@code GET  /categories/:id} : get the "id" category.
     *
     * @param id the id of the category to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the category, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/categories/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable String id) {
        log.debug("REST request to get Category : {}", id);
        Optional<Category> category = categoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(category);
    }

    /**
     * {@code DELETE  /categories/:id} : delete the "id" category.
     *
     * @param nomcatego the id of the category to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/categories/{nomcatego}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String nomcatego) {
        String id = categoryRepository.findOneByUserLoginAndNameCatego(getCurrentUserLoginn(), nomcatego).getId();
        log.debug("REST request to delete Category : {}", nomcatego);
        categoryService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }

    private List<Category> FindAllCategories(String login) {
        return categoryRepository.findByUserLogin(login);
    }

    private void addperodicitysolde(Category catego, User user) {
        catego.setMontant(catego.getMontant() + catego.getPeriodictyy().getFixedMontant());
        categoryRepository.save(catego);
        if (catego.getType().equals("Depense")) user.setSoldeUser(user.getSoldeUser() - catego.getPeriodictyy().getFixedMontant());
        if (catego.getType().equals("Revenus")) user.setSoldeUser(user.getSoldeUser() + catego.getPeriodictyy().getFixedMontant());
        userRepository.save(user);
        HistoryLine hist = new HistoryLine(
            catego.getNameCatego(),
            ZonedDateTime.now(),
            catego.getPeriodictyy().getFixedMontant(),
            catego.getUserLogin(),
            catego.getType(),
            user.getSoldeUser()
        );
        historyLineRepository.save(hist);
        if (catego.getType().equals("Depense")) {
            Notification notif = new Notification(
                catego.getPeriodictyy().getFixedMontant(),
                user.getLogin(),
                catego.getNameCatego(),
                ZonedDateTime.now(),
                "Money out"
            );
            notificationRepository.save(notif);
            System.out.println(notif);
        }
        if (catego.getType().equals("Revenus")) {
            Notification notif = new Notification(
                catego.getPeriodictyy().getFixedMontant(),
                user.getLogin(),
                catego.getNameCatego(),
                ZonedDateTime.now(),
                "Money IN"
            );
            notificationRepository.save(notif);
            System.out.println(notif);
        }
    }

    /*
    Cette fonction se répéte chaque jour a 7h00
    */@Scheduled(cron = "0 0 7 * * *", zone = "Africa/Tunis")
    public void Scheduled_task() {
        System.out.println("This a repeated task");

        List<User> listaa = userRepository.findAll();
        for (User user : listaa) {
            for (Category catego : FindAllCategories(user.getLogin())) {
                if (catego.getPeriodictyy() != null) {
                    System.out.println(catego.getNameCatego());
                    boolean stayin = true;
                    if (catego.getPeriodictyy().getDateFin() != null) {
                        System.out.println("HEY " + catego.getPeriodictyy().getDateFin().isBefore(ZonedDateTime.now()));
                        if (catego.getPeriodictyy().getDateFin().isBefore(ZonedDateTime.now())) {
                            catego.setPeriodictyy(null);
                            System.out.println("This catego periodicity is deleted ");
                            categoryRepository.save(catego);
                            stayin = false;
                        }
                    }
                    if (stayin) {
                        if (
                            catego.getPeriodictyy().getFrequancy().equals("mois") &
                            catego.getPeriodictyy().getDateDeb().isBefore(ZonedDateTime.now())
                        ) {
                            if (ZonedDateTime.now().getDayOfMonth() == catego.getPeriodictyy().getDateDeb().getDayOfMonth()) {
                                if (catego.getPeriodictyy().getDateFin() == null) {
                                    System.out.println("I m here");
                                    addperodicitysolde(catego, user);
                                }
                                if ((catego.getPeriodictyy().getDateFin() != null)) if (
                                    catego.getPeriodictyy().getDateFin().isBefore(ZonedDateTime.now())
                                ) {
                                    System.out.println("I m here2");
                                    addperodicitysolde(catego, user);
                                }
                            }
                        }

                        if (
                            catego.getPeriodictyy().getFrequancy().equals("mois") &
                            catego.getPeriodictyy().getDateDeb().isAfter(ZonedDateTime.now())
                        ) {
                            if (ZonedDateTime.now().getDayOfMonth() == catego.getPeriodictyy().getDateDeb().minusDays(1).getDayOfMonth()) {
                                if (catego.getPeriodictyy().getDateFin() == null) {
                                    Notification notif = new Notification(
                                        catego.getMontant(),
                                        user.getLogin(),
                                        catego.getNameCatego(),
                                        ZonedDateTime.now(),
                                        "1Day left"
                                    );
                                    notificationRepository.save(notif);
                                    System.out.println(notif);
                                }
                                if ((catego.getPeriodictyy().getDateFin() != null)) if (
                                    catego.getPeriodictyy().getDateFin().isBefore(ZonedDateTime.now())
                                ) {
                                    Notification notif = new Notification(
                                        catego.getMontant(),
                                        user.getLogin(),
                                        catego.getNameCatego(),
                                        ZonedDateTime.now(),
                                        "1Day left"
                                    );
                                    notificationRepository.save(notif);
                                    System.out.println(notif);
                                }
                            }
                        }

                        if (
                            catego.getPeriodictyy().getFrequancy().equals("jour") &
                            catego.getPeriodictyy().getDateDeb().isBefore(ZonedDateTime.now())
                        ) {
                            System.out.println(catego.getPeriodictyy().getDateDeb());
                            System.out.println(ZonedDateTime.now());
                            if (catego.getPeriodictyy().getDateFin() == null) addperodicitysolde(catego, user);

                            if (catego.getPeriodictyy().getDateFin() != null) {
                                if (catego.getPeriodictyy().getDateFin().isAfter(ZonedDateTime.now())) addperodicitysolde(catego, user);
                            }
                        }

                        if (
                            catego.getPeriodictyy().getFrequancy().equals("semaine") &
                            catego.getPeriodictyy().getDateDeb().isBefore(ZonedDateTime.now())
                        ) {
                            if ((catego.getPeriodictyy().getDateFin() == null)) {
                                if (catego.getPeriodictyy().getNumberleft() == 0) {
                                    addperodicitysolde(catego, user);
                                    catego.setperiodicity(catego.getPeriodictyy().getNumberleft() + 1);
                                } else if (catego.getPeriodictyy().getNumberleft() == 6) catego.setperiodicity(
                                    0L
                                ); else catego.setperiodicity(catego.getPeriodictyy().getNumberleft() + 1);
                                categoryRepository.save(catego);
                            }
                            if ((catego.getPeriodictyy().getDateFin() != null)) {
                                if (catego.getPeriodictyy().getDateFin().isAfter(ZonedDateTime.now())) {
                                    if (catego.getPeriodictyy().getNumberleft() == 0) {
                                        addperodicitysolde(catego, user);
                                        catego.setperiodicity(catego.getPeriodictyy().getNumberleft() + 1);
                                    } else if (catego.getPeriodictyy().getNumberleft() == 6) catego.setperiodicity(
                                        0L
                                    ); else catego.setperiodicity(catego.getPeriodictyy().getNumberleft() + 1);
                                    categoryRepository.save(catego);
                                }
                            }
                        }
                        if (
                            catego.getPeriodictyy().getFrequancy().equals("2semaine") &
                            catego.getPeriodictyy().getDateDeb().isBefore(ZonedDateTime.now())
                        ) {
                            if ((catego.getPeriodictyy().getDateFin() == null)) {
                                if (catego.getPeriodictyy().getNumberleft() == 0) {
                                    addperodicitysolde(catego, user);
                                    catego.setperiodicity(catego.getPeriodictyy().getNumberleft() + 1);
                                } else if (catego.getPeriodictyy().getNumberleft() == 13) catego.setperiodicity(
                                    0L
                                ); else catego.setperiodicity(catego.getPeriodictyy().getNumberleft() + 1);
                                categoryRepository.save(catego);
                            }
                            if ((catego.getPeriodictyy().getDateFin() != null)) {
                                if (catego.getPeriodictyy().getDateFin().isAfter(ZonedDateTime.now())) {
                                    if (catego.getPeriodictyy().getNumberleft() == 0) {
                                        addperodicitysolde(catego, user);
                                        catego.setperiodicity(catego.getPeriodictyy().getNumberleft() + 1);
                                    } else if (catego.getPeriodictyy().getNumberleft() == 13) catego.setperiodicity(
                                        0L
                                    ); else catego.setperiodicity(catego.getPeriodictyy().getNumberleft() + 1);
                                    categoryRepository.save(catego);
                                }
                            }
                        }

                        if (
                            catego.getPeriodictyy().getFrequancy().equals("trimestre") &
                            catego.getPeriodictyy().getDateDeb().isBefore(ZonedDateTime.now()) &
                            (ZonedDateTime.now().getDayOfMonth() == catego.getPeriodictyy().getDateDeb().getDayOfMonth())
                        ) {
                            if ((catego.getPeriodictyy().getDateFin() == null)) {
                                if (catego.getPeriodictyy().getNumberleft() == 0) {
                                    addperodicitysolde(catego, user);
                                    catego.setperiodicity(catego.getPeriodictyy().getNumberleft() + 1);
                                } else if (catego.getPeriodictyy().getNumberleft() == 2) catego.setperiodicity(
                                    0L
                                ); else catego.setperiodicity(catego.getPeriodictyy().getNumberleft() + 1);
                                categoryRepository.save(catego);
                            }
                            if ((catego.getPeriodictyy().getDateFin() != null)) {
                                if (catego.getPeriodictyy().getDateFin().isAfter(ZonedDateTime.now())) {
                                    if (catego.getPeriodictyy().getNumberleft() == 0) {
                                        addperodicitysolde(catego, user);
                                        catego.setperiodicity(catego.getPeriodictyy().getNumberleft() + 1);
                                    } else if (catego.getPeriodictyy().getNumberleft() == 2) catego.setperiodicity(
                                        0L
                                    ); else catego.setperiodicity(catego.getPeriodictyy().getNumberleft() + 1);
                                    categoryRepository.save(catego);
                                }
                            }
                        }

                        if (
                            catego.getPeriodictyy().getFrequancy().equals("semestre") &
                            catego.getPeriodictyy().getDateDeb().isBefore(ZonedDateTime.now()) &
                            (ZonedDateTime.now().getDayOfMonth() == catego.getPeriodictyy().getDateDeb().getDayOfMonth())
                        ) {
                            if ((catego.getPeriodictyy().getDateFin() == null)) {
                                if (catego.getPeriodictyy().getNumberleft() == 0) {
                                    addperodicitysolde(catego, user);
                                    catego.setperiodicity(catego.getPeriodictyy().getNumberleft() + 1);
                                } else if (catego.getPeriodictyy().getNumberleft() == 2) catego.setperiodicity(
                                    0L
                                ); else catego.setperiodicity(catego.getPeriodictyy().getNumberleft() + 1);
                                categoryRepository.save(catego);
                            }
                            if ((catego.getPeriodictyy().getDateFin() != null)) {
                                if (catego.getPeriodictyy().getDateFin().isAfter(ZonedDateTime.now())) {
                                    if (catego.getPeriodictyy().getNumberleft() == 0) {
                                        addperodicitysolde(catego, user);
                                        catego.setperiodicity(catego.getPeriodictyy().getNumberleft() + 1);
                                    } else if (catego.getPeriodictyy().getNumberleft() == 2) catego.setperiodicity(
                                        0L
                                    ); else catego.setperiodicity(catego.getPeriodictyy().getNumberleft() + 1);
                                    categoryRepository.save(catego);
                                }
                            }
                        }
                        if (
                            catego.getPeriodictyy().getFrequancy().equals("annee") &
                            catego.getPeriodictyy().getDateDeb().isBefore(ZonedDateTime.now())
                        ) {
                            if (ZonedDateTime.now().getDayOfYear() == catego.getPeriodictyy().getDateDeb().getDayOfYear()) {
                                if (catego.getPeriodictyy().getDateFin() == null) {
                                    System.out.println("I m here3");
                                    addperodicitysolde(catego, user);
                                }
                                if ((catego.getPeriodictyy().getDateFin() != null)) if (
                                    catego.getPeriodictyy().getDateFin().isBefore(ZonedDateTime.now())
                                ) {
                                    System.out.println("I m here4");
                                    addperodicitysolde(catego, user);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @GetMapping(value = "/categories/piechartdepense")
    public List<List<Object>> piechartdepense() {
        List<List<Object>> resultat = new ArrayList<List<Object>>();
        List<Category> listecategoDepense = categoryRepository.findByUserLoginAndTypeAndOriginType(
            getCurrentUserLoginn(),
            "Depense",
            "Catego"
        );
        for (Category catego : listecategoDepense) {
            List<Object> listaa = new ArrayList<Object>();

            listaa.add(catego.getNameCatego());
            listaa.add(catego.getMontant());
            listaa.add(false);
            resultat.add(listaa);
        }
        return resultat;
    }

    @GetMapping(value = "/categories/piechartRevenus")
    public List<List<Object>> piechartRevenus() {
        List<List<Object>> resultat = new ArrayList<List<Object>>();
        List<Category> listecategoDepense = categoryRepository.findByUserLoginAndTypeAndOriginType(
            getCurrentUserLoginn(),
            "Revenus",
            "Catego"
        );
        for (Category catego : listecategoDepense) {
            List<Object> listaa = new ArrayList<Object>();

            listaa.add(catego.getNameCatego());
            listaa.add(catego.getMontant());
            listaa.add(false);
            resultat.add(listaa);
        }
        return resultat;
    }
}
