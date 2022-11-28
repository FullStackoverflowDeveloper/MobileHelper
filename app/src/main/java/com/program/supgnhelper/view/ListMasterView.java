package com.program.supgnhelper.view;

import com.arellomobile.mvp.MvpView;
import com.program.supgnhelper.model.User;

import java.util.List;

public interface ListMasterView extends MvpView {
    void loading();
    void loaded(List<User> data);
    void showMsg(String msg);
    void showEmpty();
}
