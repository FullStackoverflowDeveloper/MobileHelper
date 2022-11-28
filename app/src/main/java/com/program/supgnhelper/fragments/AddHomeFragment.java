package com.program.supgnhelper.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.program.supgnhelper.R;
import com.program.supgnhelper.model.Home;
import com.program.supgnhelper.model.Item;
import com.program.supgnhelper.model.Local;
import com.program.supgnhelper.model.Street;
import com.program.supgnhelper.model.enums.Status;
import com.program.supgnhelper.presenters.AddressPresenter;
import com.program.supgnhelper.view.AddressView;

import java.util.ArrayList;
import java.util.List;

// Фрагмент добавления дома, валидация, загрзка адреса
public class AddHomeFragment extends MvpAppCompatFragment implements AddressView {
    protected Spinner spinnerLocal, spinnerStreet;
    protected EditText family, name, patronym, phone, counter, plomb, plug, readings;
    protected Button save;
    private EditText home;
    private CheckBox isFlat;
    protected List<Local> locals;

    @InjectPresenter
    AddressPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_home, container, false);
        BottomNavigationView navigationView = requireActivity().findViewById(R.id.bottomNavigationView);
        navigationView.setVisibility(View.GONE);

        spinnerLocal = view.findViewById(R.id.local4);
        spinnerStreet = view.findViewById(R.id.street4);
        family = view.findViewById(R.id.family4);
        name = view.findViewById(R.id.name4);
        patronym = view.findViewById(R.id.patronym4);
        phone = view.findViewById(R.id.phone4);
        save = view.findViewById(R.id.save4);
        counter = view.findViewById(R.id.numberCounter4);
        plomb = view.findViewById(R.id.plomb4);
        plug = view.findViewById(R.id.plug_plomb4);
        readings = view.findViewById(R.id.readings4);

        home = view.findViewById(R.id.add_home4);
        isFlat = view.findViewById(R.id.isFlat4);
        save = view.findViewById(R.id.save4);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.loadAddress();

        isFlat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                EditText[] editTexts = {family, name, patronym, phone, counter, plug, plomb, readings};
                for (EditText editText: editTexts){
                    if (isChecked){
                        editText.setVisibility(View.GONE);
                    }else {
                        editText.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                Local local = locals.get(spinnerLocal.getSelectedItemPosition());
                Street street = local.getStreets().get(spinnerStreet.getSelectedItemPosition());
                if (isFlat.isChecked()){
                    if(home.getText().toString().isEmpty()){
                        home.setError("Введите значение");
                        home.requestFocus();
                    }else {
                        street.addHome(new Home(true, new ArrayList<>(), Integer.parseInt(home.getText().toString())));
                        presenter.updateAddress(local);
                        home.setText("");
                    }
                }else {
                    if (checkValues()) {
                        Home newHome = new Home(false, Integer.parseInt(home.getText().toString()));
                        street.addHome(newHome);
                        presenter.createAddress(local, new Item(family.getText().toString(), name.getText().toString(),
                                patronym.getText().toString(), phone.getText().toString(), Status.ADDRESS, newHome.getId(),
                                Integer.parseInt(counter.getText().toString()), Integer.parseInt(plomb.getText().toString()),
                                Integer.parseInt(plug.getText().toString()), Integer.parseInt(readings.getText().toString())));
                        removeValues();
                    }
                }
            }
        });
    }

    private boolean checkValues(){
        EditText[] editTexts = {family, name, patronym, phone, counter, plug, plomb, readings};
        for (EditText editText: editTexts){
            if(editText.getText().toString().isEmpty()){
                editText.setError("Введите значение");
                editText.requestFocus();
                return false;
            }
        }

        if(counter.getText().toString().length() < 7){
            counter.setError("Минимальная длина 7 символов");
            counter.requestFocus();
            return false;
        }

        if(plomb.getText().toString().length() < 6){
            plomb.setError("Минимальная длина 6 символов");
            plomb.requestFocus();
            return false;
        }

        if(plug.getText().toString().length() < 5){
            plug.setError("Минимальная длина 5 символов");
            plug.requestFocus();
            return false;
        }

        return true;
    }

    protected void removeValues(){
        EditText[] editTexts = {family, name, patronym, phone, counter, plug, plomb, readings};
        for (EditText editText: editTexts){
            editText.setText("");
        }
    }

    @Override
    public void addressLoaded(List<Local> data) {
        locals = data;
        List<String> nameLocals = new ArrayList<String>();
        for (Local loc : locals) {
            nameLocals.add(loc.getName());
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter
                (getActivity(), android.R.layout.simple_spinner_item, nameLocals);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinnerLocal.setAdapter(spinnerArrayAdapter);

        spinnerLocal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<Street> streets = locals.get(position).getStreets();
                List<String> namesStreets = new ArrayList<>();
                for (Street street : streets) {
                    namesStreets.add(street.getName());
                }
                ArrayAdapter<String> streetsArrayAdapter = new ArrayAdapter<String>
                        (getContext(), android.R.layout.simple_spinner_item, namesStreets);
                streetsArrayAdapter.setDropDownViewResource(android.R.layout
                        .simple_spinner_dropdown_item);
                spinnerStreet.setAdapter(streetsArrayAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    @Override
    public void saved() {
        Toast.makeText(requireContext(), "Сохранено", Toast.LENGTH_LONG).show();
    }
}
