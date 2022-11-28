package com.program.supgnhelper.view;

import com.arellomobile.mvp.MvpView;
import com.program.supgnhelper.model.Item;

public interface ResultView extends MvpView {
    void itemLoaded(Item item);
    void loaded();
    void error(String msg);
}
