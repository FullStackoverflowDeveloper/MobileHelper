package com.program.supgnhelper.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.program.supgnhelper.model.Model;
import com.program.supgnhelper.model.User;
import com.program.supgnhelper.view.ListMasterView;

import java.util.List;

@InjectViewState
public class ListMasterPresenter extends MvpPresenter<ListMasterView> {
    private final Model model;

    public ListMasterPresenter(){
        model = new Model();
    }

    // Загрузка работников
    public void loadDataPr(){
        getViewState().loading();
        model.getWorkers(this);
    }

    // Пользователи загружены
    public void dataLoadedPr(List<User> data){
        getViewState().loaded(data);
    }

    public void showMsgPr(String message){
        getViewState().showMsg(message);
    }
}
