package com.program.supgnhelper.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.program.supgnhelper.model.Model;
import com.program.supgnhelper.view.AboutView;

@InjectViewState
public class AboutPresenter extends MvpPresenter<AboutView> {
    private final Model model;

    public AboutPresenter(){
        model = new Model();
    }

    // Загрузка описания
    public void loadTitle(){
        model.getDescription(this);
    }

    // Описание получено
    public void titleLoaded(String title){
        getViewState().titleLoaded(title);
    }
}
