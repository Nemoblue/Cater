package com.example.cater.data;

import com.example.cater.data.model.LoggedInUser;
import com.example.cater.profile.ProfileViewModel;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private ProfileViewModel mProfileViewModel;

    public Result<LoggedInUser> login(String username, String password, boolean dump) {

       // mProfileViewModel = ViewModelProviders.of().get(ProfileViewModel.class);
        try {
            // TODO: handle loggedInUser authentication
            if(dump) {
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            username, password);
            return new Result.Success<>(fakeUser);
            } else { return new Result.Error(new IOException("Error")); }
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}