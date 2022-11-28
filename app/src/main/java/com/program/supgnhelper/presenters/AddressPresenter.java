package com.program.supgnhelper.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.program.supgnhelper.model.Item;
import com.program.supgnhelper.model.Local;
import com.program.supgnhelper.model.Model;
import com.program.supgnhelper.view.AddressView;

import java.util.ArrayList;
import java.util.List;

@InjectViewState
public class AddressPresenter extends MvpPresenter<AddressView> {
    private Model model;

    public AddressPresenter(){
        model = new Model();
    }

    // Загрузка адреса
    public void loadAddress(){
        model.getAddress(this);
    }

    // Адрес загружен
    public void addressLoaded(List<Local> data){
        getViewState().addressLoaded(data);
    }

    // Создание обьекта
    public void createAddress(Local local, Item item){
        model.updateAddress(local);
        model.addTask(item);
        getViewState().saved();
    }

    // Обновление адреса
    public void updateAddress(Local local){
        model.updateAddress(local);
        getViewState().saved();
    }

    // Добавление района
    public void saveLocal(String localName){
        Local local = new Local(localName, new ArrayList<>());
        model.addLocal(local);
    }
}
