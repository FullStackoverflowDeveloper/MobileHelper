package com.program.supgnhelper.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.program.supgnhelper.R;
import com.program.supgnhelper.model.Model;
import com.program.supgnhelper.model.enums.Role;
import com.program.supgnhelper.presenters.LoginPresenter;
import com.program.supgnhelper.view.LoginView;

import java.util.Calendar;

public class LoginActivity extends MvpAppCompatActivity implements LoginView {
    private EditText editEmail, editPassword;
    private Button signIn;
    private ProgressBar progressBar;
    private TextView forgot;

    @InjectPresenter
    LoginPresenter presenter;

    private FirebaseAuth auth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = findViewById(R.id.email);
        editPassword  = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        forgot = findViewById(R.id.forgotPassword);

        forgot.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class)));

        auth = FirebaseAuth.getInstance();

        signIn = findViewById(R.id.signIn);
        signIn.setOnClickListener(view -> {
            login();
        });
    }

    private boolean isValid(String email, String password){
        if(email.isEmpty()){
            editEmail.setError("Email in required!");
            editEmail.requestFocus();
            return false;
        }

        if(password.isEmpty()){
            editPassword.setError("Password in required!");
            editPassword.requestFocus();
            return false;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editEmail.setError("Please provide valid email");
            editEmail.requestFocus();
            return false;
        }

        if(password.length() < 6){
            editPassword.setError("Min password length should be 6 characters!");
            editPassword.requestFocus();
            return false;
        }
        return true;
    }

    private void login(){
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (isValid(email, password)) {
            loading();
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    assert user != null;
                    presenter.loadingPr(email, password);
                } else {
                    Log.d("LOGIN", task.getException().toString());
                    error();
                }
            });
        }
    }

    @Override
    public void loading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void loginAccepted(Role role, int userID) {
        SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("userId", userID);
        editor.putBoolean("login", true);

        Calendar calendar = Calendar.getInstance();
        editor.putLong("date", calendar.getTime().getTime());

        Intent intent;
        if(role == Role.MASTER){
            editor.putBoolean("master", true);
            intent = new Intent(this, MainActivity.class);
            intent.putExtra("list", true);
        }else {
            editor.putBoolean("master", false);
            intent = new Intent(this, MainActivity.class);
            intent.putExtra("list", false);
        }

        editor.apply();
        startActivity(intent);
        finish();
    }

    @Override
    public void error() {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, "Failed to login", Toast.LENGTH_LONG).show();
    }
}
