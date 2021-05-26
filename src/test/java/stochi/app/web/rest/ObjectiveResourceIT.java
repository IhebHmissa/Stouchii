package stochi.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import stochi.app.domain.Objective;
import stochi.app.repository.ObjectiveRepository;

/**
 * Integration tests for the {@link ObjectiveResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ObjectiveResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String DEFAULT_USER_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_USER_LOGIN = "BBBBBBBBBB";

    private static final Float DEFAULT_AMOUNT_TOT = 1F;
    private static final Float UPDATED_AMOUNT_TOT = 2F;

    private static final Float DEFAULT_AMOUNT_VAR = 1F;
    private static final Float UPDATED_AMOUNT_VAR = 2F;

    private static final String ENTITY_API_URL = "/api/objectives";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectiveRepository objectiveRepository;

    @Autowired
    private MockMvc restObjectiveMockMvc;

    private Objective objective;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Objective createEntity() {
        Objective objective = new Objective()
            .name(DEFAULT_NAME)
            .note(DEFAULT_NOTE)
            .userLogin(DEFAULT_USER_LOGIN)
            .amountTot(DEFAULT_AMOUNT_TOT)
            .amountVar(DEFAULT_AMOUNT_VAR);
        return objective;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Objective createUpdatedEntity() {
        Objective objective = new Objective()
            .name(UPDATED_NAME)
            .note(UPDATED_NOTE)
            .userLogin(UPDATED_USER_LOGIN)
            .amountTot(UPDATED_AMOUNT_TOT)
            .amountVar(UPDATED_AMOUNT_VAR);
        return objective;
    }

    @BeforeEach
    public void initTest() {
        objectiveRepository.deleteAll();
        objective = createEntity();
    }

    @Test
    void createObjective() throws Exception {
        int databaseSizeBeforeCreate = objectiveRepository.findAll().size();
        // Create the Objective
        restObjectiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(objective)))
            .andExpect(status().isCreated());

        // Validate the Objective in the database
        List<Objective> objectiveList = objectiveRepository.findAll();
        assertThat(objectiveList).hasSize(databaseSizeBeforeCreate + 1);
        Objective testObjective = objectiveList.get(objectiveList.size() - 1);
        assertThat(testObjective.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testObjective.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testObjective.getUserLogin()).isEqualTo(DEFAULT_USER_LOGIN);
        assertThat(testObjective.getAmountTot()).isEqualTo(DEFAULT_AMOUNT_TOT);
        assertThat(testObjective.getAmountVar()).isEqualTo(DEFAULT_AMOUNT_VAR);
    }

    @Test
    void createObjectiveWithExistingId() throws Exception {
        // Create the Objective with an existing ID
        objective.setId("existing_id");

        int databaseSizeBeforeCreate = objectiveRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restObjectiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(objective)))
            .andExpect(status().isBadRequest());

        // Validate the Objective in the database
        List<Objective> objectiveList = objectiveRepository.findAll();
        assertThat(objectiveList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllObjectives() throws Exception {
        // Initialize the database
        objectiveRepository.save(objective);

        // Get all the objectiveList
        restObjectiveMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(objective.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].userLogin").value(hasItem(DEFAULT_USER_LOGIN)))
            .andExpect(jsonPath("$.[*].amountTot").value(hasItem(DEFAULT_AMOUNT_TOT.doubleValue())))
            .andExpect(jsonPath("$.[*].amountVar").value(hasItem(DEFAULT_AMOUNT_VAR.doubleValue())));
    }

    @Test
    void getObjective() throws Exception {
        // Initialize the database
        objectiveRepository.save(objective);

        // Get the objective
        restObjectiveMockMvc
            .perform(get(ENTITY_API_URL_ID, objective.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(objective.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.userLogin").value(DEFAULT_USER_LOGIN))
            .andExpect(jsonPath("$.amountTot").value(DEFAULT_AMOUNT_TOT.doubleValue()))
            .andExpect(jsonPath("$.amountVar").value(DEFAULT_AMOUNT_VAR.doubleValue()));
    }

    @Test
    void getNonExistingObjective() throws Exception {
        // Get the objective
        restObjectiveMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewObjective() throws Exception {
        // Initialize the database
        objectiveRepository.save(objective);

        int databaseSizeBeforeUpdate = objectiveRepository.findAll().size();

        // Update the objective
        Objective updatedObjective = objectiveRepository.findById(objective.getId()).get();
        updatedObjective
            .name(UPDATED_NAME)
            .note(UPDATED_NOTE)
            .userLogin(UPDATED_USER_LOGIN)
            .amountTot(UPDATED_AMOUNT_TOT)
            .amountVar(UPDATED_AMOUNT_VAR);

        restObjectiveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedObjective.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedObjective))
            )
            .andExpect(status().isOk());

        // Validate the Objective in the database
        List<Objective> objectiveList = objectiveRepository.findAll();
        assertThat(objectiveList).hasSize(databaseSizeBeforeUpdate);
        Objective testObjective = objectiveList.get(objectiveList.size() - 1);
        assertThat(testObjective.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testObjective.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testObjective.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
        assertThat(testObjective.getAmountTot()).isEqualTo(UPDATED_AMOUNT_TOT);
        assertThat(testObjective.getAmountVar()).isEqualTo(UPDATED_AMOUNT_VAR);
    }

    @Test
    void putNonExistingObjective() throws Exception {
        int databaseSizeBeforeUpdate = objectiveRepository.findAll().size();
        objective.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restObjectiveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, objective.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(objective))
            )
            .andExpect(status().isBadRequest());

        // Validate the Objective in the database
        List<Objective> objectiveList = objectiveRepository.findAll();
        assertThat(objectiveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchObjective() throws Exception {
        int databaseSizeBeforeUpdate = objectiveRepository.findAll().size();
        objective.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restObjectiveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(objective))
            )
            .andExpect(status().isBadRequest());

        // Validate the Objective in the database
        List<Objective> objectiveList = objectiveRepository.findAll();
        assertThat(objectiveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamObjective() throws Exception {
        int databaseSizeBeforeUpdate = objectiveRepository.findAll().size();
        objective.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restObjectiveMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(objective)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Objective in the database
        List<Objective> objectiveList = objectiveRepository.findAll();
        assertThat(objectiveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateObjectiveWithPatch() throws Exception {
        // Initialize the database
        objectiveRepository.save(objective);

        int databaseSizeBeforeUpdate = objectiveRepository.findAll().size();

        // Update the objective using partial update
        Objective partialUpdatedObjective = new Objective();
        partialUpdatedObjective.setId(objective.getId());

        partialUpdatedObjective.name(UPDATED_NAME).amountVar(UPDATED_AMOUNT_VAR);

        restObjectiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedObjective.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedObjective))
            )
            .andExpect(status().isOk());

        // Validate the Objective in the database
        List<Objective> objectiveList = objectiveRepository.findAll();
        assertThat(objectiveList).hasSize(databaseSizeBeforeUpdate);
        Objective testObjective = objectiveList.get(objectiveList.size() - 1);
        assertThat(testObjective.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testObjective.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testObjective.getUserLogin()).isEqualTo(DEFAULT_USER_LOGIN);
        assertThat(testObjective.getAmountTot()).isEqualTo(DEFAULT_AMOUNT_TOT);
        assertThat(testObjective.getAmountVar()).isEqualTo(UPDATED_AMOUNT_VAR);
    }

    @Test
    void fullUpdateObjectiveWithPatch() throws Exception {
        // Initialize the database
        objectiveRepository.save(objective);

        int databaseSizeBeforeUpdate = objectiveRepository.findAll().size();

        // Update the objective using partial update
        Objective partialUpdatedObjective = new Objective();
        partialUpdatedObjective.setId(objective.getId());

        partialUpdatedObjective
            .name(UPDATED_NAME)
            .note(UPDATED_NOTE)
            .userLogin(UPDATED_USER_LOGIN)
            .amountTot(UPDATED_AMOUNT_TOT)
            .amountVar(UPDATED_AMOUNT_VAR);

        restObjectiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedObjective.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedObjective))
            )
            .andExpect(status().isOk());

        // Validate the Objective in the database
        List<Objective> objectiveList = objectiveRepository.findAll();
        assertThat(objectiveList).hasSize(databaseSizeBeforeUpdate);
        Objective testObjective = objectiveList.get(objectiveList.size() - 1);
        assertThat(testObjective.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testObjective.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testObjective.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
        assertThat(testObjective.getAmountTot()).isEqualTo(UPDATED_AMOUNT_TOT);
        assertThat(testObjective.getAmountVar()).isEqualTo(UPDATED_AMOUNT_VAR);
    }

    @Test
    void patchNonExistingObjective() throws Exception {
        int databaseSizeBeforeUpdate = objectiveRepository.findAll().size();
        objective.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restObjectiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, objective.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(objective))
            )
            .andExpect(status().isBadRequest());

        // Validate the Objective in the database
        List<Objective> objectiveList = objectiveRepository.findAll();
        assertThat(objectiveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchObjective() throws Exception {
        int databaseSizeBeforeUpdate = objectiveRepository.findAll().size();
        objective.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restObjectiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(objective))
            )
            .andExpect(status().isBadRequest());

        // Validate the Objective in the database
        List<Objective> objectiveList = objectiveRepository.findAll();
        assertThat(objectiveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamObjective() throws Exception {
        int databaseSizeBeforeUpdate = objectiveRepository.findAll().size();
        objective.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restObjectiveMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(objective))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Objective in the database
        List<Objective> objectiveList = objectiveRepository.findAll();
        assertThat(objectiveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteObjective() throws Exception {
        // Initialize the database
        objectiveRepository.save(objective);

        int databaseSizeBeforeDelete = objectiveRepository.findAll().size();

        // Delete the objective
        restObjectiveMockMvc
            .perform(delete(ENTITY_API_URL_ID, objective.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Objective> objectiveList = objectiveRepository.findAll();
        assertThat(objectiveList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
