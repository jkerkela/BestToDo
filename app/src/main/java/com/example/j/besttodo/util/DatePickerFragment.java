package com.example.j.besttodo.util;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.j.besttodo.TodoItem;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {

    private TodoItem todoItem;
    private EditText todoItemDate;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
    }

    public void setTodoItemHandle(TodoItem todoItem, EditText todoItemDate) {
        this.todoItem = todoItem;
        this.todoItemDate = todoItemDate;
    }

    private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    setTodoItemDate(view);
                }
            };

    private void setTodoItemDate(DatePicker view) {
        int day = view.getDayOfMonth();
        int month = view.getMonth()+1;
        int year =  view.getYear();
        String date = "Schedule: " + day + "-" + month + "-" + year;
        todoItemDate.setText(date);
        todoItem.setSchedule(date);
    }
}