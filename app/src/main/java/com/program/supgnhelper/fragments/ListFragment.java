package com.program.supgnhelper.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.program.supgnhelper.R;
import com.program.supgnhelper.activities.CreateWorkerActivity;
import com.program.supgnhelper.activities.MainActivity;
import com.program.supgnhelper.adapters.UserAdapter;
import com.program.supgnhelper.model.User;
import com.program.supgnhelper.presenters.ListMasterPresenter;
import com.program.supgnhelper.view.ListMasterView;

import java.util.ArrayList;
import java.util.List;

// Фрагмент списка работников
public class ListFragment extends MvpAppCompatFragment implements ListMasterView {
    @InjectPresenter
    public ListMasterPresenter presenter;

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private ProgressBar spinner;
    private TextView listIsEmpty;
    private FloatingActionButton fabAdd;
    private Button btn_restart;
    private final List<User> workers = new ArrayList<>();

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_list_master, container, false);

        requireActivity().setTitle("Сотрудники");

        BottomNavigationView navigationView = requireActivity().findViewById(R.id.bottomNavigationView);
        navigationView.setVisibility(View.GONE);

        spinner = view.findViewById(R.id.progressBar);
        listIsEmpty = view.findViewById(R.id.nothing);
        btn_restart = view.findViewById(R.id.btn_again);
        fabAdd = view.findViewById(R.id.fab_add);

        // При нажатии на кнопку нового элемента переход к новому фрагменту
        fabAdd.setOnClickListener(v -> startActivity(new Intent(getContext(), CreateWorkerActivity.class)));

        // При нажатии кнопки перезагрузка, попытка загрузки, запуск индикатора
        btn_restart.setOnClickListener(view1 -> presenter.loadDataPr());

        // Установка адаптера на recyclerview
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UserAdapter(workers);

        adapter.setClickListener(position -> {
            navigationView.setVisibility(View.VISIBLE);
            SharedPreferences preferences = requireActivity().getSharedPreferences("pref", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("worker", workers.get(position).getId());
            editor.apply();
            getActivity().getSupportFragmentManager().beginTransaction().remove(this);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, new TaskFragment()).commit();
        });
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        presenter.loadDataPr();
    }

    @Override
    public void loading() {
        spinner.setVisibility(View.VISIBLE);
        listIsEmpty.setVisibility(View.GONE);
        btn_restart.setVisibility(View.GONE);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void loaded(List<User> data) {
        if (data.size() == 0){
            showEmpty();
            workers.clear();
            adapter.notifyDataSetChanged();
        }else{
            //Если не ноль, выключение всего остального(индикатора загрузки, надписи что ничего не найдено)
            spinner.setVisibility(View.GONE);
            listIsEmpty.setVisibility(View.GONE);
            btn_restart.setVisibility(View.GONE);

            // Обновление списка
            workers.clear();
            workers.addAll(data);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showMsg(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showEmpty() {
        spinner.setVisibility(View.GONE);
        listIsEmpty.setVisibility(View.VISIBLE);
        btn_restart.setVisibility(View.VISIBLE);
    }
}
