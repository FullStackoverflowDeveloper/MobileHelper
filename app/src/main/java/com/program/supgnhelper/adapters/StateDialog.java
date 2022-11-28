package com.program.supgnhelper.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.program.supgnhelper.R;
import com.program.supgnhelper.model.enums.Status;

// Окно выбора состояния
public class StateDialog extends DialogFragment {
    private Status status = null;
    private RadioGroup state;
    private StateDialogListener listener;
    private String title;

    public void setTitle(String title) {
        this.title = title;
    }

    public interface StateDialogListener{
        void getDataState(Status status);
    }

    public void setListener(StateDialogListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_state, null);

        state = view.findViewById(R.id.radio_state);

        state.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
               switch (checkedId){
                   case R.id.radio_address:
                       status = Status.ADDRESS;
                       break;
                   case R.id.radio_problem:
                       status = Status.PROBLEM;
                       break;
                   case R.id.radio_task:
                       status = Status.TASK;
                       break;
                   case R.id.radio_work:
                       status = Status.WORK;
                       break;
               }
            }
        });


        builder.setTitle(this.title).setView(R.layout.dialog_search)

                .setPositiveButton("Сформировать", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.getDataState(status);
                        dialog.cancel();
                    }
                });


        builder.setView(view);
        return builder.create();
    }
}
