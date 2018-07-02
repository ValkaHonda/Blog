package blog.areas.user.controller;

import blog.areas.user.bindingModels.ChangePassBindingModel;
import blog.areas.user.bindingModels.UserBindingModel;
import blog.areas.user.entity.User;
import blog.areas.user.services.UserServices;
import blog.areas.user.viewModel.UserViewModel;
import blog.services.password.PasswordServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

@Controller
public class UserController {
    private UserServices userService;

    @Autowired
    public UserController(final UserServices userService){
        this.userService = userService;
    }

    @GetMapping("/resetPassword")
    public String resetPassword(Model model){
        model.addAttribute("view","user/resetPass");
        model.addAttribute("wrongEmail","Email");
        return "base-layout";
    }

    @PostMapping("/resetPassword")
    public String resetPasswordProcess(Model model, final UserBindingModel userBindingModel){
        if(this.userService.emailExists(userBindingModel.getEmail())){
            this.userService.resetPassword(userBindingModel.getEmail());
            return "redirect:/login";
        }
        model.addAttribute("view","user/resetPass");
        model.addAttribute("noSuchEmail",true);
        model.addAttribute("wrongEmail",userBindingModel.getEmail());
        return "base-layout";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/changePassword")
    public String changePassword(Model model){
        model.addAttribute("view","user/changePassword");
        return "base-layout";
    }

    @PostMapping("/changePassword")
    public String changePasswordProcess(Model model, ChangePassBindingModel changePassBindingModel){
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        User user = this.userService.getUser(principal.getUsername());
        if(!this.userService.isValidChangePassword(model,changePassBindingModel,user)){ // the service call!
            model.addAttribute("view","user/changePassword");
            return "base-layout";
        }
        this.userService.updateUserPass(user, changePassBindingModel.getNewPass());
        return "redirect:/logout";
    }

    @GetMapping("/register")
    public String register(Model model){
        this.userService.placeHoldersData(model);
        model.addAttribute("view", "user/register");
        return "base-layout";
    }

    @PostMapping("/register") // working here
    public String registerProcess(final UserBindingModel userBindingModel, Model model) throws IOException {
        if (!this.userService.validateFormInput(userBindingModel,model)){
            model.addAttribute("view", "user/register");
            return "base-layout";
        }
        this.userService.registerUser(userBindingModel);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("view","user/login");
        return "base-layout";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null){
            new SecurityContextLogoutHandler().logout(request,response,auth);
        }

        return "redirect:/login?logout";
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String profilePage(Model model){
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        UserViewModel user = this.userService.getUserView(principal.getUsername());

        model.addAttribute("picture", Base64.getEncoder().encodeToString(user.getPicture()));
        model.addAttribute("user",user);
        model.addAttribute("view","user/profile");

        return "base-layout";
    }
    
    @GetMapping("/validation/{code}")
    public String edit(final @PathVariable String code, Model model) {
        if (this.userService.validateUser(code)){
            model.addAttribute("view","user/successfulValidation");
        } else {
            model.addAttribute("view", "user/unSuccessfulValidation");
        }
        return "base-layout";
    }
}

