package com.colin.melissacolin_comp304sec001_lab4_group14;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

                //TODO - login validation

                //userViewModel.login(email, password);
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
}