package com.program.supgnhelper.activities;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.program.supgnhelper.activities.LoginActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Получение переменной, осуществлен ли вход
        SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);
        boolean isLogin = preferences.getBoolean("login", false);

        // Через 2,5 секунды запуск логина или основного окна
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isLogin){
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }else{
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }

                finish();
            }
        }, 2500);
    }
}