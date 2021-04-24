package stochi.app.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import stochi.app.domain.HistoryLine;
import stochi.app.repository.HistoryLineRepository;
import stochi.app.service.HistoryLineService;

/**
 * Service Implementation for managing {@link HistoryLine}.
 */
@Service
public class HistoryLineServiceImpl implements HistoryLineService {

    private final Logger log = LoggerFactory.getLogger(HistoryLineServiceImpl.class);

    private final HistoryLineRepository historyLineRepository;

    public HistoryLineServiceImpl(HistoryLineRepository historyLineRepository) {
        this.historyLineRepository = historyLineRepository;
    }

    @Override
    public HistoryLine save(HistoryLine historyLine) {
        log.debug("Request to save HistoryLine : {}", historyLine);
        return historyLineRepository.save(historyLine);
    }

    @Override
    public Optional<HistoryLine> partialUpdate(HistoryLine historyLine) {
        log.debug("Request to partially update HistoryLine : {}", historyLine);

        return historyLineRepository
            .findById(historyLine.getId())
            .map(
                existingHistoryLine -> {
                    if (historyLine.getCategoryName() != null) {
                        existingHistoryLine.setCategoryName(historyLine.getCategoryName());
                    }
                    if (historyLine.getDateModif() != null) {
                        existingHistoryLine.setDateModif(historyLine.getDateModif());
                    }
                    if (historyLine.getMontant() != null) {
                        existingHistoryLine.setMontant(historyLine.getMontant());
                    }
                    if (historyLine.getUserLogin() != null) {
                        existingHistoryLine.setUserLogin(historyLine.getUserLogin());
                    }
                    if (historyLine.getNote() != null) {
                        existingHistoryLine.setNote(historyLine.getNote());
                    }
                    if (historyLine.getTypeCatego() != null) {
                        existingHistoryLine.setTypeCatego(historyLine.getTypeCatego());
                    }

                    return existingHistoryLine;
                }
            )
            .map(historyLineRepository::save);
    }

    @Override
    public Page<HistoryLine> findAll(Pageable pageable) {
        log.debug("Request to get all HistoryLines");
        return historyLineRepository.findAll(pageable);
    }

    @Override
    public Optional<HistoryLine> findOne(String id) {
        log.debug("Request to get HistoryLine : {}", id);
        return historyLineRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete HistoryLine : {}", id);
        historyLineRepository.deleteById(id);
    }
}
