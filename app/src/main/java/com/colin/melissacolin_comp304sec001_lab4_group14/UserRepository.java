package com.colin.melissacolin_comp304sec001_lab4_group14;

import android.content.Context;

import androidx.lifecycle.LiveData;

public class UserRepository {
    private final UserDao userDao;
    private LiveData<Boolean> isLoginSuccessful, isRegisterSuccessful;

    public UserRepository(Context context) {
        userDao = UserDao.getInstance();
        isLoginSuccessful = userDao.getLoginResult();
        isRegisterSuccessful = userDao.getRegisterResult();
    }

    //public void insert(User user) { userDao.insert(user); }

    public void login(String email, String password) { userDao.login(email, password); }

    public void register(String email, String password) { userDao.register(email, password); }

    public void logout() { userDao.logout(); }

    public LiveData<Boolean> getLoginResult() { return isLoginSuccessful; }

    public LiveData<Boolean> getRegisterResult() { return isRegisterSuccessful; }
}
