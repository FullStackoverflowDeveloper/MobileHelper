package com.program.supgnhelper.view;

import com.arellomobile.mvp.MvpView;
import com.program.supgnhelper.model.Item;
import com.program.supgnhelper.model.Local;

import java.util.List;

public interface CatalogView extends MvpView {
    void addressLoaded(List<Local> locals);
    void itemsLoaded(List<Item> items);
    void showEmpty();
}
