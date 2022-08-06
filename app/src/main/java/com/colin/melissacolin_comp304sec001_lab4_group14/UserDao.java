package com.colin.melissacolin_comp304sec001_lab4_group14;

import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserDao {
    private static volatile UserDao INSTANCE;
    private static final String DATABASE_NAME = "UserDB";
    private static String TAG = "DAO";

    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    private MutableLiveData<Boolean> flagRegister = new MutableLiveData<>();
    private MutableLiveData<Boolean> flagLogin = new MutableLiveData<>();
    private MutableLiveData<Boolean> flagLoginGoogle = new MutableLiveData<>();

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

    private void loginGoogleSuccess(boolean loginGoogle) { flagLoginGoogle.postValue(loginGoogle); }

    private void registerSuccess(boolean register) { flagRegister.postValue(register); }

    public LiveData<Boolean> getLoginResult() { return flagLogin; }

    public LiveData<Boolean> getLoginGoogleResult() { return flagLoginGoogle; }

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

    public void loginWithGoogle(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully through Google, continue with Firebase auth
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithGoogle: success");
                            loginGoogleSuccess(true);
                        } else {
                            Log.w(TAG, "signInWithGoogle: failure");
                            loginGoogleSuccess(false);
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

    public void logout() {
        Log.d(TAG, "Logout Successful");
        loginGoogleSuccess(false); // required for login bug
        mAuth.signOut();
    }
}
