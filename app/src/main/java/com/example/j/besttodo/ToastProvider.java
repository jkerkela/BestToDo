package com.example.j.besttodo;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

class ToastProvider {

    static void showToastAtCenterOfScreen(String toastText, Context context) {
        Toast toast = Toast.makeText(context, toastText, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
