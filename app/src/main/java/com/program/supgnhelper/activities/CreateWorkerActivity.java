package com.program.supgnhelper.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.program.supgnhelper.R;
import com.program.supgnhelper.model.Model;
import com.program.supgnhelper.model.enums.Role;
import com.program.supgnhelper.model.User;

public class CreateWorkerActivity extends AppCompatActivity {
    private EditText name, surname, patronym, email, password;
    private Button save;
    private FirebaseAuth auth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_worker);

        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        patronym = findViewById(R.id.patronym);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        save = findViewById(R.id.save);

        auth = FirebaseAuth.getInstance();

        save.setOnClickListener(view -> {
            if (isValid()) {
                User user = new User(fmt(name), fmt(surname), fmt(patronym), fmt(email), fmt(password), Role.WORKER);
                saving(user);
                finish();
            }
        });
    }

    private boolean isValid(){
        EditText[] editTexts = {surname, name, patronym, email, password};
        for (EditText editText: editTexts){
            if(fmt(editText).isEmpty()){
                editText.setError("Введите значение");
                editText.requestFocus();
                return false;
            }
        }

        if(fmt(password).length() < 8){
            password.setError("Минимальная длина 8 символов");
            password.requestFocus();
            return false;
        }
        return true;
    }

    // Добавление данных пользователя в бд и firebase auth
    private void saving(User user){
        Model model = new Model();
        model.addUser(user);

        auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "User saved", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Failed to save new user", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private String fmt(EditText editText){
        return editText.getText().toString();
    }
}
