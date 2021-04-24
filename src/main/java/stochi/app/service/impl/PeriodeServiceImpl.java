package stochi.app.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import stochi.app.domain.Periode;
import stochi.app.repository.PeriodeRepository;
import stochi.app.service.PeriodeService;

/**
 * Service Implementation for managing {@link Periode}.
 */
@Service
public class PeriodeServiceImpl implements PeriodeService {

    private final Logger log = LoggerFactory.getLogger(PeriodeServiceImpl.class);

    private final PeriodeRepository periodeRepository;

    public PeriodeServiceImpl(PeriodeRepository periodeRepository) {
        this.periodeRepository = periodeRepository;
    }

    @Override
    public Periode save(Periode periode) {
        log.debug("Request to save Periode : {}", periode);
        return periodeRepository.save(periode);
    }

    @Override
    public Optional<Periode> partialUpdate(Periode periode) {
        log.debug("Request to partially update Periode : {}", periode);

        return periodeRepository
            .findById(periode.getId())
            .map(
                existingPeriode -> {
                    if (periode.getDateDeb() != null) {
                        existingPeriode.setDateDeb(periode.getDateDeb());
                    }
                    if (periode.getDateFin() != null) {
                        existingPeriode.setDateFin(periode.getDateFin());
                    }
                    if (periode.getFrequancy() != null) {
                        existingPeriode.setFrequancy(periode.getFrequancy());
                    }
                    if (periode.getFixedMontant() != null) {
                        existingPeriode.setFixedMontant(periode.getFixedMontant());
                    }
                    if (periode.getNumberleft() != null) {
                        existingPeriode.setNumberleft(periode.getNumberleft());
                    }
                    if (periode.getTypeCatego() != null) {
                        existingPeriode.setTypeCatego(periode.getTypeCatego());
                    }

                    return existingPeriode;
                }
            )
            .map(periodeRepository::save);
    }

    @Override
    public Page<Periode> findAll(Pageable pageable) {
        log.debug("Request to get all Periodes");
        return periodeRepository.findAll(pageable);
    }

    @Override
    public Optional<Periode> findOne(String id) {
        log.debug("Request to get Periode : {}", id);
        return periodeRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Periode : {}", id);
        periodeRepository.deleteById(id);
    }
}
