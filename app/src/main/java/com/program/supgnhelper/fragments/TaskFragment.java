package com.program.supgnhelper.fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.program.supgnhelper.R;
import com.program.supgnhelper.activities.CreateTaskActivity;
import com.program.supgnhelper.model.Item;
import com.program.supgnhelper.model.enums.Role;
import com.program.supgnhelper.model.enums.Status;
import com.program.supgnhelper.view.TaskView;

import java.util.List;

// Фрагмент задачи
public class TaskFragment extends MainFragment implements TaskView {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        status = Status.TASK;
        init(view, requireContext());

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (role == Role.MASTER) {
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, new ListFragment()).commit();
                        }
                        return true;
                    }
                }
                return false;
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // При нажатии на кнопку нового элемента переход к новому фрагменту
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CreateTaskActivity.class);
            intent.putExtra("work", false);
            startActivity(intent);
        });

        search.setVisibility(View.GONE);

        presenter.loadDataPr(status, worker);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void loaded(List<Item> data) {
        data = dateFilter(data);
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
}