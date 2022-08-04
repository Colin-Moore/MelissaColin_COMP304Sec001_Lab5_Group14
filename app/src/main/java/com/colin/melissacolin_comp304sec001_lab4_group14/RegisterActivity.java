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

public class RegisterActivity extends AppCompatActivity {
    private UserViewModel userViewModel;
    private Button btnLogin, btnRegister;
    private EditText editTxtEmail, editTxtPassword, editTxtConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Register for account
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTxtEmail = findViewById(R.id.editTextEmail);
                editTxtPassword = findViewById(R.id.editTextPassword);
                editTxtConfirmPassword = findViewById(R.id.editTextConfirmPassword);

                String email = editTxtEmail.getText().toString().trim();
                String password = editTxtPassword.getText().toString().trim();
                String confirmPassword = editTxtConfirmPassword.getText().toString().trim();

                // Validate user registration inputs
                Boolean isValid = validateCredentials(email, password, confirmPassword);

                // Register user
                if (isValid) {
                    userViewModel.register(email, password);
                    userViewModel.getRegisterResult().observe(RegisterActivity.this, new Observer<Boolean>() {
                        @Override
                        public void onChanged(@Nullable Boolean result) {
                            if (result) {
                                Toast.makeText(RegisterActivity.this, "Sign up successful!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(RegisterActivity.this, "Sign up failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });

        // Go to Login activity
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    // Validate user registration inputs
    public boolean validateCredentials(String email, String password, String confirmPassword) {
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

        if (password.length() < 6) {
            editTxtPassword.setError("Password too short. Min length of 6 characters required");
            editTxtPassword.requestFocus();
            return false;
        }

        // Confirm password match validation
        if (confirmPassword.isEmpty() || !confirmPassword.equals(password)) {
            editTxtConfirmPassword.setError("Passwords do not match");
            editTxtConfirmPassword.requestFocus();
            return false;
        }

        return true;
    }
}