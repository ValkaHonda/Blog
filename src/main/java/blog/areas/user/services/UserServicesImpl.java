package blog.areas.user.services;


import blog.areas.role.entities.Role;
import blog.areas.role.repository.RoleRepository;
import blog.areas.user.bindingModels.ChangePassBindingModel;
import blog.areas.user.bindingModels.UserBindingModel;
import blog.areas.user.entity.User;
import blog.areas.user.repository.UserRepository;
import blog.areas.user.viewModel.UserViewModel;
import blog.areas.user.viewModel.UserViewModelImpl;
import blog.services.EmailMessage;
import blog.services.MailServices;
import blog.services.password.PasswordServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.IOException;

@Service
public class UserServicesImpl implements UserServices {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private MailServices mailServices;
    private PasswordServices passwordServices;

    @Autowired
    public UserServicesImpl(final UserRepository userRepository
        , final RoleRepository roleRepository, MailServices mailServices
            , final PasswordServices passwordServices) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.mailServices = mailServices;
        this.passwordServices = passwordServices;
    }

    @Override
    public UserViewModel getUserView(String email) {
        User user = this.getUser(email);
        return new UserViewModelImpl(user.getId(),user.getEmail(),user.getFullName(),user.getPicture());
    }

    @Override
    public User getUser(final Integer id) {
        return this.userRepository.findOne(id);
    }

    @Override
    public User getUser(final String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public boolean doesPasswordsMatches(UserBindingModel userBindingModel) {
        String pass1 = userBindingModel.getPassword();
        String pass2 = userBindingModel.getConfirmPassword();
        return pass1.equals(pass2);
    }

    @Override
    public void registerUser(UserBindingModel userBindingModel) throws IOException {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        User user = new User(
                userBindingModel.getEmail(),
                userBindingModel.getFullName(),
                bCryptPasswordEncoder.encode(userBindingModel.getPassword())
        );


        Role userRole = this.roleRepository.findByName("ROLE_USER");


        byte[] picture = userBindingModel.getMultipartFile().getBytes();
        user.addRole(userRole);
        user.setPicture(picture);
        this.userRepository.saveAndFlush(user);
        String message = UserServicesImpl.createConfirmationEmail(user);
        this.mailServices.sendEmailMessage(new EmailMessage(user.getEmail(),"Welcome ",message));
    }

    @Override
    public boolean validateFormInput(UserBindingModel userBindingModel, Model model) {
        boolean isValid = true;
        model.addAttribute("email",userBindingModel.getEmail());
        if (!this.doesPasswordsMatches(userBindingModel)){
            model.addAttribute("inconsistentPasswords",true);
            isValid = false;
        }
        if (!this.isEmailAddressFree(userBindingModel.getEmail())){
            model.addAttribute("takenEmail",true);
            isValid = false;
        }
        placeHoldersData(model,userBindingModel.getEmail()
                ,userBindingModel.getFullName(),"Password");
        return isValid;
    }

    @Override
    public void placeHoldersData(Model model) {
        model.addAttribute("email","Email");
        model.addAttribute("fullName","Full Name");
        model.addAttribute("pass","Password");
    }

    @Override
    public boolean isValidChangePassword(Model model, ChangePassBindingModel changePassBindingModel, User user) {
        boolean isValid = true;
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String pass = changePassBindingModel.getCurrentPass();

        if (!bCryptPasswordEncoder.matches(pass,user.getPassword())){
            model.addAttribute("wrongPassword",true);
            isValid = false;
        }
        if (!changePassBindingModel.getNewPass().equals(changePassBindingModel.getConfirmPass())){
            model.addAttribute("inconsistentPasswords",true);
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void updateUserPass(User user, String newPass) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        newPass = bCryptPasswordEncoder.encode(newPass);
        user.setPassword(newPass);
        this.userRepository.saveAndFlush(user);
    }

    @Override
    public boolean emailExists(String email) {
        return this.userRepository.findByEmail(email) != null;
    }

    @Override
    public boolean resetPassword(String email) {
        String newPass = this.passwordServices.generatePassword();
        if (this.mailServices.sendEmailMessage(new EmailMessage(email,"Reset password","New password" + newPass))){
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            newPass = bCryptPasswordEncoder.encode(newPass);
            User user = this.userRepository.findByEmail(email);
            user.setPassword(newPass);
            this.userRepository.saveAndFlush(user);





            return true;
        }
        return false;
    }

    @Override
    public void changeActiveFlag(Integer id) {
        User user = this.getUser(id);
        if (user.getActive()){
            user.setActive(false);
        } else {
            user.setActive(true);
        }
        this.userRepository.saveAndFlush(user);
    }

    @Override
    public boolean validateUser(String code) {
        for (User user : this.userRepository.findAll()) {
            String currentCode = user.getFullName();
            if (currentCode.equals((code))){
                user.setConfirm(true);
                this.userRepository.saveAndFlush(user);
                return true;
            }
        }
        return false;
    }

    private void placeHoldersData(Model model, String email, String fullName, String password) {
        model.addAttribute("email",email);
        model.addAttribute("fullName",fullName);
        model.addAttribute("pass",password);
    }
    private static String createConfirmationEmail(User user){
        return "        <h2>Please press confirm if this is your email</h2>" +
                "        <a href=\"http://localhost:8080/validation/"+user.getFullName()+"\""+">Confirm</a>";
    }

    private boolean isEmailAddressFree(String email){
        User user = this.userRepository.findByEmail(email);
        return user == null;
    }
}
