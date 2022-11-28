package com.program.supgnhelper.adapters;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.program.supgnhelper.R;

import java.util.Calendar;

// Всплывающее окно установки даты
public class DatePicker extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar mCalendar = Calendar.getInstance();
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(requireContext(), (DatePickerDialog.OnDateSetListener)
                requireActivity(), year, month, dayOfMonth);
    }

}