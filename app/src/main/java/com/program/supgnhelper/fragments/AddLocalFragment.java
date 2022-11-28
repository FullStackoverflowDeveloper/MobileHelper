package com.program.supgnhelper.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.program.supgnhelper.R;
import com.program.supgnhelper.model.Local;
import com.program.supgnhelper.presenters.AddressPresenter;
import com.program.supgnhelper.view.AddressView;

import java.util.List;

// Фрагмент добавления района
public class AddLocalFragment extends MvpAppCompatFragment implements AddressView {
    private EditText local;
    private Button save;

    @InjectPresenter
    AddressPresenter presenter;

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_local, container, false);
        BottomNavigationView navigationView = requireActivity().findViewById(R.id.bottomNavigationView);
        navigationView.setVisibility(View.GONE);

        local = view.findViewById(R.id.add_local1);
        save = view.findViewById(R.id.save1);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                if(local.getText().toString().isEmpty()){
                    local.setError("Введите значение");
                    local.requestFocus();
                }else {
                    presenter.saveLocal(local.getText().toString());
                    local.setText("");
                }
            }
        });

        return view;
    }

    @Override
    public void addressLoaded(List<Local> data) {

    }

    @Override
    public void saved() {
        Toast.makeText(requireContext(), "Сохранено", Toast.LENGTH_LONG).show();
    }
}
