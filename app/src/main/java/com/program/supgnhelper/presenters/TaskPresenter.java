package com.program.supgnhelper.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.program.supgnhelper.model.Item;
import com.program.supgnhelper.model.Model;
import com.program.supgnhelper.model.enums.Status;
import com.program.supgnhelper.view.TaskView;

import java.util.List;

@InjectViewState
public class TaskPresenter extends MvpPresenter<TaskView> {
    private final Model model;

    public TaskPresenter(){
        model = new Model();
    }

    // Загрузка заявок или задач
    public void loadDataPr(Status status, int worker){
        getViewState().loading();
        if (status == Status.WORK) {
            model.getWorks(this, worker);
        } else {
            model.getTasks(this, worker);
        }
    }

    // Поиск по номеру
    public void searchPr(String query){
        model.searchItems(this, query);
    }

    // Элемент загружен
    public void dataLoadedPr(List<Item> data){
        getViewState().loaded(data);
    }

    public void showMsgPr(String message){
        getViewState().showMsg(message);
    }
}
