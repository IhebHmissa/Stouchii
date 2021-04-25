package stochi.app.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import stochi.app.domain.Category;

/**
 * Service Interface for managing {@link Category}.
 */
public interface CategoryService {
    Category save(Category category);

    Optional<Category> partialUpdate(Category category);

    Page<Category> findAll(Pageable pageable);

    Optional<Category> findOne(String id);

    void delete(String id);
}
