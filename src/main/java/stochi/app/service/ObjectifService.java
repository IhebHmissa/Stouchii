package stochi.app.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import stochi.app.domain.Objectif;

public interface ObjectifService {
    Objectif save(Objectif notification);

    Optional<Objectif> partialUpdate(Objectif notification);

    Page<Objectif> findAll(Pageable pageable);

    Optional<Objectif> findOne(String id);

    void delete(String id);
}
