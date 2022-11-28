package com.program.supgnhelper.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.program.supgnhelper.model.Item;
import com.program.supgnhelper.model.Local;
import com.program.supgnhelper.model.Model;
import com.program.supgnhelper.model.enums.Status;
import com.program.supgnhelper.view.CatalogView;
import com.program.supgnhelper.view.EditView;

import java.util.List;

@InjectViewState
public class EditPresenter extends MvpPresenter<EditView> {
    private final Model model;
    public EditPresenter(){
        model = new Model();
    }

    // Загрузка элемента
    public void loading(int id){
        model.getItem(id, this);
    }

    // Загрузка элемента по адресу
    public void loadingByAddress(int id){
        model.getItemByAddress(id, this);
    }

    // Загрузка адреса
    public void loadAddress(){
        model.getAddress(this);
    }

    // Адрес загружен
    public void addressLoad(List<Local> data){
        getViewState().addressLoaded(data);
    }

    // Элемент загружен
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
