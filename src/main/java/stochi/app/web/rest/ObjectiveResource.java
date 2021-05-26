package stochi.app.web.rest;

import static stochi.app.security.SecurityUtils.getCurrentUserLoginn;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import stochi.app.domain.Category;
import stochi.app.domain.Objective;
import stochi.app.domain.Periode;
import stochi.app.domain.User;
import stochi.app.repository.CategoryRepository;
import stochi.app.repository.ObjectiveRepository;
import stochi.app.service.ObjectiveService;
import stochi.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link stochi.app.domain.Objective}.
 */
@RestController
@RequestMapping("/api")
public class ObjectiveResource {

    private final Logger log = LoggerFactory.getLogger(ObjectiveResource.class);

    private static final String ENTITY_NAME = "objective";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ObjectiveService objectiveService;

    private final ObjectiveRepository objectiveRepository;
    private final CategoryRepository categoryRepository;

    public ObjectiveResource(
        ObjectiveService objectiveService,
        ObjectiveRepository objectiveRepository,
        CategoryRepository categoryRepository
    ) {
        this.objectiveService = objectiveService;
        this.objectiveRepository = objectiveRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * {@code POST  /objectives} : Create a new objective.
     *
     * @param objective the objective to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new objective, or with status {@code 400 (Bad Request)} if the objective has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    /*
This part is for validating a suggestion !
*/
    @PostMapping("/objectives")
    public ResponseEntity<Objective> createObjective(@RequestBody Objective objective) throws URISyntaxException {
        log.debug("REST request to save Objective : {}", objective);
        if (objective.getId() != null) {
            throw new BadRequestAlertException("A new objective cannot already have an ID", ENTITY_NAME, "idexists");
        }
        System.out.println("AZERTY");
        System.out.println(calculsavingss(objective));
        System.out.println("AZEz151");
        if (objective.getAmountTot() > calculsavingss(objective)) {
            throw new BadRequestAlertException("You can't save this amount in that period of time ", ENTITY_NAME, "ImpossibleObjective");
        }
        Objective result = objectiveService.save(objective);
        return ResponseEntity
            .created(new URI("/api/objectives/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /*
This part is for giving Suggestions
*/
    private long[] calculDurations(LocalDate DDB, LocalDate DDF) {
        Period period = Period.between(DDB, DDF);

        return new long[] { period.getDays(), period.getMonths(), period.getYears() };
    }

    @PostMapping("/objectives/sugggestion")
    public List<Objective> sugesstion(@RequestBody Objective objective) throws URISyntaxException {
        log.debug("REST request to save Objective : {}", objective);
        if (objective.getId() != null) {
            throw new BadRequestAlertException("A new objective cannot already have an ID", ENTITY_NAME, "idexists");
        }

        return suggestions(objective);
    }

    // See if the objective can be done !!
    private Float calculsavings(Objective obj, LocalDate DDF) {
        Float savings = 0F;
        List<Category> listaaa = categoryRepository.findByUserLoginAndTypeAndOriginType(getCurrentUserLoginn(), "Revenus", "Income");
        List<Category> listRE = categoryRepository.findByUserLoginAndTypeAndOriginType(getCurrentUserLoginn(), "Depense", "Catego");
        for (Category cat : listaaa) {
            if (cat.getPeriodictyy() != null) {
                if ((cat.getPeriodictyy().getDateFin() != null) & (cat.getPeriodictyy().getDateDeb() != null)) {
                    System.out.println(cat.getNameCatego() + "here2");
                    if (
                        obj.getPeriodicity().getDateDeb().isAfter(cat.getPeriodictyy().getDateDeb()) &
                        obj.getPeriodicity().getDateFin().isBefore(cat.getPeriodictyy().getDateFin())
                    ) {
                        if ((cat.getPeriodictyy().getFrequancy().equals("mois")) & (cat.getPeriodictyy().getFrequancy() != null)) savings =
                            savings + cat.getPeriodictyy().getFixedMontant() * calculDurations(obj.getPeriodicity().getDateDeb(), DDF)[1];
                        if (
                            (cat.getPeriodictyy().getFrequancy().equals("semaine")) & (cat.getPeriodictyy().getFrequancy() != null)
                        ) savings =
                            savings +
                            cat.getPeriodictyy().getFixedMontant() *
                            calculDurations(obj.getPeriodicity().getDateDeb(), DDF)[0] /
                            7;
                        if (
                            (cat.getPeriodictyy().getFrequancy().equals("2semaine")) & (cat.getPeriodictyy().getFrequancy() != null)
                        ) savings =
                            savings +
                            cat.getPeriodictyy().getFixedMontant() *
                            calculDurations(obj.getPeriodicity().getDateDeb(), DDF)[0] /
                            14;
                        if (
                            (cat.getPeriodictyy().getFrequancy().equals("trimestre")) & (cat.getPeriodictyy().getFrequancy() != null)
                        ) savings =
                            savings +
                            cat.getPeriodictyy().getFixedMontant() *
                            calculDurations(obj.getPeriodicity().getDateDeb(), DDF)[0] /
                            3;
                        if (
                            (cat.getPeriodictyy().getFrequancy().equals("semestre")) & (cat.getPeriodictyy().getFrequancy() != null)
                        ) savings =
                            savings +
                            cat.getPeriodictyy().getFixedMontant() *
                            calculDurations(obj.getPeriodicity().getDateDeb(), DDF)[1] /
                            2;
                        if ((cat.getPeriodictyy().getFrequancy().equals("annee")) & (cat.getPeriodictyy().getFrequancy() != null)) savings =
                            savings + cat.getPeriodictyy().getFixedMontant() * calculDurations(obj.getPeriodicity().getDateDeb(), DDF)[2];
                    }
                }
                if ((cat.getPeriodictyy().getDateFin() == null) & (cat.getPeriodictyy().getDateDeb() != null)) {
                    if (obj.getPeriodicity().getDateDeb().isAfter(cat.getPeriodictyy().getDateDeb())) {
                        System.out.println(cat.getNameCatego() + "here2");
                        if ((cat.getPeriodictyy().getFrequancy().equals("mois")) & (cat.getPeriodictyy().getFrequancy() != null)) savings =
                            savings + cat.getPeriodictyy().getFixedMontant() * calculDurations(obj.getPeriodicity().getDateDeb(), DDF)[1];
                        if (
                            (cat.getPeriodictyy().getFrequancy().equals("semaine")) & (cat.getPeriodictyy().getFrequancy() != null)
                        ) savings =
                            savings +
                            cat.getPeriodictyy().getFixedMontant() *
                            calculDurations(obj.getPeriodicity().getDateDeb(), DDF)[0] /
                            7;
                        if (
                            (cat.getPeriodictyy().getFrequancy().equals("2semaine")) & (cat.getPeriodictyy().getFrequancy() != null)
                        ) savings =
                            savings +
                            cat.getPeriodictyy().getFixedMontant() *
                            calculDurations(obj.getPeriodicity().getDateDeb(), DDF)[0] /
                            14;
                        if (
                            (cat.getPeriodictyy().getFrequancy().equals("trimestre")) & (cat.getPeriodictyy().getFrequancy() != null)
                        ) savings =
                            savings +
                            cat.getPeriodictyy().getFixedMontant() *
                            calculDurations(obj.getPeriodicity().getDateDeb(), DDF)[0] /
                            3;
                        if (
                            (cat.getPeriodictyy().getFrequancy().equals("semestre")) & (cat.getPeriodictyy().getFrequancy() != null)
                        ) savings =
                            savings +
                            cat.getPeriodictyy().getFixedMontant() *
                            calculDurations(obj.getPeriodicity().getDateDeb(), DDF)[1] /
                            2;
                        if ((cat.getPeriodictyy().getFrequancy().equals("annee")) & (cat.getPeriodictyy().getFrequancy() != null)) savings =
                            savings + cat.getPeriodictyy().getFixedMontant() * calculDurations(obj.getPeriodicity().getDateDeb(), DDF)[2];
                    }
                }
            }
        }
        for (Category cat : listRE) {
            if (cat.getPeriodictyy() != null) {
                if ((cat.getPeriodictyy().getDateFin() != null) & (cat.getPeriodictyy().getDateDeb() != null)) {
                    if (
                        obj.getPeriodicity().getDateDeb().isAfter(cat.getPeriodictyy().getDateDeb()) &
                        obj.getPeriodicity().getDateFin().isBefore(cat.getPeriodictyy().getDateFin())
                    ) {
                        System.out.println(cat.getNameCatego() + "here2");
                        if (cat.getPeriodictyy().getFrequancy().equals("mois")) savings =
                            savings - cat.getPeriodictyy().getFixedMontant() * calculDurations(obj.getPeriodicity().getDateDeb(), DDF)[1];
                        if (cat.getPeriodictyy().getFrequancy().equals("semaine")) savings =
                            savings -
                            cat.getPeriodictyy().getFixedMontant() *
                            calculDurations(obj.getPeriodicity().getDateDeb(), DDF)[0] /
                            7;
                        if (cat.getPeriodictyy().getFrequancy().equals("2semaine")) savings =
                            savings -
                            cat.getPeriodictyy().getFixedMontant() *
                            calculDurations(obj.getPeriodicity().getDateDeb(), DDF)[0] /
                            14;
                        if (cat.getPeriodictyy().getFrequancy().equals("trimestre")) savings =
                            savings -
                            cat.getPeriodictyy().getFixedMontant() *
                            calculDurations(obj.getPeriodicity().getDateDeb(), DDF)[0] /
                            3;
                        if (cat.getPeriodictyy().getFrequancy().equals("semestre")) savings =
                            savings -
                            cat.getPeriodictyy().getFixedMontant() *
                            calculDurations(obj.getPeriodicity().getDateDeb(), DDF)[1] /
                            2;
                        if (cat.getPeriodictyy().getFrequancy().equals("annee")) savings =
                            savings - cat.getPeriodictyy().getFixedMontant() * calculDurations(obj.getPeriodicity().getDateDeb(), DDF)[2];
                        System.out.println(savings);
                    }
                }
                if ((cat.getPeriodictyy().getDateFin() == null) & (cat.getPeriodictyy().getDateDeb() != null)) {
                    if (obj.getPeriodicity().getDateDeb().isAfter(cat.getPeriodictyy().getDateDeb())) {
                        if (cat.getPeriodictyy().getFrequancy().equals("mois")) savings =
                            savings - cat.getPeriodictyy().getFixedMontant() * calculDurations(obj.getPeriodicity().getDateDeb(), DDF)[1];
                        if (cat.getPeriodictyy().getFrequancy().equals("semaine")) savings =
                            savings -
                            cat.getPeriodictyy().getFixedMontant() *
                            calculDurations(obj.getPeriodicity().getDateDeb(), DDF)[0] /
                            7;
                        if (cat.getPeriodictyy().getFrequancy().equals("2semaine")) savings =
                            savings -
                            cat.getPeriodictyy().getFixedMontant() *
                            calculDurations(obj.getPeriodicity().getDateDeb(), DDF)[0] /
                            14;
                        if (cat.getPeriodictyy().getFrequancy().equals("trimestre")) savings =
                            savings -
                            cat.getPeriodictyy().getFixedMontant() *
                            calculDurations(obj.getPeriodicity().getDateDeb(), DDF)[0] /
                            3;
                        if (cat.getPeriodictyy().getFrequancy().equals("semestre")) savings =
                            savings -
                            cat.getPeriodictyy().getFixedMontant() *
                            calculDurations(obj.getPeriodicity().getDateDeb(), DDF)[1] /
                            2;
                        if (cat.getPeriodictyy().getFrequancy().equals("annee")) savings =
                            savings - cat.getPeriodictyy().getFixedMontant() * calculDurations(obj.getPeriodicity().getDateDeb(), DDF)[2];
                        System.out.println(savings);
                    }
                }
            }
        }

        return savings;
    }

    private Float calculsavingss(Objective obj) {
        Float savings = 0F;
        System.out.println(getCurrentUserLoginn());
        List<Category> listRE = categoryRepository.findByUserLoginAndTypeAndOriginType(getCurrentUserLoginn(), "Depense", "Catego");

        for (Category cat : listRE) {
            if (cat.getMontant() != null) {
                if (cat.getMontant() != 0F) {
                    System.out.println(cat.getNameCatego());
                    savings =
                        savings +
                        (cat.getAverage() - cat.getMinMontant()) *
                        calculDurations(obj.getPeriodicity().getDateDeb(), obj.getPeriodicity().getDateFin())[1];
                }
            }
        }
        return savings + calculsavings(obj, obj.getPeriodicity().getDateFin());
    }

    // For the Soft Suggestion to calculate the final date and how much to pay per month !

    private LocalDate findDate(LocalDate DDB, Float Fixed) {
        LocalDate DDF;
        Float savingsolde = 0F;
        List<Category> listRE = categoryRepository.findByUserLoginAndTypeAndOriginType(getCurrentUserLoginn(), "Depense", "Catego");

        for (Category cat : listRE) {
            if (cat.getMontant() != null) if (cat.getMontant() != 0) savingsolde = savingsolde + cat.getAverage() - cat.getMinMontant();
            System.out.println(savingsolde);
        }

        DDF = DDB.plusMonths((long) (Fixed / savingsolde));

        return DDF;
    }

    private Float findfixedamount() {
        Float savingsolde = 0F;
        List<Category> listRE = categoryRepository.findByUserLoginAndTypeAndOriginType(getCurrentUserLoginn(), "Depense", "Catego");
        for (Category cat : listRE) {
            if (cat.getMontant() != null) {
                if (cat.getMontant() != 0F) {
                    savingsolde = savingsolde + cat.getAverage() - cat.getMinMontant();
                }
            }
        }
        return savingsolde;
    }

    private LocalDate findDateHard(Objective objective) {
        Float amountVar = 0F;
        LocalDate DDF = objective.getPeriodicity().getDateDeb().plusMonths(1);
        while (objective.getAmountTot() > amountVar) {
            amountVar = amountVar + calculsavings(objective, DDF);
            DDF = DDF.plusMonths(1);
        }

        return DDF;
    }

    private LocalDate findardharddate(Objective objective) {
        Float amountVar = 0F;
        LocalDate DDF = objective.getPeriodicity().getDateDeb().plusMonths(1);
        Float fixed = findfixedamount();
        while (objective.getAmountTot() > amountVar) {
            amountVar = amountVar + calculsavings(objective, DDF);
            amountVar = amountVar + fixed;
            DDF = DDF.plusMonths(1);
        }
        return DDF;
    }

    private Float finperiodSadoHArdhard(Objective objective) {
        Float totale = objective.getAmountTot() / calculDurations(objective.getPeriodicity().getDateDeb(), findardharddate(objective))[1];
        return totale;
    }

    private Float finperiodSoft(Objective objective) {
        Float totale = objective.getAmountTot() / calculDurations(objective.getPeriodicity().getDateDeb(), findDateHard(objective))[1];
        return totale;
    }

    private List<Objective> suggestions(Objective objective) {
        List<Objective> suggg = new ArrayList<>();

        LocalDate DatefinSoft = findDate(objective.getPeriodicity().getDateDeb(), objective.getAmountTot());

        Periode period1 = new Periode(objective.getPeriodicity().getDateDeb(), DatefinSoft, "mois");
        System.out.println(period1);
        Objective sugg1 = new Objective(
            objective.getName(),
            "This is a Meduim Suggestion ALL categories with minimum value , You need to save : " + findfixedamount() + "/month",
            getCurrentUserLoginn(),
            objective.getAmountTot(),
            0F,
            period1,
            objective.getColor(),
            objective.getNameIcon(),
            "Meduim"
        );
        suggg.add(sugg1);
        LocalDate DatefinMeduim = findDateHard(objective);
        Periode period2 = new Periode(objective.getPeriodicity().getDateDeb(), DatefinMeduim, "mois");
        Objective sugg2 = new Objective(
            objective.getName(),
            "This is a Soft Suggestion you can just stick by your usual exepnses and save it you need to save " +
            finperiodSoft(objective) +
            "/month",
            getCurrentUserLoginn(),
            objective.getAmountTot(),
            0F,
            period2,
            objective.getColor(),
            objective.getNameIcon(),
            "Soft"
        );
        suggg.add(sugg2);
        LocalDate DatefinHard = findardharddate(objective);
        Periode period3 = new Periode(objective.getPeriodicity().getDateDeb(), DatefinHard, "mois");
        Objective sugg3 = new Objective(
            objective.getName(),
            "This is a Hard Suggestion you need to get the minimum of depense categories and save form your free budget you need to pay  " +
            finperiodSadoHArdhard(objective) +
            "/month",
            getCurrentUserLoginn(),
            objective.getAmountTot(),
            0F,
            period3,
            objective.getColor(),
            objective.getNameIcon(),
            "Hard"
        );
        suggg.add(sugg3);

        return suggg;
    }

    /**
     * {@code PUT  /objectives/:id} : Updates an existing objective.
     *
     * @param id the id of the objective to save.
     * @param objective the objective to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated objective,
     * or with status {@code 400 (Bad Request)} if the objective is not valid,
     * or with status {@code 500 (Internal Server Error)} if the objective couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/objectives/{id}")
    public ResponseEntity<Objective> updateObjective(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Objective objective
    ) throws URISyntaxException {
        log.debug("REST request to update Objective : {}, {}", id, objective);
        if (objective.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, objective.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!objectiveRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Objective result = objectiveService.save(objective);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, objective.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /objectives/:id} : Partial updates given fields of an existing objective, field will ignore if it is null
     *
     * @param id the id of the objective to save.
     * @param objective the objective to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated objective,
     * or with status {@code 400 (Bad Request)} if the objective is not valid,
     * or with status {@code 404 (Not Found)} if the objective is not found,
     * or with status {@code 500 (Internal Server Error)} if the objective couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/objectives/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Objective> partialUpdateObjective(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Objective objective
    ) throws URISyntaxException {
        log.debug("REST request to partial update Objective partially : {}, {}", id, objective);
        if (objective.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, objective.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!objectiveRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Objective> result = objectiveService.partialUpdate(objective);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, objective.getId())
        );
    }

    /**
     * {@code GET  /objectives} : get all the objectives.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of objectives in body.
     */
    @GetMapping("/objectives")
    public ResponseEntity<List<Objective>> getAllObjectives(Pageable pageable) {
        log.debug("REST request to get a page of Objectives");
        Page<Objective> page = objectiveService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /objectives/:id} : get the "id" objective.
     *
     * @param id the id of the objective to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the objective, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/objectives/{id}")
    public ResponseEntity<Objective> getObjective(@PathVariable String id) {
        log.debug("REST request to get Objective : {}", id);
        Optional<Objective> objective = objectiveService.findOne(id);
        return ResponseUtil.wrapOrNotFound(objective);
    }

    /**
     * {@code DELETE  /objectives/:id} : delete the "id" objective.
     *
     * @param id the id of the objective to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/objectives/{id}")
    public ResponseEntity<Void> deleteObjective(@PathVariable String id) {
        log.debug("REST request to delete Objective : {}", id);
        objectiveService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
