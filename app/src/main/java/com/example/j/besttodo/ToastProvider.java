package com.example.j.besttodo;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by J on 12/16/2018.
 */

class ToastProvider {

    static void showToastAtCenterOfScreen(String toastText, Context context) {
        Toast toast = Toast.makeText(context, toastText, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
