package blog.areas.category.Services;

import blog.areas.category.entity.Category;

import java.util.List;

public interface CategoryServices {
    List<Category> getAllCategories();
    boolean doesCategoryExists(final Integer id);
    Category getCategory(final Integer id);
}
