package blog.areas.admin.controllers;

import blog.areas.admin.services.AdminCategoryServices;
import blog.areas.category.bindingModel.CategoryBindingModel;
import blog.areas.category.entities.Category;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {
    private AdminCategoryServices categoryServices;

    public CategoryController(final AdminCategoryServices categoryServices) {
        this.categoryServices = categoryServices;
    }

    @GetMapping("/")
    public String list(Model model){
        List<Category> categories = this.categoryServices.findAllCategories();
        categories = this.categoryServices.sortById(categories);

        model.addAttribute("categories",categories);
        model.addAttribute("view","admin/category/list");

        return "base-layout";
    }

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("view","admin/category/create");
        return "base-layout";
    }

    @PostMapping("/create")
    public String createProcess(final CategoryBindingModel categoryBindingModel){
        if(StringUtils.isEmpty(categoryBindingModel.getName())){
            return "redirect:/admin/categories/create";
        }

        Category category = new Category(categoryBindingModel.getName());
        this.categoryServices.saveCategory(category);

        return "redirect:/admin/categories/";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, final @PathVariable Integer id){
        if(!this.categoryServices.doesCategoryExists(id)){
            return "redirect:/admin/categories/";
        }

        Category category = this.categoryServices.findCategory(id);

        model.addAttribute("category",category);
        model.addAttribute("view","admin/category/edit");

        return "base-layout";
    }

    @PostMapping("/edit/{id}")
    public String editProcess(final @PathVariable Integer id, final CategoryBindingModel categoryBindingModel){
        if(!this.categoryServices.doesCategoryExists(id)){
            return "redirect:/admin/categories/";
        }
        this.categoryServices.editCategory(id,categoryBindingModel);
        return "redirect:/admin/categories/";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model,final @PathVariable Integer id){
        if(!this.categoryServices.doesCategoryExists(id)){
            return "redirect:/admin/categories/";
        }

        Category category = this.categoryServices.findCategory(id);

        model.addAttribute("category",category);
        model.addAttribute("view", "admin/category/delete");
        return "base-layout";
    }

    @PostMapping("/delete/{id}")
    public String deleteProcess(final @PathVariable Integer id){
        if(!this.categoryServices.doesCategoryExists(id)){
            return "redirect:/admin/categories/";
        }
        this.categoryServices.deleteCategory(id);
        return "redirect:/admin/categories/";
    }
}