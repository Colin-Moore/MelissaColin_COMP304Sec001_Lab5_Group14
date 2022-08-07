package com.colin.melissacolin_comp304sec001_lab05_group14;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

public class UserRepository {
    private final UserDao userDao;
    private LiveData<Boolean> isLoginSuccessful, isLoginGoogleSuccessful, isRegisterSuccessful;

    public UserRepository(Context context) {
        userDao = UserDao.getInstance();
        isLoginSuccessful = userDao.getLoginResult();
        isLoginGoogleSuccessful = userDao.getLoginGoogleResult();
        isRegisterSuccessful = userDao.getRegisterResult();
    }

    //public void insert(User user) { userDao.insert(user); }

    public void login(String email, String password) { userDao.login(email, password); }

    public void loginWithGoogle(Task<GoogleSignInAccount> task) { userDao.loginWithGoogle(task);}

    public void register(String email, String password) { userDao.register(email, password); }

    public void logout() { userDao.logout(); }

    public LiveData<Boolean> getLoginResult() { return isLoginSuccessful; }

    public LiveData<Boolean> getLoginGoogleResult() { return isLoginGoogleSuccessful; }

    public LiveData<Boolean> getRegisterResult() { return isRegisterSuccessful; }
}
