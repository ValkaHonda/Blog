package blog.areas.admin.services;

import blog.areas.article.entity.Article;
import blog.areas.article.repository.ArticleRepository;
import blog.areas.category.bindingModel.CategoryBindingModel;
import blog.areas.category.entity.Category;
import blog.areas.category.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdminCategoryServicesImpl implements AdminCategoryServices{
    private CategoryRepository categoryRepository;
    private ArticleRepository articleRepository;

    @Autowired
    public AdminCategoryServicesImpl(final CategoryRepository categoryRepository
            , final ArticleRepository articleRepository) {
        this.categoryRepository = categoryRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public List<Category> findAllCategories() {
        return this.categoryRepository.findAll();
    }

    /**
     * author
     * Sorts by id
     * @param categories -
     * @return -
     */
    @Override
    public List<Category> sortById(final List<Category> categories) {
        return categories.stream()
                .sorted(Comparator.comparingInt(Category::getId))
                .collect(Collectors.toList());
    }

    @Override
    public void saveCategory(final Category category) {
        this.categoryRepository.saveAndFlush(category);
    }

    @Override
    public Category findCategory(final Integer id) {
        return this.categoryRepository.findOne(id);
    }

    @Override
    public boolean doesCategoryExists(final Integer id) {
        return this.categoryRepository.exists(id);
    }

    @Override
    public void deleteArticles(final Set<Article> articles) {
        this.articleRepository.delete(articles);
    }

    @Override
    public void deleteCategory(final Category category) {
        this.categoryRepository.delete(category);
    }
    @Override
    public void deleteCategory(final Integer id) {
        Category category = this.findCategory(id);
        this.deleteArticles(category.getArticles());
        this.deleteCategory(category);
    }

    @Override
    public void editCategory(final Integer id, final CategoryBindingModel categoryBindingModel) {
        Category category = this.categoryRepository.getOne(id);
        category.setName(categoryBindingModel.getName());
        this.categoryRepository.saveAndFlush(category);
    }
}
