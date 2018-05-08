package blog.areas.user.services;

import blog.areas.role.entity.Role;
import blog.areas.role.repository.RoleRepository;
import blog.areas.user.bindingModel.UserBindingModel;
import blog.areas.user.entity.User;
import blog.areas.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class UserServicesImpl implements UserServices {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public UserServicesImpl(final UserRepository userRepository
        , final RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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
}
