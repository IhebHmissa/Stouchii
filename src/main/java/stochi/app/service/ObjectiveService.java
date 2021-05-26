package stochi.app.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import stochi.app.domain.Objective;

/**
 * Service Interface for managing {@link Objective}.
 */
public interface ObjectiveService {
    /**
     * Save a objective.
     *
     * @param objective the entity to save.
     * @return the persisted entity.
     */
    Objective save(Objective objective);

    /**
     * Partially updates a objective.
     *
     * @param objective the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Objective> partialUpdate(Objective objective);

    /**
     * Get all the objectives.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Objective> findAll(Pageable pageable);

    /**
     * Get the "id" objective.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Objective> findOne(String id);

    /**
     * Delete the "id" objective.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
