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
import stochi.app.domain.Category;
import stochi.app.repository.CategoryRepository;

/**
 * Integration tests for the {@link CategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CategoryResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_CATEGO = "AAAAAAAAAA";
    private static final String UPDATED_NAME_CATEGO = "BBBBBBBBBB";

    private static final String DEFAULT_ORIGIN_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ORIGIN_TYPE = "BBBBBBBBBB";

    private static final Float DEFAULT_MONTANT = 1F;
    private static final Float UPDATED_MONTANT = 2F;

    private static final String DEFAULT_COLOR = "AAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBB";

    private static final String DEFAULT_USER_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_USER_LOGIN = "BBBBBBBBBB";

    private static final Float DEFAULT_MIN_MONTANT = 1F;
    private static final Float UPDATED_MIN_MONTANT = 2F;

    private static final Float DEFAULT_MAX_MONTANT = 1F;
    private static final Float UPDATED_MAX_MONTANT = 2F;

    private static final String DEFAULT_PERIODICTY = "AAAAAAAAAA";
    private static final String UPDATED_PERIODICTY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MockMvc restCategoryMockMvc;

    private Category category;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Category createEntity() {
        Category category = new Category()
            .type(DEFAULT_TYPE)
            .nameCatego(DEFAULT_NAME_CATEGO)
            .originType(DEFAULT_ORIGIN_TYPE)
            .montant(DEFAULT_MONTANT)
            .color(DEFAULT_COLOR)
            .userLogin(DEFAULT_USER_LOGIN)
            .minMontant(DEFAULT_MIN_MONTANT)
            .maxMontant(DEFAULT_MAX_MONTANT)
            .periodicty(DEFAULT_PERIODICTY);
        return category;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Category createUpdatedEntity() {
        Category category = new Category()
            .type(UPDATED_TYPE)
            .nameCatego(UPDATED_NAME_CATEGO)
            .originType(UPDATED_ORIGIN_TYPE)
            .montant(UPDATED_MONTANT)
            .color(UPDATED_COLOR)
            .userLogin(UPDATED_USER_LOGIN)
            .minMontant(UPDATED_MIN_MONTANT)
            .maxMontant(UPDATED_MAX_MONTANT)
            .periodicty(UPDATED_PERIODICTY);
        return category;
    }

    @BeforeEach
    public void initTest() {
        categoryRepository.deleteAll();
        category = createEntity();
    }

    @Test
    void createCategory() throws Exception {
        int databaseSizeBeforeCreate = categoryRepository.findAll().size();
        // Create the Category
        restCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(category)))
            .andExpect(status().isCreated());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeCreate + 1);
        Category testCategory = categoryList.get(categoryList.size() - 1);
        assertThat(testCategory.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCategory.getNameCatego()).isEqualTo(DEFAULT_NAME_CATEGO);
        assertThat(testCategory.getOriginType()).isEqualTo(DEFAULT_ORIGIN_TYPE);
        assertThat(testCategory.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testCategory.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testCategory.getUserLogin()).isEqualTo(DEFAULT_USER_LOGIN);
        assertThat(testCategory.getMinMontant()).isEqualTo(DEFAULT_MIN_MONTANT);
        assertThat(testCategory.getMaxMontant()).isEqualTo(DEFAULT_MAX_MONTANT);
        assertThat(testCategory.getPeriodicty()).isEqualTo(DEFAULT_PERIODICTY);
    }

    @Test
    void createCategoryWithExistingId() throws Exception {
        // Create the Category with an existing ID
        category.setId("existing_id");

        int databaseSizeBeforeCreate = categoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(category)))
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryRepository.findAll().size();
        // set the field null
        category.setType(null);

        // Create the Category, which fails.

        restCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(category)))
            .andExpect(status().isBadRequest());

        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkOriginTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryRepository.findAll().size();
        // set the field null
        category.setOriginType(null);

        // Create the Category, which fails.

        restCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(category)))
            .andExpect(status().isBadRequest());

        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkColorIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryRepository.findAll().size();
        // set the field null
        category.setColor(null);

        // Create the Category, which fails.

        restCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(category)))
            .andExpect(status().isBadRequest());

        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkUserLoginIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryRepository.findAll().size();
        // set the field null
        category.setUserLogin(null);

        // Create the Category, which fails.

        restCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(category)))
            .andExpect(status().isBadRequest());

        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllCategories() throws Exception {
        // Initialize the database
        categoryRepository.save(category);

        // Get all the categoryList
        restCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(category.getId())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].nameCatego").value(hasItem(DEFAULT_NAME_CATEGO)))
            .andExpect(jsonPath("$.[*].originType").value(hasItem(DEFAULT_ORIGIN_TYPE)))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].userLogin").value(hasItem(DEFAULT_USER_LOGIN)))
            .andExpect(jsonPath("$.[*].minMontant").value(hasItem(DEFAULT_MIN_MONTANT.doubleValue())))
            .andExpect(jsonPath("$.[*].maxMontant").value(hasItem(DEFAULT_MAX_MONTANT.doubleValue())))
            .andExpect(jsonPath("$.[*].periodicty").value(hasItem(DEFAULT_PERIODICTY)));
    }

    @Test
    void getCategory() throws Exception {
        // Initialize the database
        categoryRepository.save(category);

        // Get the category
        restCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, category.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(category.getId()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.nameCatego").value(DEFAULT_NAME_CATEGO))
            .andExpect(jsonPath("$.originType").value(DEFAULT_ORIGIN_TYPE))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.doubleValue()))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR))
            .andExpect(jsonPath("$.userLogin").value(DEFAULT_USER_LOGIN))
            .andExpect(jsonPath("$.minMontant").value(DEFAULT_MIN_MONTANT.doubleValue()))
            .andExpect(jsonPath("$.maxMontant").value(DEFAULT_MAX_MONTANT.doubleValue()))
            .andExpect(jsonPath("$.periodicty").value(DEFAULT_PERIODICTY));
    }

    @Test
    void getNonExistingCategory() throws Exception {
        // Get the category
        restCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewCategory() throws Exception {
        // Initialize the database
        categoryRepository.save(category);

        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();

        // Update the category
        Category updatedCategory = categoryRepository.findById(category.getId()).get();
        updatedCategory
            .type(UPDATED_TYPE)
            .nameCatego(UPDATED_NAME_CATEGO)
            .originType(UPDATED_ORIGIN_TYPE)
            .montant(UPDATED_MONTANT)
            .color(UPDATED_COLOR)
            .userLogin(UPDATED_USER_LOGIN)
            .minMontant(UPDATED_MIN_MONTANT)
            .maxMontant(UPDATED_MAX_MONTANT)
            .periodicty(UPDATED_PERIODICTY);

        restCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCategory))
            )
            .andExpect(status().isOk());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
        Category testCategory = categoryList.get(categoryList.size() - 1);
        assertThat(testCategory.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCategory.getNameCatego()).isEqualTo(UPDATED_NAME_CATEGO);
        assertThat(testCategory.getOriginType()).isEqualTo(UPDATED_ORIGIN_TYPE);
        assertThat(testCategory.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testCategory.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testCategory.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
        assertThat(testCategory.getMinMontant()).isEqualTo(UPDATED_MIN_MONTANT);
        assertThat(testCategory.getMaxMontant()).isEqualTo(UPDATED_MAX_MONTANT);
        assertThat(testCategory.getPeriodicty()).isEqualTo(UPDATED_PERIODICTY);
    }

    @Test
    void putNonExistingCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();
        category.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, category.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(category))
            )
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();
        category.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(category))
            )
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();
        category.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(category)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCategoryWithPatch() throws Exception {
        // Initialize the database
        categoryRepository.save(category);

        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();

        // Update the category using partial update
        Category partialUpdatedCategory = new Category();
        partialUpdatedCategory.setId(category.getId());

        partialUpdatedCategory.userLogin(UPDATED_USER_LOGIN).maxMontant(UPDATED_MAX_MONTANT);

        restCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategory))
            )
            .andExpect(status().isOk());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
        Category testCategory = categoryList.get(categoryList.size() - 1);
        assertThat(testCategory.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCategory.getNameCatego()).isEqualTo(DEFAULT_NAME_CATEGO);
        assertThat(testCategory.getOriginType()).isEqualTo(DEFAULT_ORIGIN_TYPE);
        assertThat(testCategory.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testCategory.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testCategory.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
        assertThat(testCategory.getMinMontant()).isEqualTo(DEFAULT_MIN_MONTANT);
        assertThat(testCategory.getMaxMontant()).isEqualTo(UPDATED_MAX_MONTANT);
        assertThat(testCategory.getPeriodicty()).isEqualTo(DEFAULT_PERIODICTY);
    }

    @Test
    void fullUpdateCategoryWithPatch() throws Exception {
        // Initialize the database
        categoryRepository.save(category);

        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();

        // Update the category using partial update
        Category partialUpdatedCategory = new Category();
        partialUpdatedCategory.setId(category.getId());

        partialUpdatedCategory
            .type(UPDATED_TYPE)
            .nameCatego(UPDATED_NAME_CATEGO)
            .originType(UPDATED_ORIGIN_TYPE)
            .montant(UPDATED_MONTANT)
            .color(UPDATED_COLOR)
            .userLogin(UPDATED_USER_LOGIN)
            .minMontant(UPDATED_MIN_MONTANT)
            .maxMontant(UPDATED_MAX_MONTANT)
            .periodicty(UPDATED_PERIODICTY);

        restCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategory))
            )
            .andExpect(status().isOk());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
        Category testCategory = categoryList.get(categoryList.size() - 1);
        assertThat(testCategory.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCategory.getNameCatego()).isEqualTo(UPDATED_NAME_CATEGO);
        assertThat(testCategory.getOriginType()).isEqualTo(UPDATED_ORIGIN_TYPE);
        assertThat(testCategory.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testCategory.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testCategory.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
        assertThat(testCategory.getMinMontant()).isEqualTo(UPDATED_MIN_MONTANT);
        assertThat(testCategory.getMaxMontant()).isEqualTo(UPDATED_MAX_MONTANT);
        assertThat(testCategory.getPeriodicty()).isEqualTo(UPDATED_PERIODICTY);
    }

    @Test
    void patchNonExistingCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();
        category.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, category.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(category))
            )
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();
        category.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(category))
            )
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();
        category.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(category)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCategory() throws Exception {
        // Initialize the database
        categoryRepository.save(category);

        int databaseSizeBeforeDelete = categoryRepository.findAll().size();

        // Delete the category
        restCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, category.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
