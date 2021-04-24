package stochi.app.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import stochi.app.domain.HistoryLine;

/**
 * Service Interface for managing {@link HistoryLine}.
 */
public interface HistoryLineService {
    /**
     * Save a historyLine.
     *
     * @param historyLine the entity to save.
     * @return the persisted entity.
     */
    HistoryLine save(HistoryLine historyLine);

    /**
     * Partially updates a historyLine.
     *
     * @param historyLine the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HistoryLine> partialUpdate(HistoryLine historyLine);

    /**
     * Get all the historyLines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HistoryLine> findAll(Pageable pageable);

    /**
     * Get the "id" historyLine.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HistoryLine> findOne(String id);

    /**
     * Delete the "id" historyLine.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
