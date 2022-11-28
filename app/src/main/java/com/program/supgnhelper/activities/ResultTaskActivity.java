package com.program.supgnhelper.activities;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.program.supgnhelper.R;
import com.program.supgnhelper.model.Item;
import com.program.supgnhelper.model.enums.Status;
import com.program.supgnhelper.presenters.ResultPresenter;
import com.program.supgnhelper.view.ResultView;

public class ResultTaskActivity extends MvpAppCompatActivity implements ResultView {
    private EditText counter, readings, plomb, plugPlomb, comment;
    private CheckBox isProblem;
    private Button send;
    private Item item;

    @InjectPresenter
    ResultPresenter presenter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Получение ссылок на элементы
        counter = findViewById(R.id.numberCounter);
        readings = findViewById(R.id.readings);
        plomb = findViewById(R.id.plomb);
        plugPlomb = findViewById(R.id.plug_plomb);
        comment = findViewById(R.id.comment);
        isProblem = findViewById(R.id.problem);

        // Отключение доступа к переменным
        disable(counter);
        disable(plomb);
        disable(plugPlomb);

        // загрузка элемента по id
        int id = getIntent().getIntExtra("id", 0);
        presenter.loading(id);

        // При нажатии на кнопку обновление задачи, заявки
        send = findViewById(R.id.send);
        send.setOnClickListener(view -> {
            if (checkValues()) {
                item.setCurrentReadings(Integer.parseInt(readings.getText().toString()));
                if (!comment.getText().toString().trim().isEmpty()) {
                    item.setComment(comment.getText().toString());
                }
                if (isProblem.isChecked()) {
                    item.setProblem(true);
                    item.setStatus(Status.PROBLEM);
                } else{
                    item.setProblem(false);
                    item.setStatus(Status.ADDRESS);
                }
                presenter.update(item);
            }
        });
    }

    // Проверка значений
    private boolean checkValues(){
        if(readings.getText().toString().isEmpty()){
            readings.setError("Введите значение");
            readings.requestFocus();
            return false;
        }

        return true;
    }

    // Отключение поля ввода
    private void disable(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
    }

    // При загрузке задачи ввод значений в поля
    @Override
    public void itemLoaded(Item item) {
        counter.setText(String.valueOf(item.getNumberCounter()));
        plomb.setText(String.valueOf(item.getPlomb()));
        plugPlomb.setText(String.valueOf(item.getPlugPlomb()));
        counter.setText(String.valueOf(item.getNumberCounter()));
        comment.setText(item.getComment());
        this.item = item;
    }

    // Выход если заявка обновлена
    @Override
    public void loaded() {
        sendMsg("Successful");
        finish();
    }

    @Override
    public void error(String msg) {
        sendMsg(msg);
    }

    private void sendMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
