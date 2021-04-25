package stochi.app.web.rest;

import static stochi.app.security.SecurityUtils.getCurrentUserLoginn;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
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
import stochi.app.domain.User;
import stochi.app.repository.CategoryRepository;
import stochi.app.repository.HistoryLineRepository;
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

    public CategoryResource(
        CategoryService categoryService,
        CategoryRepository categoryRepository,
        UserRepository userRepository,
        HistoryLineRepository historyLineRepository
    ) {
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.historyLineRepository = historyLineRepository;
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
        log.debug("REST request to save Category : {}", category);
        if (category.getId() != null) {
            throw new BadRequestAlertException("A new category cannot already have an ID", ENTITY_NAME, "idexists");
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
        float montan = category.getMontant();
        System.out.println(montan);
        if (!category.getColor().equals("")) catNEW.setColor(category.getColor());
        if (category.getMaxMontant() != 0) catNEW.setMaxMontant(category.getMaxMontant());
        if (category.getMinMontant() != 0) catNEW.setMinMontant(category.getMinMontant());
        if (!category.getNameCatego().equals("")) catNEW.setNameCatego(category.getNameCatego());
        if (category.getPeriodictyy() != null) catNEW.setPeriodictyy(category.getPeriodictyy());
        System.out.println(category.getPeriodictyy());
        if (category.getMontant() != 0) {
            if (!category.getOriginType().equals("Catego")) {
                Category Categooo = categoryRepository.findOneByUserLoginAndNameCatego(getCurrentUserLoginn(), category.getOriginType());
                Categooo.setMontant(Categooo.getMontant() + category.getMontant());
                categoryRepository.save(Categooo);
                System.out.println(Categooo);
            }
            catNEW.setMontant(category.getMontant() + catNEW.getMontant());
            System.out.println("from here ?");
            Optional<User> constants = userRepository.findOneByLogin(catNEW.getUserLogin());
            System.out.println(constants);
            User value = constants.orElseThrow(() -> new RuntimeException("No such data found"));
            System.out.println(value);
            if (catNEW.getType().equals("Depense")) value.setSoldeUser(value.getSoldeUser() - category.getMontant());
            if (catNEW.getType().equals("Revenus")) value.setSoldeUser(value.getSoldeUser() + category.getMontant());
            System.out.println(value);
            userRepository.save(value);
            System.out.println(value);

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
        log.debug("REST request to delete Category : {}", nomcatego);
        categoryService.delete(nomcatego);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, nomcatego))
            .build();
    }

    private List<Category> FindAllCategories(String login) {
        return categoryRepository.findByUserLogin(login);
    }

    private void addperodicitysolde(Category catego, User user) {
        catego.setMontant(catego.getMontant() + catego.getPeriodictyy().getFixedMontant());
        categoryRepository.save(catego);
        if (catego.getType().equals("Depense")) user.setSoldeUser(user.getSoldeUser() + catego.getPeriodictyy().getFixedMontant());
        if (catego.getType().equals("Revenus")) user.setSoldeUser(user.getSoldeUser() - catego.getPeriodictyy().getFixedMontant());
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
    }

    /*
    Cette fonction se répéte chaque jour a 7h00
    */

    @Scheduled(cron = "0 0 7 * * *", zone = "Africa/Tunis")
    public void Scheduled_task() {
        System.out.println("This a repeated task");

        List<User> listaa = userRepository.findAll();
        for (User user : listaa) {
            for (Category catego : FindAllCategories(user.getLogin())) {
                if (catego.getPeriodictyy() != null) {
                    if (
                        catego.getPeriodictyy().getFrequancy().equals("mois") &
                        catego.getPeriodictyy().getDateDeb().isAfter(ZonedDateTime.now())
                    ) {
                        if (LocalDateTime.now().getDayOfMonth() == catego.getPeriodictyy().getDateDeb().getDayOfMonth()) if (
                            (catego.getPeriodictyy().getDateFin() == null) |
                            (
                                catego.getPeriodictyy().getDateFin() != null &
                                catego.getPeriodictyy().getDateFin().isBefore(ZonedDateTime.now())
                            )
                        ) addperodicitysolde(catego, user);
                    }
                    if (
                        catego.getPeriodictyy().getFrequancy().equals("jour") &
                        catego.getPeriodictyy().getDateDeb().isAfter(ZonedDateTime.now())
                    ) {
                        if (
                            (catego.getPeriodictyy().getDateFin() == null) |
                            (
                                catego.getPeriodictyy().getDateFin() != null &
                                catego.getPeriodictyy().getDateFin().isBefore(ZonedDateTime.now())
                            )
                        ) addperodicitysolde(catego, user);
                    }
                    if (
                        catego.getPeriodictyy().getFrequancy().equals("semaine") &
                        catego.getPeriodictyy().getDateDeb().isAfter(ZonedDateTime.now())
                    ) {
                        if (
                            (catego.getPeriodictyy().getDateFin() == null) |
                            (
                                catego.getPeriodictyy().getDateFin() != null &
                                catego.getPeriodictyy().getDateFin().isBefore(ZonedDateTime.now())
                            )
                        ) {
                            if (catego.getPeriodictyy().getNumberleft() == 6) {
                                addperodicitysolde(catego, user);
                                catego.getPeriodictyy().setNumberleft(0L);
                            } else catego.getPeriodictyy().setNumberleft(catego.getPeriodictyy().getNumberleft() + 1);
                            categoryRepository.save(catego);
                        }
                    }
                    if (
                        catego.getPeriodictyy().getFrequancy().equals("2semaine") &
                        catego.getPeriodictyy().getDateDeb().isAfter(ZonedDateTime.now())
                    ) {
                        if (
                            (catego.getPeriodictyy().getDateFin() == null) |
                            (
                                catego.getPeriodictyy().getDateFin() != null &
                                catego.getPeriodictyy().getDateFin().isBefore(ZonedDateTime.now())
                            )
                        ) {
                            if (catego.getPeriodictyy().getNumberleft() == 13) {
                                addperodicitysolde(catego, user);
                                catego.getPeriodictyy().setNumberleft(0L);
                            } else catego.getPeriodictyy().setNumberleft(catego.getPeriodictyy().getNumberleft() + 1);
                            categoryRepository.save(catego);
                        }
                    }
                    if (
                        catego.getPeriodictyy().getFrequancy().equals("trimestre") &
                        catego.getPeriodictyy().getDateDeb().isAfter(ZonedDateTime.now())
                    ) {
                        if (
                            (catego.getPeriodictyy().getDateFin() == null) |
                            (
                                catego.getPeriodictyy().getDateFin() != null &
                                catego.getPeriodictyy().getDateFin().isBefore(ZonedDateTime.now())
                            )
                        ) {
                            if (LocalDateTime.now().getDayOfMonth() == catego.getPeriodictyy().getDateDeb().getDayOfMonth()) {
                                if (catego.getPeriodictyy().getNumberleft() == 3) {
                                    addperodicitysolde(catego, user);
                                    catego.getPeriodictyy().setNumberleft(0L);
                                } else catego.getPeriodictyy().setNumberleft(catego.getPeriodictyy().getNumberleft() + 1);
                            }
                            categoryRepository.save(catego);
                        }
                    }
                    if (
                        catego.getPeriodictyy().getFrequancy().equals("semestre") &
                        catego.getPeriodictyy().getDateDeb().isAfter(ZonedDateTime.now())
                    ) {
                        if (
                            (catego.getPeriodictyy().getDateFin() == null) |
                            (
                                catego.getPeriodictyy().getDateFin() != null &
                                catego.getPeriodictyy().getDateFin().isBefore(ZonedDateTime.now())
                            )
                        ) {
                            if (LocalDateTime.now().getDayOfMonth() == catego.getPeriodictyy().getDateDeb().getDayOfMonth()) {
                                if (catego.getPeriodictyy().getNumberleft() == 5) {
                                    addperodicitysolde(catego, user);
                                    catego.getPeriodictyy().setNumberleft(0L);
                                } else catego.getPeriodictyy().setNumberleft(catego.getPeriodictyy().getNumberleft() + 1);
                                categoryRepository.save(catego);
                            }
                        }
                    }
                    if (
                        catego.getPeriodictyy().getFrequancy().equals("annee") &
                        catego.getPeriodictyy().getDateDeb().isAfter(ZonedDateTime.now())
                    ) {
                        if (LocalDateTime.now().getDayOfYear() == catego.getPeriodictyy().getDateDeb().getDayOfYear()) if (
                            (catego.getPeriodictyy().getDateFin() == null) |
                            (
                                catego.getPeriodictyy().getDateFin() != null &
                                catego.getPeriodictyy().getDateFin().isBefore(ZonedDateTime.now())
                            )
                        ) addperodicitysolde(catego, user);
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
