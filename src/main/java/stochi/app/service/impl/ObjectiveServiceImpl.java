package stochi.app.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import stochi.app.domain.Objective;
import stochi.app.repository.ObjectiveRepository;
import stochi.app.service.ObjectiveService;

/**
 * Service Implementation for managing {@link Objective}.
 */
@Service
public class ObjectiveServiceImpl implements ObjectiveService {

    private final Logger log = LoggerFactory.getLogger(ObjectiveServiceImpl.class);

    private final ObjectiveRepository objectiveRepository;

    public ObjectiveServiceImpl(ObjectiveRepository objectiveRepository) {
        this.objectiveRepository = objectiveRepository;
    }

    @Override
    public Objective save(Objective objective) {
        log.debug("Request to save Objective : {}", objective);
        return objectiveRepository.save(objective);
    }

    @Override
    public Optional<Objective> partialUpdate(Objective objective) {
        log.debug("Request to partially update Objective : {}", objective);

        return objectiveRepository
            .findById(objective.getId())
            .map(
                existingObjective -> {
                    if (objective.getName() != null) {
                        existingObjective.setName(objective.getName());
                    }
                    if (objective.getNote() != null) {
                        existingObjective.setNote(objective.getNote());
                    }
                    if (objective.getUserLogin() != null) {
                        existingObjective.setUserLogin(objective.getUserLogin());
                    }
                    if (objective.getAmountTot() != null) {
                        existingObjective.setAmountTot(objective.getAmountTot());
                    }
                    if (objective.getAmountVar() != null) {
                        existingObjective.setAmountVar(objective.getAmountVar());
                    }

                    return existingObjective;
                }
            )
            .map(objectiveRepository::save);
    }

    @Override
    public Page<Objective> findAll(Pageable pageable) {
        log.debug("Request to get all Objectives");
        return objectiveRepository.findAll(pageable);
    }

    @Override
    public Optional<Objective> findOne(String id) {
        log.debug("Request to get Objective : {}", id);
        return objectiveRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Objective : {}", id);
        objectiveRepository.deleteById(id);
    }
}
