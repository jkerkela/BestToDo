package com.example.j.besttodo;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

class PopUpProvider {

    static PopupWindow providePopUpWindowOnViewAtClickLocation(View popupView, View view) {
        int[] locationCoordinates = locatePosition(view);
        PopupWindow popupWindow = getPopupWithDefaultParameters(popupView);
        popupWindow.showAsDropDown(popupView, locationCoordinates[0], locationCoordinates[1]);
        return popupWindow;
    }

    static PopupWindow providePopUpWindowOnItemLocation(View popupView) {
        PopupWindow popupWindow = getPopupWithDefaultParameters(popupView);
        popupWindow.showAtLocation(popupView, Gravity.CLIP_HORIZONTAL, 0, 250);
        return popupWindow;
    }

    private static PopupWindow getPopupWithDefaultParameters(View popupView) {
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setElevation(40);
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

    static PopupWindow providePopUpWindowOnViewAtCenter(View popupView) {
        PopupWindow popupWindow = getPopupWithDefaultParameters(popupView);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
        return popupWindow;
    }

    static void dimBackgroundOfPopup(PopupWindow popupWindow, Context context) {
        View parentView = (View) popupWindow.getContentView().getParent();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) parentView.getLayoutParams();
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.4f;
        windowManager.updateViewLayout(parentView, p);
    }

}
