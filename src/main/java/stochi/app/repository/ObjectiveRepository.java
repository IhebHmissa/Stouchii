package stochi.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import stochi.app.domain.Objective;

/**
 * Spring Data MongoDB repository for the Objective entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ObjectiveRepository extends MongoRepository<Objective, String> {}
