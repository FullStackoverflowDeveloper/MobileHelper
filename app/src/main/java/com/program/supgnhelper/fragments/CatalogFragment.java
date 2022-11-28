package com.program.supgnhelper.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.program.supgnhelper.R;
import com.program.supgnhelper.activities.EditTaskActivity;
import com.program.supgnhelper.adapters.CatalogAdapter;
import com.program.supgnhelper.adapters.ItemAdapter;
import com.program.supgnhelper.adapters.SearchDialog;
import com.program.supgnhelper.adapters.StateDialog;
import com.program.supgnhelper.model.Flat;
import com.program.supgnhelper.model.Home;
import com.program.supgnhelper.model.Item;
import com.program.supgnhelper.model.Local;
import com.program.supgnhelper.model.Street;
import com.program.supgnhelper.model.enums.Role;
import com.program.supgnhelper.model.enums.Status;
import com.program.supgnhelper.presenters.CatalogPresenter;
import com.program.supgnhelper.view.CatalogView;

import java.util.ArrayList;
import java.util.List;

public class CatalogFragment extends MvpAppCompatFragment implements CatalogView, SearchDialog.SearchDialogListener, StateDialog.StateDialogListener {
    @InjectPresenter
    public CatalogPresenter presenter;

    private RecyclerView recyclerView;
    private CatalogAdapter adapter;
    private ProgressBar spinner;
    private TextView listIsEmpty;
    private Button btn_restart;
    private final List<Item> items = new ArrayList<>();
    private List<Local> locals;
    private int addressID = 0;
    private Status status = null;

    private FloatingActionButton search;
    private FloatingActionButton state;

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);
        BottomNavigationView navigationView = requireActivity().findViewById(R.id.bottomNavigationView);
        navigationView.setVisibility(View.GONE);
        getActivity().setTitle("Каталог");

        spinner = view.findViewById(R.id.progressBar);
        listIsEmpty = view.findViewById(R.id.nothing);
        btn_restart = view.findViewById(R.id.btn_again);

        presenter.loadAllItems();

        // При нажатии на кнопку запуск соответствующих окон
        search = view.findViewById(R.id.fab_search_address);
        search.setOnClickListener(v -> {
            SearchDialog dialog = new SearchDialog();
            dialog.setTitle("Поиск по адресу");
            dialog.setListener(CatalogFragment.this);
            openDialog(dialog);
        });

        state = view.findViewById(R.id.fab_search_state);
        state.setOnClickListener(v -> {
            StateDialog dialog = new StateDialog();
            dialog.setTitle("Отчет о состоянии");
            dialog.setListener(CatalogFragment.this);
            openDialog(dialog);
        });

        btn_restart.setOnClickListener(button -> presenter.loadItems(addressID));

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CatalogAdapter(items, getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }

    // Запуск диалога
    private void openDialog(DialogFragment dialog){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        dialog.show(manager, "myDialog");
    }

    // При нажатии на элемент списка запуск новой активити
    @Override
    public void onStart() {
        super.onStart();

        adapter.setClickListener(new CatalogAdapter.ClickListener() {
            @Override
            public void itemClick(int position) {
                Intent intent =new Intent(getContext(), EditTaskActivity.class);
                intent.putExtra("id", items.get(position).getId());
                startActivity(intent);
            }
        });
    }


    @Override
    public void addressLoaded(List<Local> locals) {
        this.locals = locals;
    }

    // Фильтрация по адресу
    private List<Item> filter(List<Item> items){
        if (status == null){
            return items;
        } else if (status == Status.ADDRESS){
            return items;
        }

        List<Item> data = new ArrayList<>();

        for(Item item: items){
            if (item.getStatus() == status){
                data.add(item);
            }
        }
        return data;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void itemsLoaded(List<Item> itemList) {
        List<Item> data = filter(itemList);
        if (data.size() == 0){
            showEmpty();
            items.clear();
            adapter.notifyDataSetChanged();
        }else{
            //Если не ноль, выключение всего остального(индикатора загрузки, надписи что ничего не найдено)
            spinner.setVisibility(View.GONE);
            listIsEmpty.setVisibility(View.GONE);
            btn_restart.setVisibility(View.GONE);

            // Обновление списка
            items.clear();
            items.addAll(data);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showEmpty() {
        listIsEmpty.setVisibility(View.VISIBLE);
        btn_restart.setVisibility(View.VISIBLE);
    }

    // Получение данных с диалога адресв
    @Override
    public void getDataAddress(int addressID) {
        this.addressID = addressID;
        this.status = null;
        presenter.loadItems(addressID);
    }

    // Получение данных с диалога состояния
    @Override
    public void getDataState(Status status) {
        this.status = status;
        this.addressID = 0;

        presenter.loadAllItems();
    }
}
