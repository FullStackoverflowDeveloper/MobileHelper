package com.program.supgnhelper.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.program.supgnhelper.model.Model;
import com.program.supgnhelper.model.enums.Role;
import com.program.supgnhelper.view.LoginView;

@InjectViewState
public class LoginPresenter extends MvpPresenter<LoginView> {
    private final Model model;

    public LoginPresenter(){
        model = new Model();
    }

    // Проверка наличие пользователя
    public void loadingPr(String email, String password){
        model.checkUser(email, password, this);
    }

    // Пользователь загружен
    public void userFound(Role role, int userID){
        getViewState().loginAccepted(role, userID);
    }

    public void errorPr(){
        getViewState().error();
    }
}
