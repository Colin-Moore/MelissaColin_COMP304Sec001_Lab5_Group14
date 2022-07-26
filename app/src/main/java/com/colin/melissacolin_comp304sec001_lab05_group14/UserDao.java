package com.colin.melissacolin_comp304sec001_lab05_group14;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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

    // Create instance of UserDao if it doesn't already exist
    public static UserDao getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserDao();
        }
        return INSTANCE;
    }

    // Assign value to login flag based on email/password login success/failure
    private void loginSuccess(boolean login) { flagLogin.postValue(login); }

    // Assign value to Google login flag based on Google login success/failure
    private void loginGoogleSuccess(boolean loginGoogle) { flagLoginGoogle.postValue(loginGoogle); }

    // Assign value to register flag based on registration success/failure
    private void registerSuccess(boolean register) { flagRegister.postValue(register); }

    // Get email/password login attempt result
    public LiveData<Boolean> getLoginResult() { return flagLogin; }

    // Get Google login attempt result
    public LiveData<Boolean> getLoginGoogleResult() { return flagLoginGoogle; }

    // Get registration attempt result
    public LiveData<Boolean> getRegisterResult() { return flagRegister; }

    //public void insert(User user) { myRef.push().setValue(user); }

    // Login user via email/password
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

    // Login user via Google auth
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

    // Login user to Firebase based on Google auth credentials
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

    // Register user for account
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

    // Logout user from Firebase
    public void logout() {
        Log.d(TAG, "Logout Successful");
        loginGoogleSuccess(false); // required for login bug
        mAuth.signOut();
    }
}
