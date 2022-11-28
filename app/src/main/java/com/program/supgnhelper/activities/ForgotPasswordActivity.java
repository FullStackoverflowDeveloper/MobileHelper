package com.program.supgnhelper.activities;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.program.supgnhelper.R;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText emailEditText;
    private Button change;
    private ProgressBar progressBar;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailEditText = findViewById(R.id.email);
        change = findViewById(R.id.resetPassword);
        progressBar = findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();

        change.setOnClickListener(view -> resetPassword());
    }

    private void resetPassword() {
        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty()) {
            emailEditText.setError("Email is required!");
            emailEditText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please provide valid email");
            emailEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Check your email to reset your password!", Toast.LENGTH_LONG).show();
                finish();
            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Try again! Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
