package com.program.supgnhelper.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.program.supgnhelper.R;
import com.program.supgnhelper.model.Local;
import com.program.supgnhelper.model.Street;
import com.program.supgnhelper.presenters.AddressPresenter;
import com.program.supgnhelper.view.AddressView;

import java.util.ArrayList;
import java.util.List;

// Фрагмент добавления улицы
public class AddStreetFragment extends MvpAppCompatFragment implements AddressView {
    private Spinner spinnerLocal;
    private EditText street;
    private Button save;
    protected List<Local> locals;

    @InjectPresenter
    AddressPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_street, container, false);
        BottomNavigationView navigationView = requireActivity().findViewById(R.id.bottomNavigationView);
        navigationView.setVisibility(View.GONE);

        spinnerLocal = view.findViewById(R.id.local2);
        street = view.findViewById(R.id.add_street2);
        save = view.findViewById(R.id.save2);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                if(street.getText().toString().isEmpty()){
                    street.setError("Введите значение");
                    street.requestFocus();
                }else {
                    Local local = locals.get(spinnerLocal.getSelectedItemPosition());
                    local.addStreet(new Street(street.getText().toString(), new ArrayList<>()));
                    presenter.updateAddress(local);
                    street.setText("");
                }
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.loadAddress();
    }

    @Override
    public void addressLoaded(List<Local> data) {
        locals = data;
        List<String> nameLocals = new ArrayList<String>();
        for(Local loc: locals){
            nameLocals.add(loc.getName());
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter
                (getActivity(), android.R.layout.simple_spinner_item, nameLocals);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinnerLocal.setAdapter(spinnerArrayAdapter);
    }

    @Override
    public void saved() {
        Toast.makeText(requireContext(), "Сохранено", Toast.LENGTH_LONG).show();
    }
}
