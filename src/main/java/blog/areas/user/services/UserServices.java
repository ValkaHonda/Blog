package blog.areas.user.services;

import blog.areas.user.bindingModel.UserBindingModel;
import blog.areas.user.entity.User;

import java.io.IOException;

public interface UserServices {
    User getUser(final Integer id);
    User getUser(final String email);
    boolean doesPasswordsMatches(final UserBindingModel userBindingModel);
    void registerUser(final UserBindingModel userBindingModel) throws IOException;
}
