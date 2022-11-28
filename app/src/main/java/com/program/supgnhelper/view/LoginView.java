package com.program.supgnhelper.view;

import com.arellomobile.mvp.MvpView;
import com.program.supgnhelper.model.enums.Role;

public interface LoginView extends MvpView {
    void loading();
    void loginAccepted(Role role, int userID);
    void error();
}
