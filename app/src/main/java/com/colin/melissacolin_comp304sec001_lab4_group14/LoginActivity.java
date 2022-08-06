package com.colin.melissacolin_comp304sec001_lab4_group14;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    private UserViewModel userViewModel;
    private Button btnLogin, btnLoginGoogle, btnRegister;
    private EditText editTxtEmail, editTxtPassword;

    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;

    private final static int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        // Google login
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(getApplicationContext(), gso);

        // Login to account
        btnLoginGoogle = findViewById(R.id.btnLoginGoogle);
        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = gsc.getSignInIntent();
                startActivityForResult(intent, RC_SIGN_IN);
            }
        });

        // Email and password login
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Login to account
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTxtEmail = findViewById(R.id.editTextEmail);
                editTxtPassword = findViewById(R.id.editTextPassword);

                String email = editTxtEmail.getText().toString();
                String password = editTxtPassword.getText().toString();

                // Validate user login credentials
                Boolean isValid = validateCredentials(email, password);

                // Login user
                if (isValid) {
                    userViewModel.login(email, password);
                    userViewModel.getLoginResult().observe(LoginActivity.this, new Observer<Boolean>() {
                        @Override
                        public void onChanged(@Nullable Boolean result) {
                            if (result) {
                                Toast.makeText(LoginActivity.this, "Sign in successful!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, ViewBikesActivity.class);
                                startActivity(intent);
                                finish(); //kill this activity so that it can't be navigated back to after logging in
                            } else {
                                Toast.makeText(LoginActivity.this, "Sign in failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        // Go to Register activity
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    // Handles Google sign in intent
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            userViewModel.loginWithGoogle(task);
            userViewModel.getLoginGoogleResult().observe(LoginActivity.this, new Observer<Boolean>() {
                @Override
                public void onChanged(@Nullable Boolean result) {
                    if (result) {
                        Toast.makeText(LoginActivity.this, "Sign in with Google successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, ViewBikesActivity.class);
                        startActivity(intent);
                        finish(); //kill this activity so that it can't be navigated back to after logging in
                    } else {
                        Toast.makeText(LoginActivity.this, "Sign in failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    // Validate user login credentials
    private boolean validateCredentials(String email, String password) {
        // Email validation
        if (email.isEmpty()) {
            editTxtEmail.setError("Email is required");
            editTxtEmail.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTxtEmail.setError("Email is invalid");
            editTxtEmail.requestFocus();
            return false;
        }

        // Password validation
        if (password.isEmpty()) {
            editTxtPassword.setError("Password is required");
            editTxtPassword.requestFocus();
            return false;
        }

        return true;
    }
}