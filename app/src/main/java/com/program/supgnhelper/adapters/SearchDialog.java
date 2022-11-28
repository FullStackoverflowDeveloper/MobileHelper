package com.program.supgnhelper.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.arellomobile.mvp.MvpAppCompatDialogFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.program.supgnhelper.R;
import com.program.supgnhelper.model.Flat;
import com.program.supgnhelper.model.Home;
import com.program.supgnhelper.model.Local;
import com.program.supgnhelper.model.Street;
import com.program.supgnhelper.model.enums.Status;
import com.program.supgnhelper.presenters.AddressPresenter;
import com.program.supgnhelper.view.AddressView;

import java.util.ArrayList;
import java.util.List;

// Окно выбора адреса
public class SearchDialog extends MvpAppCompatDialogFragment implements AddressView {
    private Spinner local, street, homeNumber, flatNumber;
    private TextView flatText;
    private int addressID;
    private List<Local> locals;
    private String title;

    private SearchDialogListener listener;

    @InjectPresenter
    AddressPresenter presenter;

    public void setTitle(String title) {
        this.title = title;
    }

    public interface SearchDialogListener{
        void getDataAddress(int id);
    }



    public void setListener(SearchDialogListener dialogListener){
        listener = dialogListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_search, null);

        local = view.findViewById(R.id.local);
        homeNumber = view.findViewById(R.id.homeNumber);
        street = view.findViewById(R.id.street);
        flatText = view.findViewById(R.id.text_flat);
        flatNumber = view.findViewById(R.id.flatNumber);

        presenter.loadAddress();

        builder.setTitle(this.title).setView(R.layout.dialog_search)
                .setPositiveButton("Искать", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.getDataAddress(addressID);
                        dialog.cancel();
                    }
                });
        builder.setView(view);

        return builder.create();
    }

    // Установка значений в выпадающий список
    @Override
    public void addressLoaded(List<Local> data) {
        this.locals = data;

        List<String> nameLocals = new ArrayList<String>();
        for(Local loc: locals){
            nameLocals.add(loc.getName());
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_spinner_item, nameLocals);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        local.setAdapter(spinnerArrayAdapter);
        local.setSelection(0);

        local.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                                (getContext(), android.R.layout.simple_spinner_item, namesHomes);
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
                                            (getContext(), android.R.layout.simple_spinner_item, nameFlats);
                                    flatsArrayAdapter.setDropDownViewResource(android.R.layout
                                            .simple_spinner_dropdown_item);

                                    flatNumber.setVisibility(View.VISIBLE);
                                    flatText.setVisibility(View.VISIBLE);
                                    flatNumber.setAdapter(flatsArrayAdapter);

                                    flatNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            if(addressID == 0){
                                                addressID = homes.get(position).getId();
                                            }else {
                                                addressID = flats.get(position).getId();
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });
                                } else {
                                    addressID = homes.get(position).getId();
                                    flatNumber.setVisibility(View.GONE);
                                    flatText.setVisibility(View.GONE);
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
    public void saved() {

    }
}
