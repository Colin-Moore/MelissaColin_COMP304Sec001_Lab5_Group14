package com.colin.melissacolin_comp304sec001_lab4_group14;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private UserViewModel userViewModel;
    private Button btnLogin, btnRegister;
    private EditText editTxtEmail, editTxtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

    // Validate user login credentials
    public boolean validateCredentials(String email, String password) {
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