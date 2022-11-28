package com.program.supgnhelper.view;

import com.arellomobile.mvp.MvpView;
import com.program.supgnhelper.model.Item;

import java.util.List;

public interface TaskView extends MvpView {
    void loading();
    void loaded(List<Item> data);
    void showMsg(String msg);
    void showEmpty();
}
