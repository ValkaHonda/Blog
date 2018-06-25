package blog.areas.user.services;

import blog.areas.user.bindingModels.UserBindingModel;
import blog.areas.user.entity.User;
import blog.areas.user.viewModel.UserViewModel;

import java.io.IOException;

public interface UserServices {
    UserViewModel getUserView(final String email);
    User getUser(final Integer id);
    User getUser(final String email);
    boolean doesPasswordsMatches(final UserBindingModel userBindingModel);
    void registerUser(final UserBindingModel userBindingModel) throws IOException;
}
