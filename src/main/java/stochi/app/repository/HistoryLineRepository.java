package stochi.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import stochi.app.domain.HistoryLine;

/**
 * Spring Data MongoDB repository for the HistoryLine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HistoryLineRepository extends MongoRepository<HistoryLine, String> {}
