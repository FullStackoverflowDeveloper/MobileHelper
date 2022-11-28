package com.program.supgnhelper.view;

import com.arellomobile.mvp.MvpView;
import com.program.supgnhelper.model.Item;
import com.program.supgnhelper.model.Local;

import java.util.List;

public interface EditView extends MvpView {
    void itemLoaded(Item item);
    void addressLoaded(List<Local> locals);
    void loaded();
    void error(String msg);
}