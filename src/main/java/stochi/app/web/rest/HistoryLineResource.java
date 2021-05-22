package stochi.app.web.rest;

import static stochi.app.security.SecurityUtils.getCurrentUserLoginn;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import stochi.app.domain.HistoryLine;
import stochi.app.repository.HistoryLineRepository;
import stochi.app.service.HistoryLineService;
import stochi.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link stochi.app.domain.HistoryLine}.
 */
@RestController
@RequestMapping("/api")
public class HistoryLineResource {

    private final Logger log = LoggerFactory.getLogger(HistoryLineResource.class);

    private static final String ENTITY_NAME = "historyLine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HistoryLineService historyLineService;

    private final HistoryLineRepository historyLineRepository;

    public HistoryLineResource(HistoryLineService historyLineService, HistoryLineRepository historyLineRepository) {
        this.historyLineService = historyLineService;
        this.historyLineRepository = historyLineRepository;
    }

    /**
     * {@code POST  /history-lines} : Create a new historyLine.
     *
     * @param historyLine the historyLine to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new historyLine, or with status {@code 400 (Bad Request)} if the historyLine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/history-lines")
    public ResponseEntity<HistoryLine> createHistoryLine(@Valid @RequestBody HistoryLine historyLine) throws URISyntaxException {
        log.debug("REST request to save HistoryLine : {}", historyLine);
        if (historyLine.getId() != null) {
            throw new BadRequestAlertException("A new historyLine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HistoryLine result = historyLineService.save(historyLine);
        return ResponseEntity
            .created(new URI("/api/history-lines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /history-lines/:id} : Updates an existing historyLine.
     *
     * @param id the id of the historyLine to save.
     * @param historyLine the historyLine to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historyLine,
     * or with status {@code 400 (Bad Request)} if the historyLine is not valid,
     * or with status {@code 500 (Internal Server Error)} if the historyLine couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/history-lines/{id}")
    public ResponseEntity<HistoryLine> updateHistoryLine(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody HistoryLine historyLine
    ) throws URISyntaxException {
        log.debug("REST request to update HistoryLine : {}, {}", id, historyLine);
        if (historyLine.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historyLine.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historyLineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HistoryLine result = historyLineService.save(historyLine);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, historyLine.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /history-lines/:id} : Partial updates given fields of an existing historyLine, field will ignore if it is null
     *
     * @param id the id of the historyLine to save.
     * @param historyLine the historyLine to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historyLine,
     * or with status {@code 400 (Bad Request)} if the historyLine is not valid,
     * or with status {@code 404 (Not Found)} if the historyLine is not found,
     * or with status {@code 500 (Internal Server Error)} if the historyLine couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/history-lines/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<HistoryLine> partialUpdateHistoryLine(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody HistoryLine historyLine
    ) throws URISyntaxException {
        log.debug("REST request to partial update HistoryLine partially : {}, {}", id, historyLine);
        if (historyLine.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historyLine.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historyLineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HistoryLine> result = historyLineService.partialUpdate(historyLine);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, historyLine.getId())
        );
    }

    /**
     * {@code GET  /history-lines} : get all the historyLines.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of historyLines in body.
     */
    @GetMapping("/history-lines")
    public ResponseEntity<List<HistoryLine>> getAllHistoryLines(Pageable pageable) {
        log.debug("REST request to get a page of HistoryLines");
        Page<HistoryLine> page = historyLineService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/history-lines/all")
    public List<HistoryLine> getAllUserhisto() {
        return historyLineRepository.findByUserLogin(getCurrentUserLoginn());
    }

    @GetMapping("/history/specifichisto/{nomcatego}")
    public List<HistoryLine> getSpecifichistoca(@PathVariable(value = "nomcatego") String nomcatego) {
        if (historyLineRepository.findByUserLoginAndCategoryName(getCurrentUserLoginn(), nomcatego) == null) {
            throw new BadRequestAlertException("There is no categ with that name", ENTITY_NAME, "Catego not found");
        }
        return historyLineRepository.findByUserLoginAndCategoryName(getCurrentUserLoginn(), nomcatego);
    }

    /**
     * {@code GET  /history-lines/:id} : get the "id" historyLine.
     *
     * @param id the id of the historyLine to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the historyLine, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/history-lines/{id}")
    public ResponseEntity<HistoryLine> getHistoryLine(@PathVariable String id) {
        log.debug("REST request to get HistoryLine : {}", id);
        Optional<HistoryLine> historyLine = historyLineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(historyLine);
    }

    /**
     * {@code DELETE  /history-lines/:id} : delete the "id" historyLine.
     *
     * @param id the id of the historyLine to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/history-lines/{id}")
    public ResponseEntity<Void> deleteHistoryLine(@PathVariable String id) {
        log.debug("REST request to delete HistoryLine : {}", id);
        historyLineService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }

    @GetMapping("/history-lines/soldechart")
    public List<Float> evolutionSolde() {
        List<Float> listaa = new ArrayList<Float>();
        List<HistoryLine> histo = historyLineRepository.findByUserLogin(getCurrentUserLoginn());
        for (HistoryLine histoo : histo) {
            listaa.add(histoo.getSoldeuseravant());
        }
        return listaa;
    }

    @GetMapping("/history-lines/multiseries")
    public String evolutionSol() {
        JSONArray dataD = new JSONArray();
        List<HistoryLine> histo = historyLineRepository.findByUserLoginAndTypeCategoAndOrigintype(
            getCurrentUserLoginn(),
            "Depense",
            "Catego"
        );
        try {
            JSONObject jo22 = new JSONObject();
            jo22.put("x", LocalDate.now());
            jo22.put("y", 0F);
            dataD.put(jo22);

            for (HistoryLine hist : histo) {
                JSONObject jo = new JSONObject();
                jo.put("x", hist.getDateModif());
                jo.put("y", hist.getDepensechart());
                dataD.put(jo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray dataR = new JSONArray();
        List<HistoryLine> histo2 = historyLineRepository.findByUserLoginAndTypeCatego(getCurrentUserLoginn(), "Revenus");
        try {
            JSONObject jo22 = new JSONObject();
            jo22.put("x", LocalDate.now());
            jo22.put("y", 0F);
            dataR.put(jo22);
            for (HistoryLine hist2 : histo2) {
                JSONObject jo = new JSONObject();
                jo.put("x", hist2.getDateModif());
                jo.put("y", hist2.getRevenuschart());
                dataR.put(jo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray JA = new JSONArray();
        try {
            JSONObject jo = new JSONObject();
            JSONObject jo2 = new JSONObject();
            jo.put("seriesName", "Depense");
            jo.put("data", dataD);
            jo.put("color", "#297AB1");
            JA.put(jo);
            jo2.put("seriesName", "Revenus");
            jo2.put("data", dataR);
            jo2.put("color", "yellow");
            JA.put(jo2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return JA.toString();
    }

    @GetMapping("/history-lines/revenusevolution")
    public String evolutionrevenus() {
        JSONArray dataR = new JSONArray();
        List<HistoryLine> histo2 = historyLineRepository.findByUserLoginAndTypeCatego(getCurrentUserLoginn(), "Revenus");
        try {
            for (HistoryLine hist2 : histo2) {
                JSONObject jo = new JSONObject();
                jo.put("x", hist2.getDateModif());
                jo.put("y", hist2.getRevenuschart());
                dataR.put(jo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataR.toString();
    }

    @GetMapping("/history-lines/depenseevolution")
    public String evolutiondepenses() {
        JSONArray dataR = new JSONArray();
        List<HistoryLine> histo2 = historyLineRepository.findByUserLoginAndTypeCatego(getCurrentUserLoginn(), "Depense");
        try {
            for (HistoryLine hist2 : histo2) {
                JSONObject jo = new JSONObject();
                jo.put("x", hist2.getDateModif());
                jo.put("y", hist2.getDepensechart());
                dataR.put(jo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataR.toString();
    }
}
