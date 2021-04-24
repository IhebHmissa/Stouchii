package stochi.app.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import stochi.app.domain.Periode;

/**
 * Service Interface for managing {@link Periode}.
 */
public interface PeriodeService {
    /**
     * Save a periode.
     *
     * @param periode the entity to save.
     * @return the persisted entity.
     */
    Periode save(Periode periode);

    /**
     * Partially updates a periode.
     *
     * @param periode the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Periode> partialUpdate(Periode periode);

    /**
     * Get all the periodes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Periode> findAll(Pageable pageable);

    /**
     * Get the "id" periode.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Periode> findOne(String id);

    /**
     * Delete the "id" periode.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
