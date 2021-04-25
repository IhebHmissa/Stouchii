package stochi.app.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import stochi.app.domain.Category;

/**
 * Spring Data MongoDB repository for the Category entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    List<Category> findByUserLoginAndOriginType(String login, String type);
    Category findOneByUserLoginAndNameCatego(String login, String namecatego);
    List<Category> findByUserLoginAndType(String login, String type);
    List<Category> findByUserLogin(String login);
    List<Category> findByUserLoginAndTypeAndOriginType(String login, String type, String origin);
}
