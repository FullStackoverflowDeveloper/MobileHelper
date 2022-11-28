package com.program.supgnhelper.view;

import com.arellomobile.mvp.MvpView;
import com.program.supgnhelper.model.User;

public interface ProfileView extends MvpView {
    void loading();
    void loaded(User user);
}