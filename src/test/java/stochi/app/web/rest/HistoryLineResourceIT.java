package stochi.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static stochi.app.web.rest.TestUtil.sameInstant;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
import stochi.app.domain.HistoryLine;
import stochi.app.repository.HistoryLineRepository;

/**
 * Integration tests for the {@link HistoryLineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HistoryLineResourceIT {

    private static final String DEFAULT_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_MODIF = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_MODIF = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Float DEFAULT_MONTANT = 1F;
    private static final Float UPDATED_MONTANT = 2F;

    private static final String DEFAULT_USER_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_USER_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE_CATEGO = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_CATEGO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/history-lines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private HistoryLineRepository historyLineRepository;

    @Autowired
    private MockMvc restHistoryLineMockMvc;

    private HistoryLine historyLine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoryLine createEntity() {
        HistoryLine historyLine = new HistoryLine()
            .categoryName(DEFAULT_CATEGORY_NAME)
            .dateModif(DEFAULT_DATE_MODIF)
            .montant(DEFAULT_MONTANT)
            .userLogin(DEFAULT_USER_LOGIN)
            .note(DEFAULT_NOTE)
            .typeCatego(DEFAULT_TYPE_CATEGO);
        return historyLine;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoryLine createUpdatedEntity() {
        HistoryLine historyLine = new HistoryLine()
            .categoryName(UPDATED_CATEGORY_NAME)
            .dateModif(UPDATED_DATE_MODIF)
            .montant(UPDATED_MONTANT)
            .userLogin(UPDATED_USER_LOGIN)
            .note(UPDATED_NOTE)
            .typeCatego(UPDATED_TYPE_CATEGO);
        return historyLine;
    }

    @BeforeEach
    public void initTest() {
        historyLineRepository.deleteAll();
        historyLine = createEntity();
    }

    @Test
    void createHistoryLine() throws Exception {
        int databaseSizeBeforeCreate = historyLineRepository.findAll().size();
        // Create the HistoryLine
        restHistoryLineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historyLine)))
            .andExpect(status().isCreated());

        // Validate the HistoryLine in the database
        List<HistoryLine> historyLineList = historyLineRepository.findAll();
        assertThat(historyLineList).hasSize(databaseSizeBeforeCreate + 1);
        HistoryLine testHistoryLine = historyLineList.get(historyLineList.size() - 1);
        assertThat(testHistoryLine.getCategoryName()).isEqualTo(DEFAULT_CATEGORY_NAME);
        assertThat(testHistoryLine.getDateModif()).isEqualTo(DEFAULT_DATE_MODIF);
        assertThat(testHistoryLine.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testHistoryLine.getUserLogin()).isEqualTo(DEFAULT_USER_LOGIN);
        assertThat(testHistoryLine.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testHistoryLine.getTypeCatego()).isEqualTo(DEFAULT_TYPE_CATEGO);
    }

    @Test
    void createHistoryLineWithExistingId() throws Exception {
        // Create the HistoryLine with an existing ID
        historyLine.setId("existing_id");

        int databaseSizeBeforeCreate = historyLineRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistoryLineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historyLine)))
            .andExpect(status().isBadRequest());

        // Validate the HistoryLine in the database
        List<HistoryLine> historyLineList = historyLineRepository.findAll();
        assertThat(historyLineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkCategoryNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = historyLineRepository.findAll().size();
        // set the field null
        historyLine.setCategoryName(null);

        // Create the HistoryLine, which fails.

        restHistoryLineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historyLine)))
            .andExpect(status().isBadRequest());

        List<HistoryLine> historyLineList = historyLineRepository.findAll();
        assertThat(historyLineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDateModifIsRequired() throws Exception {
        int databaseSizeBeforeTest = historyLineRepository.findAll().size();
        // set the field null
        historyLine.setDateModif(null);

        // Create the HistoryLine, which fails.

        restHistoryLineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historyLine)))
            .andExpect(status().isBadRequest());

        List<HistoryLine> historyLineList = historyLineRepository.findAll();
        assertThat(historyLineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkMontantIsRequired() throws Exception {
        int databaseSizeBeforeTest = historyLineRepository.findAll().size();
        // set the field null
        historyLine.setMontant(null);

        // Create the HistoryLine, which fails.

        restHistoryLineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historyLine)))
            .andExpect(status().isBadRequest());

        List<HistoryLine> historyLineList = historyLineRepository.findAll();
        assertThat(historyLineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllHistoryLines() throws Exception {
        // Initialize the database
        historyLineRepository.save(historyLine);

        // Get all the historyLineList
        restHistoryLineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historyLine.getId())))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].dateModif").value(hasItem(sameInstant(DEFAULT_DATE_MODIF))))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())))
            .andExpect(jsonPath("$.[*].userLogin").value(hasItem(DEFAULT_USER_LOGIN)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].typeCatego").value(hasItem(DEFAULT_TYPE_CATEGO)));
    }

    @Test
    void getHistoryLine() throws Exception {
        // Initialize the database
        historyLineRepository.save(historyLine);

        // Get the historyLine
        restHistoryLineMockMvc
            .perform(get(ENTITY_API_URL_ID, historyLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(historyLine.getId()))
            .andExpect(jsonPath("$.categoryName").value(DEFAULT_CATEGORY_NAME))
            .andExpect(jsonPath("$.dateModif").value(sameInstant(DEFAULT_DATE_MODIF)))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.doubleValue()))
            .andExpect(jsonPath("$.userLogin").value(DEFAULT_USER_LOGIN))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.typeCatego").value(DEFAULT_TYPE_CATEGO));
    }

    @Test
    void getNonExistingHistoryLine() throws Exception {
        // Get the historyLine
        restHistoryLineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewHistoryLine() throws Exception {
        // Initialize the database
        historyLineRepository.save(historyLine);

        int databaseSizeBeforeUpdate = historyLineRepository.findAll().size();

        // Update the historyLine
        HistoryLine updatedHistoryLine = historyLineRepository.findById(historyLine.getId()).get();
        updatedHistoryLine
            .categoryName(UPDATED_CATEGORY_NAME)
            .dateModif(UPDATED_DATE_MODIF)
            .montant(UPDATED_MONTANT)
            .userLogin(UPDATED_USER_LOGIN)
            .note(UPDATED_NOTE)
            .typeCatego(UPDATED_TYPE_CATEGO);

        restHistoryLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHistoryLine.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHistoryLine))
            )
            .andExpect(status().isOk());

        // Validate the HistoryLine in the database
        List<HistoryLine> historyLineList = historyLineRepository.findAll();
        assertThat(historyLineList).hasSize(databaseSizeBeforeUpdate);
        HistoryLine testHistoryLine = historyLineList.get(historyLineList.size() - 1);
        assertThat(testHistoryLine.getCategoryName()).isEqualTo(UPDATED_CATEGORY_NAME);
        assertThat(testHistoryLine.getDateModif()).isEqualTo(UPDATED_DATE_MODIF);
        assertThat(testHistoryLine.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testHistoryLine.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
        assertThat(testHistoryLine.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testHistoryLine.getTypeCatego()).isEqualTo(UPDATED_TYPE_CATEGO);
    }

    @Test
    void putNonExistingHistoryLine() throws Exception {
        int databaseSizeBeforeUpdate = historyLineRepository.findAll().size();
        historyLine.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoryLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historyLine.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historyLine))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoryLine in the database
        List<HistoryLine> historyLineList = historyLineRepository.findAll();
        assertThat(historyLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchHistoryLine() throws Exception {
        int databaseSizeBeforeUpdate = historyLineRepository.findAll().size();
        historyLine.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoryLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historyLine))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoryLine in the database
        List<HistoryLine> historyLineList = historyLineRepository.findAll();
        assertThat(historyLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamHistoryLine() throws Exception {
        int databaseSizeBeforeUpdate = historyLineRepository.findAll().size();
        historyLine.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoryLineMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historyLine)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HistoryLine in the database
        List<HistoryLine> historyLineList = historyLineRepository.findAll();
        assertThat(historyLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateHistoryLineWithPatch() throws Exception {
        // Initialize the database
        historyLineRepository.save(historyLine);

        int databaseSizeBeforeUpdate = historyLineRepository.findAll().size();

        // Update the historyLine using partial update
        HistoryLine partialUpdatedHistoryLine = new HistoryLine();
        partialUpdatedHistoryLine.setId(historyLine.getId());

        partialUpdatedHistoryLine.dateModif(UPDATED_DATE_MODIF).montant(UPDATED_MONTANT).note(UPDATED_NOTE).typeCatego(UPDATED_TYPE_CATEGO);

        restHistoryLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoryLine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHistoryLine))
            )
            .andExpect(status().isOk());

        // Validate the HistoryLine in the database
        List<HistoryLine> historyLineList = historyLineRepository.findAll();
        assertThat(historyLineList).hasSize(databaseSizeBeforeUpdate);
        HistoryLine testHistoryLine = historyLineList.get(historyLineList.size() - 1);
        assertThat(testHistoryLine.getCategoryName()).isEqualTo(DEFAULT_CATEGORY_NAME);
        assertThat(testHistoryLine.getDateModif()).isEqualTo(UPDATED_DATE_MODIF);
        assertThat(testHistoryLine.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testHistoryLine.getUserLogin()).isEqualTo(DEFAULT_USER_LOGIN);
        assertThat(testHistoryLine.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testHistoryLine.getTypeCatego()).isEqualTo(UPDATED_TYPE_CATEGO);
    }

    @Test
    void fullUpdateHistoryLineWithPatch() throws Exception {
        // Initialize the database
        historyLineRepository.save(historyLine);

        int databaseSizeBeforeUpdate = historyLineRepository.findAll().size();

        // Update the historyLine using partial update
        HistoryLine partialUpdatedHistoryLine = new HistoryLine();
        partialUpdatedHistoryLine.setId(historyLine.getId());

        partialUpdatedHistoryLine
            .categoryName(UPDATED_CATEGORY_NAME)
            .dateModif(UPDATED_DATE_MODIF)
            .montant(UPDATED_MONTANT)
            .userLogin(UPDATED_USER_LOGIN)
            .note(UPDATED_NOTE)
            .typeCatego(UPDATED_TYPE_CATEGO);

        restHistoryLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoryLine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHistoryLine))
            )
            .andExpect(status().isOk());

        // Validate the HistoryLine in the database
        List<HistoryLine> historyLineList = historyLineRepository.findAll();
        assertThat(historyLineList).hasSize(databaseSizeBeforeUpdate);
        HistoryLine testHistoryLine = historyLineList.get(historyLineList.size() - 1);
        assertThat(testHistoryLine.getCategoryName()).isEqualTo(UPDATED_CATEGORY_NAME);
        assertThat(testHistoryLine.getDateModif()).isEqualTo(UPDATED_DATE_MODIF);
        assertThat(testHistoryLine.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testHistoryLine.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
        assertThat(testHistoryLine.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testHistoryLine.getTypeCatego()).isEqualTo(UPDATED_TYPE_CATEGO);
    }

    @Test
    void patchNonExistingHistoryLine() throws Exception {
        int databaseSizeBeforeUpdate = historyLineRepository.findAll().size();
        historyLine.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoryLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, historyLine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(historyLine))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoryLine in the database
        List<HistoryLine> historyLineList = historyLineRepository.findAll();
        assertThat(historyLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchHistoryLine() throws Exception {
        int databaseSizeBeforeUpdate = historyLineRepository.findAll().size();
        historyLine.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoryLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(historyLine))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoryLine in the database
        List<HistoryLine> historyLineList = historyLineRepository.findAll();
        assertThat(historyLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamHistoryLine() throws Exception {
        int databaseSizeBeforeUpdate = historyLineRepository.findAll().size();
        historyLine.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoryLineMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(historyLine))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HistoryLine in the database
        List<HistoryLine> historyLineList = historyLineRepository.findAll();
        assertThat(historyLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteHistoryLine() throws Exception {
        // Initialize the database
        historyLineRepository.save(historyLine);

        int databaseSizeBeforeDelete = historyLineRepository.findAll().size();

        // Delete the historyLine
        restHistoryLineMockMvc
            .perform(delete(ENTITY_API_URL_ID, historyLine.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HistoryLine> historyLineList = historyLineRepository.findAll();
        assertThat(historyLineList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
