package com.program.supgnhelper.view;

import com.arellomobile.mvp.MvpView;
import com.program.supgnhelper.model.Local;

import java.util.List;

public interface AddressView extends MvpView {
    void addressLoaded(List<Local> data);
    void saved();
}
