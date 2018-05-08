package blog.areas.admin.services;

import blog.areas.role.entity.Role;
import blog.areas.user.bindingModel.UserEditBindingModel;
import blog.areas.user.entity.User;

import java.util.List;

public interface AdminUserServices {
    List<User> findAllStudents();
    boolean doesUserExists(final Integer userId);
    User getUser(final Integer userId);
    List<Role> getRoles();

    void editUser(final Integer userId,final UserEditBindingModel userEditBindingModel);
    void deleteUser(final Integer userId);

}
