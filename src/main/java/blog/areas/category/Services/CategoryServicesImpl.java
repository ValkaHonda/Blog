package blog.areas.category.Services;

import blog.areas.category.entity.Category;
import blog.areas.category.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServicesImpl implements CategoryServices{
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryServicesImpl(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        return this.categoryRepository.findAll();
    }

    @Override
    public boolean doesCategoryExists(final Integer id) {
        return this.categoryRepository.exists(id);
    }

    @Override
    public Category getCategory(final Integer id) {
        return this.categoryRepository.getOne(id);
    }
}
