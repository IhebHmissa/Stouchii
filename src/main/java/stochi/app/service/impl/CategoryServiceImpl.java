package stochi.app.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import stochi.app.domain.Category;
import stochi.app.repository.CategoryRepository;
import stochi.app.service.CategoryService;

/**
 * Service Implementation for managing {@link Category}.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category save(Category category) {
        log.debug("Request to save Category : {}", category);
        return categoryRepository.save(category);
    }

    @Override
    public Optional<Category> partialUpdate(Category category) {
        log.debug("Request to partially update Category : {}", category);

        return categoryRepository
            .findById(category.getId())
            .map(
                existingCategory -> {
                    if (category.getType() != null) {
                        existingCategory.setType(category.getType());
                    }
                    if (category.getNameCatego() != null) {
                        existingCategory.setNameCatego(category.getNameCatego());
                    }
                    if (category.getOriginType() != null) {
                        existingCategory.setOriginType(category.getOriginType());
                    }
                    if (category.getMontant() != null) {
                        existingCategory.setMontant(category.getMontant());
                    }
                    if (category.getColor() != null) {
                        existingCategory.setColor(category.getColor());
                    }
                    if (category.getUserLogin() != null) {
                        existingCategory.setUserLogin(category.getUserLogin());
                    }
                    if (category.getMinMontant() != null) {
                        existingCategory.setMinMontant(category.getMinMontant());
                    }
                    if (category.getMaxMontant() != null) {
                        existingCategory.setMaxMontant(category.getMaxMontant());
                    }
                    if (category.getPeriodicty() != null) {
                        existingCategory.setPeriodicty(category.getPeriodicty());
                    }

                    return existingCategory;
                }
            )
            .map(categoryRepository::save);
    }

    @Override
    public Page<Category> findAll(Pageable pageable) {
        log.debug("Request to get all Categories");
        return categoryRepository.findAll(pageable);
    }

    @Override
    public Optional<Category> findOne(String id) {
        log.debug("Request to get Category : {}", id);
        return categoryRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Category : {}", id);
        categoryRepository.deleteById(id);
    }
}
