package stochi.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.*;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import stochi.app.IntegrationTest;
import stochi.app.domain.Periode;
import stochi.app.repository.PeriodeRepository;

/**
 * Integration tests for the {@link PeriodeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PeriodeResourceIT {

    private static final LocalDate DEFAULT_DATE_DEB = LocalDate.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final LocalDate UPDATED_DATE_DEB = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_FREQUANCY = "AAAAAAAAAA";
    private static final String UPDATED_FREQUANCY = "BBBBBBBBBB";

    private static final Float DEFAULT_FIXED_MONTANT = 1F;
    private static final Float UPDATED_FIXED_MONTANT = 2F;

    private static final Long DEFAULT_NUMBERLEFT = 1L;
    private static final Long UPDATED_NUMBERLEFT = 2L;

    private static final String DEFAULT_TYPE_CATEGO = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_CATEGO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/periodes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private PeriodeRepository periodeRepository;

    @Autowired
    private MockMvc restPeriodeMockMvc;

    private Periode periode;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Periode createEntity() {
        Periode periode = new Periode()
            .dateDeb(DEFAULT_DATE_DEB)
            .dateFin(DEFAULT_DATE_FIN)
            .frequancy(DEFAULT_FREQUANCY)
            .fixedMontant(DEFAULT_FIXED_MONTANT)
            .numberleft(DEFAULT_NUMBERLEFT)
            .typeCatego(DEFAULT_TYPE_CATEGO);
        return periode;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Periode createUpdatedEntity() {
        Periode periode = new Periode()
            .dateDeb(UPDATED_DATE_DEB)
            .dateFin(UPDATED_DATE_FIN)
            .frequancy(UPDATED_FREQUANCY)
            .fixedMontant(UPDATED_FIXED_MONTANT)
            .numberleft(UPDATED_NUMBERLEFT)
            .typeCatego(UPDATED_TYPE_CATEGO);
        return periode;
    }

    @BeforeEach
    public void initTest() {
        periodeRepository.deleteAll();
        periode = createEntity();
    }

    @Test
    void createPeriode() throws Exception {
        int databaseSizeBeforeCreate = periodeRepository.findAll().size();
        // Create the Periode
        restPeriodeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(periode)))
            .andExpect(status().isCreated());

        // Validate the Periode in the database
        List<Periode> periodeList = periodeRepository.findAll();
        assertThat(periodeList).hasSize(databaseSizeBeforeCreate + 1);
        Periode testPeriode = periodeList.get(periodeList.size() - 1);
        assertThat(testPeriode.getDateDeb()).isEqualTo(DEFAULT_DATE_DEB);
        assertThat(testPeriode.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testPeriode.getFrequancy()).isEqualTo(DEFAULT_FREQUANCY);
        assertThat(testPeriode.getFixedMontant()).isEqualTo(DEFAULT_FIXED_MONTANT);
        assertThat(testPeriode.getNumberleft()).isEqualTo(DEFAULT_NUMBERLEFT);
        assertThat(testPeriode.getTypeCatego()).isEqualTo(DEFAULT_TYPE_CATEGO);
    }

    @Test
    void createPeriodeWithExistingId() throws Exception {
        // Create the Periode with an existing ID
        periode.setId("existing_id");

        int databaseSizeBeforeCreate = periodeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPeriodeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(periode)))
            .andExpect(status().isBadRequest());

        // Validate the Periode in the database
        List<Periode> periodeList = periodeRepository.findAll();
        assertThat(periodeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllPeriodes() throws Exception {
        // Initialize the database
        periodeRepository.save(periode);

        // Get all the periodeList
        restPeriodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(periode.getId())))
            .andExpect(jsonPath("$.[*].frequancy").value(hasItem(DEFAULT_FREQUANCY)))
            .andExpect(jsonPath("$.[*].fixedMontant").value(hasItem(DEFAULT_FIXED_MONTANT.doubleValue())))
            .andExpect(jsonPath("$.[*].numberleft").value(hasItem(DEFAULT_NUMBERLEFT.intValue())))
            .andExpect(jsonPath("$.[*].typeCatego").value(hasItem(DEFAULT_TYPE_CATEGO)));
    }

    @Test
    void getPeriode() throws Exception {
        // Initialize the database
        periodeRepository.save(periode);

        // Get the periode
        restPeriodeMockMvc
            .perform(get(ENTITY_API_URL_ID, periode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(periode.getId()))
            .andExpect(jsonPath("$.frequancy").value(DEFAULT_FREQUANCY))
            .andExpect(jsonPath("$.fixedMontant").value(DEFAULT_FIXED_MONTANT.doubleValue()))
            .andExpect(jsonPath("$.numberleft").value(DEFAULT_NUMBERLEFT.intValue()))
            .andExpect(jsonPath("$.typeCatego").value(DEFAULT_TYPE_CATEGO));
    }

    @Test
    void getNonExistingPeriode() throws Exception {
        // Get the periode
        restPeriodeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewPeriode() throws Exception {
        // Initialize the database
        periodeRepository.save(periode);

        int databaseSizeBeforeUpdate = periodeRepository.findAll().size();

        // Update the periode
        Periode updatedPeriode = periodeRepository.findById(periode.getId()).get();
        updatedPeriode
            .dateDeb(UPDATED_DATE_DEB)
            .dateFin(UPDATED_DATE_FIN)
            .frequancy(UPDATED_FREQUANCY)
            .fixedMontant(UPDATED_FIXED_MONTANT)
            .numberleft(UPDATED_NUMBERLEFT)
            .typeCatego(UPDATED_TYPE_CATEGO);

        restPeriodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPeriode.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPeriode))
            )
            .andExpect(status().isOk());

        // Validate the Periode in the database
        List<Periode> periodeList = periodeRepository.findAll();
        assertThat(periodeList).hasSize(databaseSizeBeforeUpdate);
        Periode testPeriode = periodeList.get(periodeList.size() - 1);
        assertThat(testPeriode.getDateDeb()).isEqualTo(UPDATED_DATE_DEB);
        assertThat(testPeriode.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testPeriode.getFrequancy()).isEqualTo(UPDATED_FREQUANCY);
        assertThat(testPeriode.getFixedMontant()).isEqualTo(UPDATED_FIXED_MONTANT);
        assertThat(testPeriode.getNumberleft()).isEqualTo(UPDATED_NUMBERLEFT);
        assertThat(testPeriode.getTypeCatego()).isEqualTo(UPDATED_TYPE_CATEGO);
    }

    @Test
    void putNonExistingPeriode() throws Exception {
        int databaseSizeBeforeUpdate = periodeRepository.findAll().size();
        periode.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeriodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, periode.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(periode))
            )
            .andExpect(status().isBadRequest());

        // Validate the Periode in the database
        List<Periode> periodeList = periodeRepository.findAll();
        assertThat(periodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPeriode() throws Exception {
        int databaseSizeBeforeUpdate = periodeRepository.findAll().size();
        periode.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeriodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(periode))
            )
            .andExpect(status().isBadRequest());

        // Validate the Periode in the database
        List<Periode> periodeList = periodeRepository.findAll();
        assertThat(periodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPeriode() throws Exception {
        int databaseSizeBeforeUpdate = periodeRepository.findAll().size();
        periode.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeriodeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(periode)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Periode in the database
        List<Periode> periodeList = periodeRepository.findAll();
        assertThat(periodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePeriodeWithPatch() throws Exception {
        // Initialize the database
        periodeRepository.save(periode);

        int databaseSizeBeforeUpdate = periodeRepository.findAll().size();

        // Update the periode using partial update
        Periode partialUpdatedPeriode = new Periode();
        partialUpdatedPeriode.setId(periode.getId());

        partialUpdatedPeriode.frequancy(UPDATED_FREQUANCY).numberleft(UPDATED_NUMBERLEFT).typeCatego(UPDATED_TYPE_CATEGO);

        restPeriodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPeriode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPeriode))
            )
            .andExpect(status().isOk());

        // Validate the Periode in the database
        List<Periode> periodeList = periodeRepository.findAll();
        assertThat(periodeList).hasSize(databaseSizeBeforeUpdate);
        Periode testPeriode = periodeList.get(periodeList.size() - 1);
        assertThat(testPeriode.getDateDeb()).isEqualTo(DEFAULT_DATE_DEB);
        assertThat(testPeriode.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testPeriode.getFrequancy()).isEqualTo(UPDATED_FREQUANCY);
        assertThat(testPeriode.getFixedMontant()).isEqualTo(DEFAULT_FIXED_MONTANT);
        assertThat(testPeriode.getNumberleft()).isEqualTo(UPDATED_NUMBERLEFT);
        assertThat(testPeriode.getTypeCatego()).isEqualTo(UPDATED_TYPE_CATEGO);
    }

    @Test
    void fullUpdatePeriodeWithPatch() throws Exception {
        // Initialize the database
        periodeRepository.save(periode);

        int databaseSizeBeforeUpdate = periodeRepository.findAll().size();

        // Update the periode using partial update
        Periode partialUpdatedPeriode = new Periode();
        partialUpdatedPeriode.setId(periode.getId());

        partialUpdatedPeriode
            .dateDeb(UPDATED_DATE_DEB)
            .dateFin(UPDATED_DATE_FIN)
            .frequancy(UPDATED_FREQUANCY)
            .fixedMontant(UPDATED_FIXED_MONTANT)
            .numberleft(UPDATED_NUMBERLEFT)
            .typeCatego(UPDATED_TYPE_CATEGO);

        restPeriodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPeriode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPeriode))
            )
            .andExpect(status().isOk());

        // Validate the Periode in the database
        List<Periode> periodeList = periodeRepository.findAll();
        assertThat(periodeList).hasSize(databaseSizeBeforeUpdate);
        Periode testPeriode = periodeList.get(periodeList.size() - 1);
        assertThat(testPeriode.getDateDeb()).isEqualTo(UPDATED_DATE_DEB);
        assertThat(testPeriode.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testPeriode.getFrequancy()).isEqualTo(UPDATED_FREQUANCY);
        assertThat(testPeriode.getFixedMontant()).isEqualTo(UPDATED_FIXED_MONTANT);
        assertThat(testPeriode.getNumberleft()).isEqualTo(UPDATED_NUMBERLEFT);
        assertThat(testPeriode.getTypeCatego()).isEqualTo(UPDATED_TYPE_CATEGO);
    }

    @Test
    void patchNonExistingPeriode() throws Exception {
        int databaseSizeBeforeUpdate = periodeRepository.findAll().size();
        periode.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeriodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, periode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(periode))
            )
            .andExpect(status().isBadRequest());

        // Validate the Periode in the database
        List<Periode> periodeList = periodeRepository.findAll();
        assertThat(periodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPeriode() throws Exception {
        int databaseSizeBeforeUpdate = periodeRepository.findAll().size();
        periode.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeriodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(periode))
            )
            .andExpect(status().isBadRequest());

        // Validate the Periode in the database
        List<Periode> periodeList = periodeRepository.findAll();
        assertThat(periodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPeriode() throws Exception {
        int databaseSizeBeforeUpdate = periodeRepository.findAll().size();
        periode.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeriodeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(periode)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Periode in the database
        List<Periode> periodeList = periodeRepository.findAll();
        assertThat(periodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePeriode() throws Exception {
        // Initialize the database
        periodeRepository.save(periode);

        int databaseSizeBeforeDelete = periodeRepository.findAll().size();

        // Delete the periode
        restPeriodeMockMvc
            .perform(delete(ENTITY_API_URL_ID, periode.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Periode> periodeList = periodeRepository.findAll();
        assertThat(periodeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
