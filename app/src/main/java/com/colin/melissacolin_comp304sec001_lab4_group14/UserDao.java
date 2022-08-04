package com.colin.melissacolin_comp304sec001_lab4_group14;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserDao {
    private static volatile UserDao INSTANCE;
    private static final String DATABASE_NAME = "UserDB";
    private static String TAG = "DAO";

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;

    private MutableLiveData<Boolean> flagRegister = new MutableLiveData<>();
    private MutableLiveData<Boolean> flagLogin = new MutableLiveData<>();

    private UserDao() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(DATABASE_NAME);
        mAuth = FirebaseAuth.getInstance();
    }

    public static UserDao getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserDao();
        }
        return INSTANCE;
    }

    private void loginSuccess(boolean login) { flagLogin.postValue(login); }

    private void registerSuccess(boolean register) { flagRegister.postValue(register); }

    public LiveData<Boolean> getLoginResult() { return flagLogin; }

    public LiveData<Boolean> getRegisterResult() { return flagRegister; }

    //public void insert(User user) { myRef.push().setValue(user); }

    public void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail: success");
                            loginSuccess(true);
                        } else {
                            Log.w(TAG, "signInWithEmail: failure");
                            loginSuccess(false);
                        }
                    }
                });
    }

    public void register(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User Registration Successful");
                            registerSuccess(true);
                        } else {
                            Log.w(TAG, "User Registration Failed", task.getException());
                            registerSuccess(false);
                        }
                    }
                });
    }

    public void logout() { mAuth.signOut(); }
}
