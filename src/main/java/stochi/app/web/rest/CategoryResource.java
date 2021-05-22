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
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
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
        if (category.getMontant() == null) category.setMontant(0F);
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
            if (value.getSoldeuserdepense() == null) value.setSoldeuserdepense(0F);
            if (value.getSolduserrevenus() == null) value.setSolduserrevenus(0F);
            userRepository.save(value);
            System.out.println("Here!!:/");
            System.out.println((category.getModifDate() != null));
            if ((category.getModifDate() != null) || (category.getNote() != null)) {
                if (catNEW.getType().equals("Depense")) {
                    value.setSoldeuserdepense(value.getSoldeuserdepense() + montan);
                    userRepository.save(value);
                    HistoryLine hist = new HistoryLine(
                        catNEW.getNameCatego(),
                        category.getModifDate(),
                        montan,
                        catNEW.getUserLogin(),
                        category.getNote(),
                        catNEW.getType(),
                        value.getSoldeUser(),
                        value.getSoldeuserdepense() + category.getMontant(),
                        catNEW.getOriginType(),
                        catNEW.getNameIcon(),
                        catNEW.getColor()
                    );
                    historyLineRepository.save(hist);
                    System.out.println(hist);
                } else {
                    value.setSolduserrevenus(value.getSolduserrevenus() + montan);
                    userRepository.save(value);
                    HistoryLine hist = new HistoryLine(
                        catNEW.getNameCatego(),
                        category.getModifDate(),
                        montan,
                        catNEW.getUserLogin(),
                        category.getNote(),
                        value.getSolduserrevenus(),
                        catNEW.getType(),
                        value.getSoldeUser(),
                        catNEW.getOriginType(),
                        catNEW.getNameIcon(),
                        catNEW.getColor()
                    );
                    historyLineRepository.save(hist);
                    System.out.println(hist);
                }
            } else if ((category.getModifDate() == null) || (category.getNote() != null)) {
                if (catNEW.getType().equals("Depense")) {
                    value.setSoldeuserdepense(value.getSoldeuserdepense() + montan);
                    userRepository.save(value);
                    HistoryLine hist = new HistoryLine(
                        catNEW.getNameCatego(),
                        montan,
                        catNEW.getUserLogin(),
                        category.getNote(),
                        catNEW.getType(),
                        value.getSoldeUser(),
                        value.getSoldeuserdepense() + category.getMontant(),
                        catNEW.getOriginType(),
                        catNEW.getNameIcon(),
                        catNEW.getColor()
                    );
                    historyLineRepository.save(hist);
                    System.out.println(hist);
                } else {
                    value.setSolduserrevenus(value.getSolduserrevenus() + montan);
                    userRepository.save(value);
                    HistoryLine hist = new HistoryLine(
                        catNEW.getNameCatego(),
                        montan,
                        catNEW.getUserLogin(),
                        category.getNote(),
                        value.getSolduserrevenus(),
                        catNEW.getType(),
                        value.getSoldeUser(),
                        catNEW.getOriginType(),
                        catNEW.getNameIcon(),
                        catNEW.getColor()
                    );
                    historyLineRepository.save(hist);
                    System.out.println(hist);
                }
            } else if ((category.getModifDate() != null) || (category.getNote() == null)) {
                if (catNEW.getType().equals("Depense")) {
                    value.setSoldeuserdepense(value.getSoldeuserdepense() + montan);
                    userRepository.save(value);
                    HistoryLine hist = new HistoryLine(
                        catNEW.getNameCatego(),
                        category.getModifDate(),
                        montan,
                        catNEW.getUserLogin(),
                        catNEW.getType(),
                        value.getSoldeUser(),
                        value.getSoldeuserdepense() + category.getMontant(),
                        catNEW.getOriginType(),
                        catNEW.getNameIcon(),
                        catNEW.getColor()
                    );
                    historyLineRepository.save(hist);
                    System.out.println(hist);
                } else {
                    value.setSolduserrevenus(value.getSolduserrevenus() + montan);
                    userRepository.save(value);
                    HistoryLine hist = new HistoryLine(
                        catNEW.getNameCatego(),
                        category.getModifDate(),
                        montan,
                        catNEW.getUserLogin(),
                        value.getSolduserrevenus(),
                        catNEW.getType(),
                        value.getSoldeUser(),
                        catNEW.getOriginType(),
                        catNEW.getNameIcon(),
                        catNEW.getColor()
                    );
                    historyLineRepository.save(hist);
                    System.out.println(hist);
                }
            } else {
                if (catNEW.getType().equals("Depense")) {
                    value.setSoldeuserdepense(value.getSoldeuserdepense() + montan);
                    userRepository.save(value);
                    HistoryLine hist = new HistoryLine(
                        catNEW.getNameCatego(),
                        montan,
                        catNEW.getUserLogin(),
                        catNEW.getType(),
                        value.getSoldeUser(),
                        value.getSoldeuserdepense() + category.getMontant(),
                        catNEW.getOriginType(),
                        catNEW.getNameIcon(),
                        catNEW.getColor()
                    );
                    historyLineRepository.save(hist);
                    System.out.println(hist);
                } else {
                    value.setSolduserrevenus(value.getSolduserrevenus() + montan);
                    userRepository.save(value);
                    HistoryLine hist = new HistoryLine(
                        catNEW.getNameCatego(),
                        montan,
                        catNEW.getUserLogin(),
                        value.getSolduserrevenus(),
                        catNEW.getType(),
                        value.getSoldeUser(),
                        catNEW.getOriginType(),
                        catNEW.getNameIcon(),
                        catNEW.getColor()
                    );
                    historyLineRepository.save(hist);
                    System.out.println(hist);
                }
            }

            // Notificiation part
            // Notification for the main category
            if (categoryRepository.findOneByUserLoginAndNameCatego(getCurrentUserLoginn(), "Salary").getPeriodictyy() != null) {
                Category catNotif = new Category();
                if (catNEW.getOriginType().equals("Catego")) catNotif = catNEW; else catNotif =
                    categoryRepository.findOneByUserLoginAndNameCatego(getCurrentUserLoginn(), catNEW.getOriginType());
                if (
                    catNotif.getAverage() != null &
                    categoryRepository
                        .findOneByUserLoginAndNameCatego(getCurrentUserLoginn(), "Salary")
                        .getPeriodictyy()
                        .getFixedMontant() !=
                    null
                ) {
                    Float salary = categoryRepository
                        .findOneByUserLoginAndNameCatego(getCurrentUserLoginn(), "Salary")
                        .getPeriodictyy()
                        .getFixedMontant();
                    if (catNotif.getMontant() != null) {
                        if (
                            (catNotif.getMontant() > 0.9 * salary * catNotif.getAverage()) &
                            (catNotif.getMontant() < 0.95 * salary * catNotif.getAverage())
                        ) {
                            Notification notif = new Notification(
                                catNotif.getMontant(),
                                value.getLogin(),
                                catNotif.getNameCatego(),
                                LocalDate.now(),
                                "90% exceeded"
                            );
                            notificationRepository.save(notif);
                            System.out.println(notif);
                        } else if (
                            (catNotif.getMontant() > 0.95 * salary * catNotif.getAverage()) &
                            (catNotif.getMontant() < salary * catNotif.getAverage())
                        ) {
                            Notification notif = new Notification(
                                catNotif.getMontant(),
                                value.getLogin(),
                                catNotif.getNameCatego(),
                                LocalDate.now(),
                                "95% exceeded"
                            );
                            notificationRepository.save(notif);
                            System.out.println(notif);
                        } else if (catNotif.getMontant() > salary * catNotif.getAverage()) {
                            Notification notif = new Notification(
                                catNotif.getMontant(),
                                value.getLogin(),
                                catNotif.getNameCatego(),
                                LocalDate.now(),
                                "100% exceeded"
                            );
                            notificationRepository.save(notif);
                            System.out.println(notif);
                        }
                    }
                }
                // Notification for the sub categories that the user specify
                if (
                    (
                        catNEW.getAverage() != null &
                        categoryRepository
                            .findOneByUserLoginAndNameCatego(getCurrentUserLoginn(), "Salary")
                            .getPeriodictyy()
                            .getFixedMontant() !=
                        null
                    ) &
                    (!catNEW.getOriginType().equals("Catego"))
                ) {
                    Float salary = categoryRepository
                        .findOneByUserLoginAndNameCatego(getCurrentUserLoginn(), "Salary")
                        .getPeriodictyy()
                        .getFixedMontant();
                    if (catNEW.getMontant() != null) {
                        if (
                            (catNEW.getMontant() > 0.9 * salary * catNEW.getAverage()) &
                            (catNEW.getMontant() < 0.95 * salary * catNEW.getAverage())
                        ) {
                            Notification notif = new Notification(
                                catNEW.getMontant(),
                                value.getLogin(),
                                catNEW.getNameCatego(),
                                LocalDate.now(),
                                "90% exceeded"
                            );
                            notificationRepository.save(notif);
                            System.out.println(notif);
                        } else if (
                            (catNEW.getMontant() > 0.95 * salary * catNEW.getAverage()) &
                            (catNEW.getMontant() < salary * catNEW.getAverage())
                        ) {
                            Notification notif = new Notification(
                                catNEW.getMontant(),
                                value.getLogin(),
                                catNEW.getNameCatego(),
                                LocalDate.now(),
                                "95% exceeded"
                            );
                            notificationRepository.save(notif);
                            System.out.println(notif);
                        } else if (catNEW.getMontant() > salary * catNEW.getAverage()) {
                            Notification notif = new Notification(
                                catNEW.getMontant(),
                                value.getLogin(),
                                catNEW.getNameCatego(),
                                LocalDate.now(),
                                "100% exceeded"
                            );
                            notificationRepository.save(notif);
                            System.out.println(notif);
                        }
                    }
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

    @GetMapping("/categories/namecatego")
    public List<String> getAllCategoriesnames() {
        List<Category> lista = categoryRepository.findByUserLogin(getCurrentUserLoginn());
        List<String> names = new ArrayList<String>();
        for (Category cat : lista) names.add(cat.getNameCatego());
        return names;
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
        Category cate = categoryRepository.findOneByUserLoginAndNameCatego(getCurrentUserLoginn(), nomcatego);
        String id = cate.getId();
        log.debug("REST request to delete Category : {}", nomcatego);
        if (cate.getOriginType().equals("Catego")) {
            List<Category> listaa = categoryRepository.findByUserLoginAndOriginType(getCurrentUserLoginn(), nomcatego);
            for (Category catego : listaa) categoryService.delete(catego.getId());
        }
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
        // History part
        if (user.getSoldeuserdepense() == null) user.setSoldeuserdepense(0F);
        if (user.getSolduserrevenus() == null) user.setSolduserrevenus(0F);
        userRepository.save(user);
        if (catego.getType().equals("Depense")) {
            user.setSoldeuserdepense(user.getSoldeuserdepense() + catego.getPeriodictyy().getFixedMontant());
            userRepository.save(user);
            HistoryLine hist = new HistoryLine(
                catego.getNameCatego(),
                LocalDate.now(),
                catego.getPeriodictyy().getFixedMontant(),
                catego.getUserLogin(),
                catego.getType(),
                user.getSoldeUser(),
                user.getSoldeuserdepense(),
                catego.getOriginType(),
                catego.getNameIcon(),
                catego.getColor()
            );
            historyLineRepository.save(hist);
            System.out.println(hist);
        } else {
            user.setSolduserrevenus(user.getSolduserrevenus() + catego.getPeriodictyy().getFixedMontant());
            userRepository.save(user);
            HistoryLine hist = new HistoryLine(
                catego.getNameCatego(),
                LocalDate.now(),
                catego.getPeriodictyy().getFixedMontant(),
                catego.getUserLogin(),
                user.getSolduserrevenus(),
                catego.getType(),
                user.getSoldeUser(),
                catego.getOriginType(),
                catego.getNameIcon(),
                catego.getColor()
            );
            historyLineRepository.save(hist);
            System.out.println(hist);
        }
        // Notifications part
        if (catego.getType().equals("Depense")) {
            Notification notif = new Notification(
                catego.getPeriodictyy().getFixedMontant(),
                user.getLogin(),
                catego.getNameCatego(),
                LocalDate.now(),
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
                LocalDate.now(),
                "Money IN"
            );
            notificationRepository.save(notif);
            System.out.println(notif);
        }
    }

    @GetMapping(value = "/categories/piechartdepense")
    public String piechartdepense() {
        JSONArray ja = new JSONArray();
        List<Category> listecategoDepense = categoryRepository.findByUserLoginAndTypeAndOriginType(
            getCurrentUserLoginn(),
            "Depense",
            "Catego"
        );
        try {
            for (Category catego : listecategoDepense) {
                System.out.println("here");
                JSONObject jo = new JSONObject();
                jo.put("value", catego.getMontant());
                jo.put("label", catego.getNameCatego());
                jo.put("color", catego.getColor());
                ja.put(jo);
            }
        } catch (JSONException e) {
            System.out.println("here2");
            e.printStackTrace();
        }

        return ja.toString();
    }

    @GetMapping(value = "/categories/pieglobale")
    public String pieglobale() {
        JSONArray ja = new JSONArray();
        List<Category> listecategoDepense = categoryRepository.findByUserLoginAndOriginType(getCurrentUserLoginn(), "Catego");
        try {
            for (Category catego : listecategoDepense) {
                JSONObject jo = new JSONObject();
                jo.put("value", catego.getMontant());
                jo.put("label", catego.getNameCatego());
                jo.put("color", catego.getColor());
                ja.put(jo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ja.toString();
    }

    @GetMapping(value = "/categories/barchartrevenus")
    public String barchartREvenus() {
        JSONArray dataR = new JSONArray();
        List<Category> hist3 = categoryRepository.findByUserLoginAndOriginType(getCurrentUserLoginn(), "Income");
        List<Category> histo4 = categoryRepository.findByUserLoginAndTypeAndOriginType(getCurrentUserLoginn(), "Revenus", "Catego");
        for (Category cat : histo4) {
            if (!cat.getNameCatego().equals("Income")) hist3.add(cat);
        }
        try {
            for (Category hist2 : hist3) {
                JSONObject jo = new JSONObject();
                jo.put("x", hist2.getNameCatego());
                jo.put("y", hist2.getMontant());
                dataR.put(jo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray JA = new JSONArray();
        try {
            JSONObject jo = new JSONObject();
            jo.put("seriesName", "Revenus");
            jo.put("data", dataR);
            jo.put("color", "yellow");
            JA.put(jo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return JA.toString();
    }

    /*
    Cette fonction se répéte chaque jour a 7h00
    */

    //@Scheduled(fixedDelay = 30000)
    @Scheduled(cron = "0 0 7 * * *", zone = "Africa/Tunis")
    public void Scheduled_task() {
        System.out.println("This a repeated task");

        List<User> listaa = userRepository.findAll();
        for (User user : listaa) {
            for (Category catego : FindAllCategories(user.getLogin())) {
                if (catego.getPeriodictyy() != null) {
                    System.out.println(catego.getNameCatego() + catego.getUserLogin());
                    boolean stayin = true;
                    if (catego.getPeriodictyy().getDateFin() != null) {
                        System.out.println("HEY " + catego.getPeriodictyy().getDateFin().isBefore(LocalDate.now()));
                        if (catego.getPeriodictyy().getDateFin().isBefore(LocalDate.now())) {
                            catego.setPeriodictyy(null);
                            System.out.println("This catego periodicity is deleted ");
                            categoryRepository.save(catego);
                            stayin = false;
                        }
                    }
                    if (stayin) {
                        if (
                            catego.getPeriodictyy().getFrequancy().equals("mois") &
                            catego.getPeriodictyy().getDateDeb().isBefore(LocalDate.now())
                        ) {
                            if (ZonedDateTime.now().getDayOfMonth() == catego.getPeriodictyy().getDateDeb().getDayOfMonth()) {
                                if (catego.getPeriodictyy().getDateFin() == null) {
                                    System.out.println("I m here");
                                    addperodicitysolde(catego, user);
                                }
                                if ((catego.getPeriodictyy().getDateFin() != null)) if (
                                    catego.getPeriodictyy().getDateFin().isBefore(LocalDate.now())
                                ) {
                                    System.out.println("I m here2");
                                    addperodicitysolde(catego, user);
                                }
                            }
                        }

                        if (
                            catego.getPeriodictyy().getFrequancy().equals("mois") &
                            catego.getPeriodictyy().getDateDeb().isAfter(LocalDate.now())
                        ) {
                            if (ZonedDateTime.now().getDayOfMonth() == catego.getPeriodictyy().getDateDeb().minusDays(1).getDayOfMonth()) {
                                if (catego.getPeriodictyy().getDateFin() == null) {
                                    Notification notif = new Notification(
                                        catego.getMontant(),
                                        user.getLogin(),
                                        catego.getNameCatego(),
                                        LocalDate.now(),
                                        "1Day left"
                                    );
                                    notificationRepository.save(notif);
                                    System.out.println(notif);
                                }
                                if ((catego.getPeriodictyy().getDateFin() != null)) if (
                                    catego.getPeriodictyy().getDateFin().isBefore(LocalDate.now())
                                ) {
                                    Notification notif = new Notification(
                                        catego.getMontant(),
                                        user.getLogin(),
                                        catego.getNameCatego(),
                                        LocalDate.now(),
                                        "1Day left"
                                    );
                                    notificationRepository.save(notif);
                                    System.out.println(notif);
                                }
                            }
                        }

                        if (
                            catego.getPeriodictyy().getFrequancy().equals("jour") &
                            catego.getPeriodictyy().getDateDeb().isBefore(LocalDate.now())
                        ) {
                            System.out.println(catego.getPeriodictyy().getDateDeb());
                            System.out.println(ZonedDateTime.now());
                            if (catego.getPeriodictyy().getDateFin() == null) addperodicitysolde(catego, user);

                            if (catego.getPeriodictyy().getDateFin() != null) {
                                if (catego.getPeriodictyy().getDateFin().isAfter(LocalDate.now())) addperodicitysolde(catego, user);
                            }
                        }

                        if (
                            catego.getPeriodictyy().getFrequancy().equals("semaine") &
                            catego.getPeriodictyy().getDateDeb().isBefore(LocalDate.now())
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
                                if (catego.getPeriodictyy().getDateFin().isAfter(LocalDate.now())) {
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
                            catego.getPeriodictyy().getDateDeb().isBefore(LocalDate.now())
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
                                if (catego.getPeriodictyy().getDateFin().isAfter(LocalDate.now())) {
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
                            catego.getPeriodictyy().getDateDeb().isBefore(LocalDate.now()) &
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
                                if (catego.getPeriodictyy().getDateFin().isAfter(LocalDate.now())) {
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
                            catego.getPeriodictyy().getDateDeb().isBefore(LocalDate.now()) &
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
                                if (catego.getPeriodictyy().getDateFin().isAfter(LocalDate.now())) {
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
                            catego.getPeriodictyy().getDateDeb().isBefore(LocalDate.now())
                        ) {
                            if (ZonedDateTime.now().getDayOfYear() == catego.getPeriodictyy().getDateDeb().getDayOfYear()) {
                                if (catego.getPeriodictyy().getDateFin() == null) {
                                    System.out.println("I m here3");
                                    addperodicitysolde(catego, user);
                                }
                                if ((catego.getPeriodictyy().getDateFin() != null)) if (
                                    catego.getPeriodictyy().getDateFin().isBefore(LocalDate.now())
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

    /*
    Cette fonction se répéte chaque mois Le 01 a 7h00
     */
    @Scheduled(cron = "0 0 7 1 * *", zone = "Africa/Tunis")
    public void scheduledtask2() {
        List<Category> Lista = categoryRepository.findByUserLogin(getCurrentUserLoginn());
        for (Category cat : Lista) {
            cat.setMontant(0F);
        }
    }

    /*
     This scheduled task : recalculate the avreage , Minmontant and Maxmontant of the user catégories After 3 MONTHS of the application use
     */
    private Float avregeInThreeMo(String login, String name, Float salary) {
        List<HistoryLine> listH = historyLineRepository.findByUserLoginAndCategoryName(login, name);
        Float result = 0F;
        if (!listH.isEmpty()) {
            int count = 0;
            for (HistoryLine hist : listH) {
                if (hist.getDateModif().isAfter(LocalDate.now().minusMonths(3))) {
                    result = hist.getMontant();
                    count = count + 1;
                }
            }
            return result / (count * salary);
        }
        return categoryRepository.findOneByUserLoginAndNameCatego(login, name).getAverage();
    }

    private Float minMongeInThreeMo(String login, String name, Float salary) {
        Float result;
        List<HistoryLine> listH = historyLineRepository.findByUserLoginAndCategoryName(login, name);
        if (categoryRepository.findOneByUserLoginAndNameCatego(login, name).getMinMontant() == null) result = salary; else result =
            categoryRepository.findOneByUserLoginAndNameCatego(login, name).getMinMontant();
        if (!listH.isEmpty()) {
            for (HistoryLine hist : listH) {
                if (hist.getDateModif().isAfter(LocalDate.now().minusMonths(3))) {
                    if ((result * salary) > hist.getMontant()) result = hist.getMontant() / salary;
                }
            }
        }
        return result;
    }

    private Float maxMongeInThreeMo(String login, String name, Float salary) {
        List<HistoryLine> listH = historyLineRepository.findByUserLoginAndCategoryName(login, name);
        Float result;
        if (categoryRepository.findOneByUserLoginAndNameCatego(login, name).getMaxMontant() == null) result = 0F; else result =
            categoryRepository.findOneByUserLoginAndNameCatego(login, name).getMaxMontant();
        if (!listH.isEmpty()) {
            for (HistoryLine hist : listH) {
                if (hist.getDateModif().isAfter(LocalDate.now().minusMonths(3))) {
                    if ((result * salary) < hist.getMontant()) result = hist.getMontant() / salary;
                }
            }
        }
        return result;
    }

    //@Scheduled(fixedDelay = 30000)
    @Scheduled(cron = "0 0 7 * * *", zone = "Africa/Tunis")
    public void scheduledtask3() {
        List<User> listaa = userRepository.findAll();
        for (User user : listaa) {
            System.out.println(user.getLogin());
            if (user.getDateSalary() != null & user.getSalary() != null) {
                if (LocalDate.now().isBefore(user.getDateSalary().plusMonths(3))) {
                    System.out.println("3 Months varibales update ");
                    List<Category> listaa2 = categoryRepository.findByUserLoginAndOriginType(user.getLogin(), "Catego");
                    for (Category cat : listaa2) {
                        if ((cat.getAverage() != null)) {
                            if (
                                (
                                    cat
                                        .getAverage()
                                        .equals(
                                            categoryRepository
                                                .findOneByUserLoginAndNameCatego("fixeduser", cat.getNameCatego())
                                                .getAverage()
                                        )
                                )
                            ) cat.setAverage(
                                avregeInThreeMo(
                                    user.getLogin(),
                                    cat.getNameCatego(),
                                    categoryRepository
                                        .findOneByUserLoginAndNameCatego(user.getLogin(), "Salary")
                                        .getPeriodictyy()
                                        .getFixedMontant()
                                )
                            );
                        } else cat.setAverage(
                            avregeInThreeMo(
                                user.getLogin(),
                                cat.getNameCatego(),
                                categoryRepository
                                    .findOneByUserLoginAndNameCatego(user.getLogin(), "Salary")
                                    .getPeriodictyy()
                                    .getFixedMontant()
                            )
                        );

                        if ((cat.getMinMontant() != null)) {
                            if (
                                (
                                    cat
                                        .getMinMontant()
                                        .equals(
                                            categoryRepository
                                                .findOneByUserLoginAndNameCatego("fixeduser", cat.getNameCatego())
                                                .getMinMontant()
                                        )
                                )
                            ) cat.setMinMontant(
                                minMongeInThreeMo(
                                    user.getLogin(),
                                    cat.getNameCatego(),
                                    categoryRepository
                                        .findOneByUserLoginAndNameCatego(user.getLogin(), "Salary")
                                        .getPeriodictyy()
                                        .getFixedMontant()
                                )
                            );
                        } else cat.setMinMontant(
                            minMongeInThreeMo(
                                user.getLogin(),
                                cat.getNameCatego(),
                                categoryRepository
                                    .findOneByUserLoginAndNameCatego(user.getLogin(), "Salary")
                                    .getPeriodictyy()
                                    .getFixedMontant()
                            )
                        );

                        if ((cat.getMaxMontant() != null)) {
                            if (
                                (
                                    cat
                                        .getMaxMontant()
                                        .equals(
                                            categoryRepository
                                                .findOneByUserLoginAndNameCatego("fixeduser", cat.getNameCatego())
                                                .getMaxMontant()
                                        )
                                )
                            ) cat.setMaxMontant(
                                maxMongeInThreeMo(
                                    user.getLogin(),
                                    cat.getNameCatego(),
                                    categoryRepository
                                        .findOneByUserLoginAndNameCatego(user.getLogin(), "Salary")
                                        .getPeriodictyy()
                                        .getFixedMontant()
                                )
                            );
                        } else cat.setMaxMontant(
                            maxMongeInThreeMo(
                                user.getLogin(),
                                cat.getNameCatego(),
                                categoryRepository
                                    .findOneByUserLoginAndNameCatego(user.getLogin(), "Salary")
                                    .getPeriodictyy()
                                    .getFixedMontant()
                            )
                        );

                        categoryRepository.save(cat);
                    }
                    user.setDateSalary(LocalDate.now());
                    userRepository.save(user);
                }
            }
        }
    }
}
