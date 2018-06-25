package blog.areas.home.controllers;

import blog.areas.article.entities.Article;
import blog.areas.category.Services.CategoryServices;
import blog.areas.category.entities.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Set;

@Controller
public class HomeController {
    private CategoryServices categoryServices;

    @Autowired
    public HomeController(final CategoryServices categoryServices) {
        this.categoryServices = categoryServices;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Category> categories = this.categoryServices.getAllCategories();

        model.addAttribute("view", "home/index");
        model.addAttribute("categories", categories);

        return "base-layout";
    }

    @GetMapping("/category/{id}")
    public String listArticles(Model model, final @PathVariable("id") Integer id) {
        if (!this.categoryServices.doesCategoryExists(id)) {
            return "redirect:/";
        }
        Category category = this.categoryServices.getCategory(id);
        Set<Article> articles = category.getArticles();

        model.addAttribute("category", category);
        model.addAttribute("articles", articles);
        model.addAttribute("view", "home/list-articles");

        return "base-layout";
    }

    @RequestMapping("/error/403")
    public String accessDenied(Model model) {
        model.addAttribute("view", "error/403");
        return "base-layout";
    }
}