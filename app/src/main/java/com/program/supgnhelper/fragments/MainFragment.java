package com.program.supgnhelper.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.program.supgnhelper.R;
import com.program.supgnhelper.activities.MainActivity;
import com.program.supgnhelper.activities.ResultTaskActivity;
import com.program.supgnhelper.adapters.DatePicker;
import com.program.supgnhelper.model.Item;
import com.program.supgnhelper.adapters.ItemAdapter;
import com.program.supgnhelper.model.enums.Role;
import com.program.supgnhelper.model.enums.Status;
import com.program.supgnhelper.presenters.TaskPresenter;
import com.program.supgnhelper.view.TaskView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Абстарактный класс для заявок и задач
public abstract class MainFragment extends MvpAppCompatFragment implements TaskView {
    @InjectPresenter
    public TaskPresenter presenter;

    protected SearchView search;
    private RecyclerView recyclerView;
    protected ItemAdapter adapter;
    protected ProgressBar spinner;
    protected TextView listIsEmpty;
    protected FloatingActionButton fabAdd;
    protected FloatingActionButton fabDate;
    protected int worker;
    protected Button btn_restart;
    protected final List<Item> items = new ArrayList<>();

    protected Status status = null;
    protected Role role = null;

    protected void init(View view, Context context) {
        // Получение роли, id работника
        SharedPreferences preferences = context.getSharedPreferences("pref", MODE_PRIVATE);
        worker = preferences.getInt("worker", 0);

        boolean isMaster = preferences.getBoolean("master", false);
        if(isMaster){
            role = Role.MASTER;
        }else{
            role = Role.WORKER;
        }

        requireActivity().setTitle("Заявки/Задачи");

        spinner = view.findViewById(R.id.progressBar);
        listIsEmpty = view.findViewById(R.id.nothing);
        btn_restart = view.findViewById(R.id.btn_again);
        fabAdd = view.findViewById(R.id.fab_add);
        fabDate = view.findViewById(R.id.fab_date);

        // Окно с датой
        fabDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker picker = new DatePicker();
                picker.show(getParentFragmentManager(), "DATE PICK");
            }
        });
        search = view.findViewById(R.id.search_line);

        if (role == Role.WORKER){
            fabAdd.setVisibility(View.GONE);
        }


        btn_restart.setOnClickListener(button -> presenter.loadDataPr(status, worker));

        BottomNavigationView navigationView = getActivity().findViewById(R.id.bottomNavigationView);
        navigationView.setVisibility(View.VISIBLE);

        // Установка адаптера на recyclerview
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ItemAdapter(items, role);

        adapter.setClickListener(new ItemAdapter.ClickListener() {
            @Override
            public void viewClick(int position) {

            }

            @Override
            public void itemClick(int position) {
                Intent intent = new Intent(context, ResultTaskActivity.class);
                intent.putExtra("id", items.get(position).getId());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        // Если работник, то подмена шторки
        if (role == Role.WORKER){
            NavigationView navigation = ((MainActivity) getActivity()).getNavigationView();
            navigation.getMenu().clear();
            navigation.inflateMenu(R.menu.worker_drawer_view);
        }

        ((MainActivity) getActivity()).setFragmentRefreshListener(new MainActivity.FragmentRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadDataPr(status, worker);
            }
        });
    }

    // Фильтрация по дате
    protected List<Item> dateFilter(List<Item> list){
        List<Item> data = new ArrayList<>();

        SharedPreferences preferences = getActivity().getSharedPreferences("pref", MODE_PRIVATE);

        Long time = preferences.getLong("date", 0);
        Date date = new Date(time);

        for (Item item: list){
            Date currentDate = item.getDate();
            Log.d("TAG", date.toString());
            Log.d("TAG", currentDate.toString());
            if (date.getYear() == currentDate.getYear() && date.getMonth() == currentDate.getMonth()
                    && date.getDate() == currentDate.getDate()){
                data.add(item);
            }
        }
        return data;
    }

    @Override
    public void loading() {
        spinner.setVisibility(View.VISIBLE);
        listIsEmpty.setVisibility(View.GONE);
        btn_restart.setVisibility(View.GONE);
    }

    @Override
    public void showEmpty() {
        spinner.setVisibility(View.GONE);
        listIsEmpty.setVisibility(View.VISIBLE);
        btn_restart.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMsg(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }
}
