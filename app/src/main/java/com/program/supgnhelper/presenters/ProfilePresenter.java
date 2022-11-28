package com.program.supgnhelper.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.program.supgnhelper.model.Model;
import com.program.supgnhelper.model.User;
import com.program.supgnhelper.view.ProfileView;

@InjectViewState
public class ProfilePresenter extends MvpPresenter<ProfileView> {
    private final Model model;

    public ProfilePresenter(){
        model = new Model();
    }

    // Загрузка пользователя
    public void loading(int id){
        model.getMaster(id, this);
    }

    // Данные пользователя загружены
    public void loaded(User user){
        getViewState().loaded(user);
    }
}