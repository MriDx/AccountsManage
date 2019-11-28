package com.mridx.accountsmanage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.mridx.accountsmanage.R;

public class LoginUI1 extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth auth;
    private FirebaseUser user;

    private AppCompatButton googleSign, facebookSignin;
    private GoogleSignInClient mGoogleSignInClient;
    private ProgressBar googleProgress, facebookProgress;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_ui1);

        googleSign = findViewById(R.id.googleSignin);
        facebookSignin = findViewById(R.id.facebookSignin);
        googleProgress = findViewById(R.id.googleProgress);
        facebookProgress = findViewById(R.id.facebookProgress);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user != null) {
            takeToDashboard();
            return;
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                //.requestScopes(new Scope("profile"))
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleSign.setOnClickListener(v -> startGoogleSignin());
        googleSign.setOnClickListener(v -> startFacebookSignin());

    }

    private void startFacebookSignin() {
        googleProgress.setVisibility(View.VISIBLE);
        googleSign.setText(null);
        googleSign.setEnabled(false);
        facebookSignin.setEnabled(false);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    private void startGoogleSignin() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                mGoogleSignInClient.revokeAccess();
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("kaku", "Google sign in failed", e);

            }
            googleProgress.setVisibility(View.GONE);
            googleSign.setEnabled(true);
            googleSign.setText("Google");
            facebookSignin.setEnabled(true);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("kaku", "signInWithCredential:success");
                            user = auth.getCurrentUser();
                            takeToDashboard();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("kaku", "signInWithCredential:failure", task.getException());
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void takeToDashboard() {
        startActivity(new Intent(this, HomeUI.class));
        finish();
    }
}
