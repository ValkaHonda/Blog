package blog.areas.admin.services;

import blog.areas.article.entities.Article;
import blog.areas.category.bindingModel.CategoryBindingModel;
import blog.areas.category.entity.Category;

import java.util.List;
import java.util.Set;

public interface AdminCategoryServices {
    List<Category> findAllCategories();
    List<Category> sortById(final List<Category> categories);
    void saveCategory(final Category category);
    Category findCategory(final Integer id);
    boolean doesCategoryExists(final Integer id);
    void deleteArticles(final Set<Article> articles);
    void deleteCategory(final Category category);
    void deleteCategory(final Integer id);
    void editCategory(final Integer id, final CategoryBindingModel categoryBindingModel);
}
