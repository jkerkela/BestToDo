package com.example.j.besttodo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

/**
 * Created by J on 12/16/2018.
 */

class PopupProvider {

    static PopupWindow providePopUpWindowOnView(View popupView, View view) {
        int[] locationCoordinates = locatePosition(view);
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setElevation(40);
        popupWindow.showAsDropDown(popupView, locationCoordinates[0], locationCoordinates[1]);
        return popupWindow;
    }

    private static int[] locatePosition(View view) {
        int[] locationCoordinates = new int[2];
        view.getLocationOnScreen(locationCoordinates);
        int adjustedYAxisLocation = adjustLocationToViewItemHeight(locationCoordinates[1], view);
        locationCoordinates[1] = adjustedYAxisLocation;
        return locationCoordinates;
    }

    private static int adjustLocationToViewItemHeight(int yAxisLoc, View view) {
        return view.getHeight() + yAxisLoc;
    }
}
