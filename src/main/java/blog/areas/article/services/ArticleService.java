package blog.areas.article.services;

import blog.areas.article.bindingModel.ArticleBindingModel;
import blog.areas.article.entity.Article;
import org.springframework.web.bind.annotation.PathVariable;

public interface ArticleService {
    void createArticle(final ArticleBindingModel articleBindingModel);
    void editArticle(final @PathVariable Integer id, final ArticleBindingModel articleBindingModel);
    boolean isUserAuthorOrAdmin(final Article article);
    Article findArticle(final Integer id);
    boolean doesArticleExists(final Integer id);
    void deleteArticle(Article article);
    String getTagString(Article article);
}
