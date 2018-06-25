package blog.areas.admin.services;

import blog.areas.article.entities.Article;
import blog.areas.article.repository.ArticleRepository;
import blog.areas.role.entity.Role;
import blog.areas.role.repository.RoleRepository;
import blog.areas.user.bindingModels.UserEditBindingModel;
import blog.areas.user.entity.User;
import blog.areas.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AdminUserServicesImpl implements AdminUserServices {
    private UserRepository userRepository;
    private ArticleRepository articleRepository;
    private RoleRepository roleRepository;

    @Autowired
    public AdminUserServicesImpl(final UserRepository userRepository,final ArticleRepository articleRepository,final RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.roleRepository = roleRepository;
    }


    @Override
    public List<User> findAllStudents() {
        return this.userRepository.findAll();
    }

    @Override
    public boolean doesUserExists(Integer userId) {
        return this.userRepository.exists(userId);
    }

    @Override
    public User getUser(Integer id) {
        return this.userRepository.findOne(id);
    }

    @Override
    public List<Role> getRoles() {
        return this.roleRepository.findAll();
    }

    @Override
    public void editUser(final Integer userId, UserEditBindingModel userEditBindingModel) {
        User user = this.userRepository.findOne(userId);


        if (!StringUtils.isEmpty(userEditBindingModel.getPassword())
                && !StringUtils.isEmpty(userEditBindingModel.getConfirmPassword())){
            if (userEditBindingModel.getPassword().equals(userEditBindingModel.getConfirmPassword())){
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

                user.setPassword(bCryptPasswordEncoder.encode(userEditBindingModel.getPassword()));
            }
        }

        user.setFullName(userEditBindingModel.getFullName());
        user.setEmail(userEditBindingModel.getEmail());

        Set<Role> roles = new HashSet<>();

        for (Integer roleId : userEditBindingModel.getRoles()) {
            roles.add(this.roleRepository.findOne(roleId));
        }

        user.setRoles(roles);

        this.userRepository.saveAndFlush(user);
    }

    @Override
    public void deleteUser(Integer userId) {
        User user = this.userRepository.findOne(userId);

        for (Article article : user.getArticles()) {
            this.articleRepository.delete(article);
        }

        this.userRepository.delete(user);
    }
}
