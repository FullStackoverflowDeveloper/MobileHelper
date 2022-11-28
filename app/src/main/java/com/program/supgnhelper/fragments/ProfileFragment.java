package com.program.supgnhelper.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.program.supgnhelper.R;
import com.program.supgnhelper.activities.LoginActivity;
import com.program.supgnhelper.model.User;
import com.program.supgnhelper.presenters.ProfilePresenter;
import com.program.supgnhelper.view.ProfileView;

import java.util.Objects;
import java.util.Random;

// Фрагмент профиля, загрузка пользователя и отображение
public class ProfileFragment extends MvpAppCompatFragment implements ProfileView {
    @InjectPresenter
    ProfilePresenter presenter;

    private TextView username, email, number;

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        BottomNavigationView navigationView = requireActivity().findViewById(R.id.bottomNavigationView);
        navigationView.setVisibility(View.GONE);
        username = view.findViewById(R.id.userID);
        email = view.findViewById(R.id.user_email);
        number = view.findViewById(R.id.tabel_number);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loading();
    }

    @Override
    public void loading() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        presenter.loading(preferences.getInt("userId", 0));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void loaded(User user) {
        username.setText(user.getFullName());
        email.setText(user.getEmail());
        number.setText("Табельный номер: " + user.getId());
    }

//    private int gen() {
//        Random r = new Random( System.currentTimeMillis() );
//        return ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
//    }
}