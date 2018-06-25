package blog.areas.admin.controllers;

import blog.areas.admin.services.AdminUserServices;
import blog.areas.role.entity.Role;
import blog.areas.user.bindingModel.UserEditBindingModel;
import blog.areas.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private AdminUserServices adminUserServices;

    @Autowired
    public AdminUserController(final AdminUserServices adminUserServices){
        this.adminUserServices = adminUserServices;
    }

    @GetMapping("/")
    public String listUsers(Model model){
        List<User> users = this.adminUserServices.findAllStudents();

        model.addAttribute("users",users);
        model.addAttribute("view","admin/user/list");

        return "base-layout";
    }

    @GetMapping("/edit/{id}")
    public String edit(final @PathVariable Integer id, Model model){
        if (!this.adminUserServices.doesUserExists(id)){
            return "redirect:/admin/users/";
        }

        User user = this.adminUserServices.getUser(id);
        List<Role> roles = this.adminUserServices.getRoles();

        model.addAttribute("user",user);
        model.addAttribute("roles",roles);
        model.addAttribute("view","admin/user/edit");

        return "base-layout";
    }

    @PostMapping("/edit/{id}")
    public String editProcess(final @PathVariable("id") Integer id, UserEditBindingModel userEditBindingModel){
        if(!this.adminUserServices.doesUserExists(id)){
            return "redirect:/admin/users/";
        }
        this.adminUserServices.editUser(id,userEditBindingModel);
        return "redirect:/admin/users/";
    }

    @GetMapping("/delete/{id}")
    public String delete(final @PathVariable Integer id, Model model){
        if(!this.adminUserServices.doesUserExists(id)){
            return "redirect:/admin/users/";
        }
        User user = this.adminUserServices.getUser(id);

        model.addAttribute("user",user);
        model.addAttribute("view","admin/user/delete");

        return "base-layout";
    }

    @PostMapping("/delete/{id}")
    public String deleteProcess(final @PathVariable Integer id){
        if (!this.adminUserServices.doesUserExists(id)){
            return "redirect:/admin/users/";
        }
        this.adminUserServices.deleteUser(id);
        return "redirect:/admin/users/";
    }
}

