package blog.areas.article.services;

import blog.areas.article.bindingModel.ArticleBindingModel;
import blog.areas.article.entity.Article;
import blog.areas.article.repository.ArticleRepository;
import blog.areas.category.entity.Category;
import blog.areas.category.repository.CategoryRepository;
import blog.areas.tag.entity.Tag;
import blog.areas.tag.repository.TagRepository;
import blog.areas.user.entity.User;
import blog.areas.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {
    private ArticleRepository articleRepository;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private TagRepository tagRepository;

    @Autowired
    public ArticleServiceImpl(final ArticleRepository articleRepository, final UserRepository userRepository
        , final CategoryRepository categoryRepository, final TagRepository tagRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public void createArticle(final ArticleBindingModel articleBindingModel) {
        UserDetails user = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        User userEntity = this.userRepository.findByEmail(user.getUsername());
        Category category = this.categoryRepository.findOne(articleBindingModel.getCategoryId());
        HashSet<Tag> tags = this.findTagsFromString(articleBindingModel.getTagString());

        Article articleEntity = new Article(
                articleBindingModel.getTitle(),
                articleBindingModel.getContent(),
                userEntity,
                category,
                tags
        );
        this.articleRepository.saveAndFlush(articleEntity);
    }

    @Override
    public void editArticle(final Integer id, final ArticleBindingModel articleBindingModel) {
        Category category = this.categoryRepository.findOne(articleBindingModel.getCategoryId());
        HashSet<Tag> tags = this.findTagsFromString(articleBindingModel.getTagString());
        Article article = this.articleRepository.findOne(id);

        article.setCategory(category);
        article.setContent(articleBindingModel.getContent());
        article.setTitle(articleBindingModel.getTitle());
        article.setTags(tags);

        this.articleRepository.saveAndFlush(article);
    }

    private HashSet<Tag> findTagsFromString(String tagString){
        HashSet<Tag> tags = new HashSet<>();
        String[] tagNames = tagString.split("//s*");

        for (String tagName : tagNames) {
            Tag currentTag = this.tagRepository.findByName(tagName);

            if (currentTag == null){
                currentTag = new Tag(tagName);
                this.tagRepository.saveAndFlush(currentTag);
            }
            tags.add(currentTag);
        }
        return tags;
    }

    @Override
    public boolean isUserAuthorOrAdmin(Article article){
        UserDetails user = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        User userEntity = this.userRepository.findByEmail(user.getUsername());

        return userEntity.isAdmin() || userEntity.isAuthor(article);
    }

    @Override
    public Article findArticle(final Integer id) {
        return this.articleRepository.findOne(id);
    }

    @Override
    public boolean doesArticleExists(final Integer id) {
        return this.articleRepository.exists(id);
    }

    @Override
    public void deleteArticle(Article article) {
        this.articleRepository.delete(article);
    }

    @Override
    public String getTagString(Article article) {
        return article.getTags().stream()
                .map(Tag::getName).collect(Collectors.joining(", "));
    }
}
