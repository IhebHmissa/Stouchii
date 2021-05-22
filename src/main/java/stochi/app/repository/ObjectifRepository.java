package stochi.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import stochi.app.domain.HistoryLine;

@Repository
public interface ObjectifRepository extends MongoRepository<HistoryLine, String> {}
