package com.program.supgnhelper.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.annotation.Nullable;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.program.supgnhelper.R;
import com.program.supgnhelper.model.Flat;
import com.program.supgnhelper.model.Home;
import com.program.supgnhelper.model.Item;
import com.program.supgnhelper.model.Local;
import com.program.supgnhelper.model.Model;
import com.program.supgnhelper.model.Street;
import com.program.supgnhelper.model.enums.Status;
import com.program.supgnhelper.presenters.EditPresenter;
import com.program.supgnhelper.view.EditView;

import java.util.ArrayList;
import java.util.List;

public class EditTaskActivity extends MvpAppCompatActivity implements EditView {
    private EditText family, name, patronym, phonenumber, comment;
    private EditText counter, readings, plomb, plugPlomb;
    private Spinner local, street, homeNumber, flatNumber;
    private Button save;
    private Status status = Status.TASK;
    private List<Local> locals;
    private int addressID;
    private Item item;

    @InjectPresenter
    EditPresenter presenter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        local = findViewById(R.id.local);
        homeNumber = findViewById(R.id.homeNumber);
        street = findViewById(R.id.street);
        flatNumber = findViewById(R.id.flatNumber);
        family = findViewById(R.id.family);
        name = findViewById(R.id.name);
        patronym = findViewById(R.id.patronym);
        phonenumber = findViewById(R.id.phoneNumber);
        comment = findViewById(R.id.comment);
        counter = findViewById(R.id.numberCounter);
        readings = findViewById(R.id.readings);
        plomb = findViewById(R.id.plomb);
        plugPlomb = findViewById(R.id.plug_plomb);

        // Загрузка адреса и id пользователя
        presenter.loadAddress();

        save = findViewById(R.id.save);
        save.setOnClickListener(view -> {
            // Обновление элемента
            if (checkValues()) {
                item.setAddressId(addressID);
                item.setFullAddress(Model.getAddress(locals, addressID));
                item.setPhoneNumber(phonenumber.getText().toString());
                item.setName(name.getText().toString());
                item.setFamily(family.getText().toString());
                item.setPatronym(patronym.getText().toString());
                item.setStatus(status);
                item.setComment(comment.getText().toString());
                item.setPlomb(Integer.parseInt(plomb.getText().toString()));
                item.setPlugPlomb(Integer.parseInt(plugPlomb.getText().toString()));
                item.setCurrentReadings(Integer.parseInt(readings.getText().toString()));
                item.setAddressId(addressID);

                presenter.update(item);
            }
        });
    }

    // Валидация полей
    private boolean checkValues(){
        EditText[] editTexts = {family, name, patronym, counter, plomb, plugPlomb, readings};
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

        if(plugPlomb.getText().toString().length() < 5){
            plugPlomb.setError("Минимальная длина 5 символов");
            plugPlomb.requestFocus();
            return false;
        }

        return true;
    }

    // Установка данных в поля
    private void saveData(Item item){
        phonenumber.setText(item.getPhoneNumber());

        comment.setText(item.getComment());
        family.setText(item.getFamily());
        name.setText(item.getName());
        patronym.setText(item.getPatronym());
        counter.setText(String.valueOf(item.getNumberCounter()));
        readings.setText(String.valueOf(item.getCurrentReadings()));
        plomb.setText(String.valueOf(item.getPlomb()));
        plugPlomb.setText(String.valueOf(item.getPlugPlomb()));
        status = item.getStatus();

        addressID = item.getAddressId();
        this.item = item;
    }

    @Override
    public void itemLoaded(Item item) {
        saveData(item);
        getAddress();
    }

    // Алгоритм назначения данных в выпадающие списки
    @Override
    public void addressLoaded(List<Local> locals) {
        this.locals = locals;

        List<String> nameLocals = new ArrayList<String>();
        for(Local loc: locals){
            nameLocals.add(loc.getName());
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, nameLocals);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        local.setAdapter(spinnerArrayAdapter);

        local.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<Street> streets = locals.get(position).getStreets();
                List<String> namesStreets = new ArrayList<>();
                for (Street street : streets) {
                    namesStreets.add(street.getName());
                }
                ArrayAdapter<String> streetsArrayAdapter = new ArrayAdapter<String>
                        (getApplicationContext(), android.R.layout.simple_spinner_item, namesStreets);
                streetsArrayAdapter.setDropDownViewResource(android.R.layout
                        .simple_spinner_dropdown_item);
                street.setAdapter(streetsArrayAdapter);

                street.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        List<Home> homes = streets.get(position).getHomes();
                        List<String> namesHomes = new ArrayList<>();
                        for (Home home : homes) {
                            namesHomes.add(String.valueOf(home.getNumber()));
                        }
                        ArrayAdapter<String> homesArrayAdapter = new ArrayAdapter<String>
                                (getApplicationContext(), android.R.layout.simple_spinner_item, namesHomes);
                        homesArrayAdapter.setDropDownViewResource(android.R.layout
                                .simple_spinner_dropdown_item);
                        homeNumber.setAdapter(homesArrayAdapter);

                        homeNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                boolean isFlat = homes.get(position).isFlat();
                                if (isFlat) {
                                    List<Flat> flats = homes.get(position).getFlats();
                                    List<String> nameFlats = new ArrayList<>();
                                    for (Flat flat : flats) {
                                        nameFlats.add(String.valueOf(flat.getNumber()));
                                    }
                                    ArrayAdapter<String> flatsArrayAdapter = new ArrayAdapter<String>
                                            (getApplicationContext(), android.R.layout.simple_spinner_item, nameFlats);
                                    flatsArrayAdapter.setDropDownViewResource(android.R.layout
                                            .simple_spinner_dropdown_item);

                                    flatNumber.setVisibility(View.VISIBLE);
                                    flatNumber.setAdapter(flatsArrayAdapter);

                                    flatNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            addressID = flats.get(position).getId();
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });
                                } else {
                                    addressID = homes.get(position).getId();
                                    flatNumber.setVisibility(View.GONE);
                                }
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
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        if (id != 0) {
            presenter.loading(id);
        }
    }

    public void getAddress(){
        int localName;
        int streetName;
        int homeName;
        int flatName;
        for (Local local: locals){
            localName = locals.indexOf(local);
            for (Street street: local.getStreets()){
                streetName = local.getStreets().indexOf(street);
                for(Home home: street.getHomes()){
                    homeName = street.getHomes().indexOf(home);
                    if (addressID == home.getId()){
                        this.local.setSelection(localName);
                        this.street.setSelection(streetName);
                        this.homeNumber.setSelection(homeName);
                        break;
                    }
                    if (home.isFlat()){
                        for (Flat flat: home.getFlats()){
                            if (addressID == flat.getId()){
                                flatName = home.getFlats().indexOf(flat);
                                this.local.setSelection(localName);
                                this.street.setSelection(streetName);
                                this.homeNumber.setSelection(homeName);
                                this.flatNumber.setSelection(flatName);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void loaded() {
        finish();
    }

    @Override
    public void error(String msg) {

    }
}
