package blog.areas.article.controller;


import blog.areas.admin.services.AdminCategoryServices;
import blog.areas.article.bindingModels.ArticleBindingModel;
import blog.areas.article.entities.Article;
import blog.areas.article.services.ArticleService;
import blog.areas.category.entity.Category;
import blog.areas.user.entity.User;
import blog.areas.user.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ArticleController {
    private ArticleService articleService;
    private AdminCategoryServices categoryServices;
    private UserServices userServices;

    @Autowired
    public ArticleController(final ArticleService articleService, final AdminCategoryServices categoryServices
        , final UserServices userServices) {
        this.articleService = articleService;
        this.categoryServices = categoryServices;
        this.userServices = userServices;
    }

    @GetMapping("/article/create")
    @PreAuthorize("isAuthenticated()")
    public String create(Model model){
        List<Category> categories = this.categoryServices.findAllCategories();

        model.addAttribute("categories",categories);
        model.addAttribute("view","article/create");
        return "base-layout";
    }

    @PostMapping("/article/create")
    @PreAuthorize("isAuthenticated()")
    public String createProcess(final ArticleBindingModel articleBindingModel){
        this.articleService.createArticle(articleBindingModel);
        return "redirect:/";
    }

    @GetMapping("/article/{id}")
    public String details(Model model, final  @PathVariable("id") Integer id){
        if(!this.articleService.doesArticleExists(id)){
            return "redirect:/";
        }

        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)){
            UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            User entityUser = this.userServices.getUser(principal.getUsername());
            model.addAttribute("user",entityUser);
        }

        Article article = this.articleService.findArticle(id);

        model.addAttribute("article",article);
        model.addAttribute("view","article/details");

        return "base-layout";
    }

    @GetMapping("/article/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String edit(final @PathVariable("id") Integer id, Model model){
        if(!this.articleService.doesArticleExists(id)){
            return "redirect:/";
        }
        Article article = this.articleService.findArticle(id);
        if (!this.articleService.isUserAuthorOrAdmin(article)){
            return "redirect:/article/" + id;
        }
        List<Category> categories = this.categoryServices.findAllCategories();
        String tagString = this.articleService.getTagString(article);

        model.addAttribute("view","article/edit");
        model.addAttribute("article",article);
        model.addAttribute("categories",categories);
        model.addAttribute("tags",tagString);

        return "base-layout";
    }

    @PostMapping("/article/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String editProcess(final @PathVariable("id") Integer id,final ArticleBindingModel articleBindingModel){
        if (!this.articleService.doesArticleExists(id)){
            return "redirect:/";
        }
        this.articleService.editArticle(id, articleBindingModel);
        return "redirect:/article/"+id;
    }

    @GetMapping("/article/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(final Model model, final @PathVariable("id") Integer id){
        if (!this.articleService.doesArticleExists(id)){
            return "redirect:/";
        }
        Article article = this.articleService.findArticle(id);

        if (!this.articleService.isUserAuthorOrAdmin(article)){
            return "redirect:/article/" + id;
        }

        model.addAttribute("article",article);
        model.addAttribute("view","article/delete");

        return "base-layout";
    }

    @PostMapping("article/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteProcess(final @PathVariable("id") Integer id){
        if (!this.articleService.doesArticleExists(id)){
            return "redirect:/";
        }

        Article article = this.articleService.findArticle(id);

        if (!this.articleService.doesArticleExists(id)){
            return "redirect:/article/" + id;
        }
        this.articleService.deleteArticle(article);
        return "redirect:/";
    }
}