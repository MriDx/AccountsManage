package com.mridx.accountsmanage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.mridx.accountsmanage.R;


public class SignupUI extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private TextInputEditText emailField, passwordField, nameField;
    private AppCompatButton signupBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_ui);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            takeToDashboard();
        }

        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        nameField = findViewById(R.id.nameField);
        signupBtn = findViewById(R.id.signupBtn);
        signupBtn.setOnClickListener(v -> signup());

    }

    private void signup() {
        String name = nameField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        if (valid(name, email, password)) {
            proceedEmailSignup(email, name, password);
        }
    }

    private void proceedEmailSignup(String email, String name, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        user = auth.getCurrentUser();
                        UserProfileChangeRequest requestBuilder = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                        user.updateProfile(requestBuilder);
                        takeToDashboard();
                    } else {
                        Toast.makeText(this, "Failed !", Toast.LENGTH_SHORT).show();
                        Log.d("kaku", "proceedEmailSignup: " + task.getException());
                    }
                });
    }

    private void takeToDashboard() {
        startActivity(new Intent(this, HomeUI.class));
        finish();
    }

    private boolean valid(String name, String email, String password) {
        if (email.length() == 0) {
            return false;
        } else if (name.length() == 0) {
            return false;
        } else if (password.length() == 0) {
            return false;
        } else {
            return true;
        }
    }
}
