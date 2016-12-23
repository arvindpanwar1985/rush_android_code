package com.hoffmans.rush.ui.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by devesh on 23/12/16.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        Calendar c = Calendar.getInstance();
        int y = c.get(Calendar.YEAR)+4;
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(), this, y, m,0);
        datePickerDialog.setTitle("Select card Expiration.");
        if (datePickerDialog.getDatePicker() != null) {

        }
        return datePickerDialog;
    }
    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }
}
