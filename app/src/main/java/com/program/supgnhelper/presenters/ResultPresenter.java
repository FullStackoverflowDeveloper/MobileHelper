package com.program.supgnhelper.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.program.supgnhelper.model.Item;
import com.program.supgnhelper.model.Model;
import com.program.supgnhelper.model.enums.Status;
import com.program.supgnhelper.view.ResultView;

@InjectViewState
public class ResultPresenter extends MvpPresenter<ResultView> {
    private final Model model;
    public ResultPresenter(){
        model = new Model();
    }

    // Загрузка элементов
    public void loading(int id){
        model.getItem(id, this);
    }

    // Элементы загружены
    public void itemLoaded(Item item){
        getViewState().itemLoaded(item);
    }

    // Обновление элемента
    public void update(Item item){
        model.updateItem(item);
        getViewState().loaded();
    }

    public void errorPr(String msg){
        getViewState().error(msg);
    }
}
