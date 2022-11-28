package com.program.supgnhelper.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.program.supgnhelper.R;
import com.program.supgnhelper.presenters.AboutPresenter;
import com.program.supgnhelper.view.AboutView;

import java.util.Objects;

// Фрагмент о приложении, загрузка и установка описания
public class AboutFragment extends MvpAppCompatFragment implements AboutView {
    @InjectPresenter
    AboutPresenter presenter;

    private TextView description;

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        BottomNavigationView navigationView = requireActivity().findViewById(R.id.bottomNavigationView);
        navigationView.setVisibility(View.GONE);

        description = view.findViewById(R.id.description);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.loadTitle();
    }

    @Override
    public void titleLoaded(String title) {
        description.setText(title);
    }
}
