package com.mridx.accountsmanage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mridx.accountsmanage.R;

public class LoginUI extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;

    private TextInputEditText emailField, passwordField;
    private AppCompatButton loginBtn;
    private AppCompatTextView signupLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_ui);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            takeToDashboard();
        }

        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(v -> proceedLogin());
        signupLink = findViewById(R.id.signupLink);
        signupLink.setOnClickListener(v -> {
            startActivity(new Intent(this, SignupUI.class));
            finish();
        });
    }

    private void proceedLogin() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        if (valid(email, password)) {
            login(email, password);
        }
    }

    private void login(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        user = auth.getCurrentUser();
                        takeToDashboard();
                    } else {
                        Toast.makeText(this, "Failed to Login !", Toast.LENGTH_SHORT).show();
                        Log.d("kaku", "login: " + task.getException());
                    }
                });
    }

    private void takeToDashboard() {
        startActivity(new Intent(this, HomeUI.class));
        finish();
    }

    private boolean valid(String email, String password) {
        if (email.length() == 0) {
            return false;
        } else if (password.length() == 0) {
            return false;
        } else {
            return true;
        }
    }
}
