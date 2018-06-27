package blog.areas.user.services;

import blog.areas.role.entity.Role;
import blog.areas.role.repository.RoleRepository;
import blog.areas.user.bindingModels.UserBindingModel;
import blog.areas.user.entity.User;
import blog.areas.user.repository.UserRepository;
import blog.areas.user.viewModel.UserViewModel;
import blog.areas.user.viewModel.UserViewModelImpl;
import blog.services.MailServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.IOException;
import  java.util.Hashtable;
import  javax.naming.*;
import  javax.naming.directory.*;

@Service
public class UserServicesImpl implements UserServices {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private MailServices mailServices;

    @Autowired
    public UserServicesImpl(final UserRepository userRepository
        , final RoleRepository roleRepository, MailServices mailServices) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.mailServices = mailServices;
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
    }

    @Override
    public boolean validateFormInput(UserBindingModel userBindingModel, Model model) {
        boolean isValid = true;
        model.addAttribute("email",userBindingModel.getEmail());
        if (!this.doesPasswordsMatches(userBindingModel)){
            model.addAttribute("inconsistentPasswords",true);
            isValid = false;
        }
        if (!UserServicesImpl.isValidEmail(userBindingModel.getEmail())){
            model.addAttribute("invalidEmail",true);
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

    public void placeHoldersData(Model model, String email, String fullName, String pass) {
        model.addAttribute("email",email);
        model.addAttribute("fullName",fullName);
        model.addAttribute("pass",pass);
    }

    private static boolean isValidEmail(String email) {
        try {
            if (UserServicesImpl.doLookup(email) > 0) {
                return true;
            }
        }catch (NamingException e){
            return false;
        }
        return false;
    }
    private static int doLookup( String hostName ) throws NamingException {
        Hashtable env = new Hashtable();
        env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
        DirContext ictx = new InitialDirContext( env );
        Attributes attrs = ictx.getAttributes( hostName, new String[] { "MX" });
        Attribute attr = attrs.get( "MX" );
        if( attr == null ) return( 0 );
        return( attr.size() );
    }
}
