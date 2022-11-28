package com.program.supgnhelper.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.snackbar.Snackbar;
import com.program.supgnhelper.R;
import com.program.supgnhelper.model.Flat;
import com.program.supgnhelper.model.Home;
import com.program.supgnhelper.model.Item;
import com.program.supgnhelper.model.Local;
import com.program.supgnhelper.model.Model;
import com.program.supgnhelper.model.Street;
import com.program.supgnhelper.model.enums.Status;
import com.program.supgnhelper.model.enums.Type;
import com.program.supgnhelper.presenters.EditPresenter;
import com.program.supgnhelper.view.EditView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreateTaskActivity extends MvpAppCompatActivity implements EditView {
    private Spinner local, street, homeNumber, flatNumber, workType;
    private Button save;
    private List<Local> locals;
    private int addressID;
    private Status status;
    private Item item;

    @InjectPresenter
    EditPresenter presenter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        local = findViewById(R.id.local);
        homeNumber = findViewById(R.id.homeNumber);
        street = findViewById(R.id.street);
        flatNumber = findViewById(R.id.flatNumber);
        workType = findViewById(R.id.work_type);

        // Загрузка статуса и адреса
        Intent intent = getIntent();

        if(intent.getBooleanExtra("work", false)){
            status = Status.WORK;
            workType.setVisibility(View.VISIBLE);
        }else{
            status = Status.TASK;
            workType.setVisibility(View.GONE);
        }

        presenter.loadAddress();

        List<String> numbers = new ArrayList();
        for(int i = 1; i < 51; i++){
            numbers.add(String.valueOf(i));
        }

        save = findViewById(R.id.save);
        save.setOnClickListener(view -> {
            // Получение id пользователя и даты
            SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);

            int id = preferences.getInt("worker", 0);
            long time = preferences.getLong("date", 0);
            Date date;

            if (time == 0){
                date = Calendar.getInstance().getTime();
            }else {
                date = new Date(time);
            }

            if (item != null) {
                // Обновление элемента
                item.setFullAddress(Model.getAddress(locals, addressID));
                item.setWorkerId(id);
                item.setDate(date);
                item.setStatus(status);
                if (Status.WORK == status) {
                    if (workType.getSelectedItemPosition() == 0) {
                        item.setType(Type.GAS_LEAK);
                    } else {
                        item.setType(Type.COUNTER_REPLACEMENT);
                    }
                }
                presenter.update(item);
            }else {
                Snackbar.make(local, "Обьект не введен в эксплуатацию", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void itemLoaded(Item item) {
        this.item = item;
    }

    // Назначение элементов в выпадающий список
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
                for(Street street: streets){
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
                        for(Home home: homes){
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
                                if(isFlat){
                                    List<Flat> flats = homes.get(position).getFlats();
                                    List<String> nameFlats = new ArrayList<>();
                                    for (Flat flat: flats){
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
                                            presenter.loadingByAddress(addressID);
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });
                                }else {
                                    addressID = homes.get(position).getId();
                                    flatNumber.setVisibility(View.GONE);
                                    presenter.loadingByAddress(addressID);
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
    }

    @Override
    public void loaded() {
        finish();
    }

    @Override
    public void error(String msg) {

    }
}
