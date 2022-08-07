package com.colin.melissacolin_comp304sec001_lab05_group14;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

public class UserViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private LiveData<Boolean> isLoginSuccessful, isLoginGoogleSuccessful, isRegisterSuccessful;

    public UserViewModel (@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        isLoginSuccessful = userRepository.getLoginResult();
        isLoginGoogleSuccessful = userRepository.getLoginGoogleResult();
        isRegisterSuccessful = userRepository.getRegisterResult();
    }

    //public void insert(User user) { userRepository.insert(user); }

    public void login(String email, String password) { userRepository.login(email, password); }

    public void loginWithGoogle(Task<GoogleSignInAccount> task) { userRepository.loginWithGoogle(task);}

    public void register(String email, String password) { userRepository.register(email, password); }

    public void logout() { userRepository.logout(); }

    public LiveData<Boolean> getLoginResult() { return isLoginSuccessful; }

    public LiveData<Boolean> getLoginGoogleResult() { return isLoginGoogleSuccessful; }

    public LiveData<Boolean> getRegisterResult() { return isRegisterSuccessful; }
}
