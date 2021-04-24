package stochi.app.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
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
import stochi.app.domain.Periode;
import stochi.app.repository.PeriodeRepository;
import stochi.app.service.PeriodeService;
import stochi.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link stochi.app.domain.Periode}.
 */
@RestController
@RequestMapping("/api")
public class PeriodeResource {

    private final Logger log = LoggerFactory.getLogger(PeriodeResource.class);

    private static final String ENTITY_NAME = "periode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PeriodeService periodeService;

    private final PeriodeRepository periodeRepository;

    public PeriodeResource(PeriodeService periodeService, PeriodeRepository periodeRepository) {
        this.periodeService = periodeService;
        this.periodeRepository = periodeRepository;
    }

    /**
     * {@code POST  /periodes} : Create a new periode.
     *
     * @param periode the periode to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new periode, or with status {@code 400 (Bad Request)} if the periode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/periodes")
    public ResponseEntity<Periode> createPeriode(@RequestBody Periode periode) throws URISyntaxException {
        log.debug("REST request to save Periode : {}", periode);
        if (periode.getId() != null) {
            throw new BadRequestAlertException("A new periode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Periode result = periodeService.save(periode);
        return ResponseEntity
            .created(new URI("/api/periodes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /periodes/:id} : Updates an existing periode.
     *
     * @param id the id of the periode to save.
     * @param periode the periode to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated periode,
     * or with status {@code 400 (Bad Request)} if the periode is not valid,
     * or with status {@code 500 (Internal Server Error)} if the periode couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/periodes/{id}")
    public ResponseEntity<Periode> updatePeriode(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Periode periode
    ) throws URISyntaxException {
        log.debug("REST request to update Periode : {}, {}", id, periode);
        if (periode.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, periode.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!periodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Periode result = periodeService.save(periode);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, periode.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /periodes/:id} : Partial updates given fields of an existing periode, field will ignore if it is null
     *
     * @param id the id of the periode to save.
     * @param periode the periode to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated periode,
     * or with status {@code 400 (Bad Request)} if the periode is not valid,
     * or with status {@code 404 (Not Found)} if the periode is not found,
     * or with status {@code 500 (Internal Server Error)} if the periode couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/periodes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Periode> partialUpdatePeriode(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Periode periode
    ) throws URISyntaxException {
        log.debug("REST request to partial update Periode partially : {}, {}", id, periode);
        if (periode.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, periode.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!periodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Periode> result = periodeService.partialUpdate(periode);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, periode.getId())
        );
    }

    /**
     * {@code GET  /periodes} : get all the periodes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of periodes in body.
     */
    @GetMapping("/periodes")
    public ResponseEntity<List<Periode>> getAllPeriodes(Pageable pageable) {
        log.debug("REST request to get a page of Periodes");
        Page<Periode> page = periodeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /periodes/:id} : get the "id" periode.
     *
     * @param id the id of the periode to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the periode, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/periodes/{id}")
    public ResponseEntity<Periode> getPeriode(@PathVariable String id) {
        log.debug("REST request to get Periode : {}", id);
        Optional<Periode> periode = periodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(periode);
    }

    /**
     * {@code DELETE  /periodes/:id} : delete the "id" periode.
     *
     * @param id the id of the periode to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/periodes/{id}")
    public ResponseEntity<Void> deletePeriode(@PathVariable String id) {
        log.debug("REST request to delete Periode : {}", id);
        periodeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
