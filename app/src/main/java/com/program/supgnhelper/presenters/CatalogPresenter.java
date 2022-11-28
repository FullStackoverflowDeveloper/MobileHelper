package com.program.supgnhelper.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.program.supgnhelper.model.Item;
import com.program.supgnhelper.model.Local;
import com.program.supgnhelper.model.Model;
import com.program.supgnhelper.view.CatalogView;

import java.util.List;

@InjectViewState
public class CatalogPresenter extends MvpPresenter<CatalogView> {
    private final Model model;

    public CatalogPresenter() {
        model = new Model();
    }

    // Загрузка всех элементов
    public void loadAllItems(){
        model.loadAllItems(this);
    }

    // Загрузка элементов по адресу
    public void loadItems(int id){
        model.getItemsByAddress(this, id);
    }

    // Элементы загружены
    public void itemsLoaded(List<Item> items){
        getViewState().itemsLoaded(items);
    }
}