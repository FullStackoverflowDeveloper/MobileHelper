package com.program.supgnhelper.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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
import com.program.supgnhelper.model.Flat;
import com.program.supgnhelper.model.Home;
import com.program.supgnhelper.model.Item;
import com.program.supgnhelper.model.Local;
import com.program.supgnhelper.model.Street;
import com.program.supgnhelper.model.enums.Status;
import com.program.supgnhelper.presenters.AddressPresenter;
import com.program.supgnhelper.view.AddressView;

import java.util.ArrayList;
import java.util.List;

// Фрагмент добавления квартиры, валидация, загрзка адреса
public class AddFlatFragment extends MvpAppCompatFragment implements AddressView {
    protected Spinner spinnerLocal, spinnerStreet;
    protected EditText flat, family, name, patronym, phone, counter, plomb, plug, readings;
    protected Button save;
    protected List<Local> locals;
    protected List<Street> streets;
    private Spinner spinnerHome;

    @InjectPresenter
    AddressPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_flat, container, false);
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

        spinnerHome = view.findViewById(R.id.home4);
        flat = view.findViewById(R.id.add_flat4);
        save = view.findViewById(R.id.save4);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.loadAddress();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                Local local = locals.get(spinnerLocal.getSelectedItemPosition());
                Street street = local.getStreets().get(spinnerStreet.getSelectedItemPosition());
                Home home = street.getHomes().get(spinnerHome.getSelectedItemPosition());
                if (home.isFlat()) {
                    if (checkValues()) {
                        Flat newFlat = new Flat(Integer.parseInt(flat.getText().toString()));
                        home.addFlat(newFlat);
                        presenter.createAddress(local, new Item(family.getText().toString(), name.getText().toString(),
                                patronym.getText().toString(), phone.getText().toString(), Status.ADDRESS, newFlat.getId(),
                                Integer.parseInt(counter.getText().toString()), Integer.parseInt(plomb.getText().toString()),
                                Integer.parseInt(plug.getText().toString()), Integer.parseInt(readings.getText().toString())));
                        removeValues();
                    }
                }else{
                    Toast.makeText(requireContext(), "В этом доме нет квартир", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean checkValues(){
        EditText[] editTexts = {flat, family, name, patronym, phone, counter, plug, plomb, readings};
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
        EditText[] editTexts = {family, name, patronym, phone, flat, counter, plug, plomb, readings};
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
                streets = locals.get(position).getStreets();
                List<String> namesStreets = new ArrayList<>();
                for (Street street : streets) {
                    namesStreets.add(street.getName());
                }
                ArrayAdapter<String> streetsArrayAdapter = new ArrayAdapter<String>
                        (getContext(), android.R.layout.simple_spinner_item, namesStreets);
                streetsArrayAdapter.setDropDownViewResource(android.R.layout
                        .simple_spinner_dropdown_item);
                spinnerStreet.setAdapter(streetsArrayAdapter);

                spinnerStreet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        List<Home> homes = streets.get(position).getHomes();
                        List<String> namesHomes = new ArrayList<>();
                        for (Home home : homes) {
                            namesHomes.add(String.valueOf(home.getNumber()));
                        }
                        ArrayAdapter<String> homesArrayAdapter = new ArrayAdapter<String>
                                (getContext(), android.R.layout.simple_spinner_item, namesHomes);
                        homesArrayAdapter.setDropDownViewResource(android.R.layout
                                .simple_spinner_dropdown_item);
                        spinnerHome.setAdapter(homesArrayAdapter);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
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
