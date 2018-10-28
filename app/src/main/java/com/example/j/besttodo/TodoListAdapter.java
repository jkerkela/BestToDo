package com.example.j.besttodo;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import java.util.List;

public class TodoListAdapter extends ArrayAdapter<TodoItem> {

    private Activity mContext;
    private int mTodoItemLayout;

    TodoListAdapter(@NonNull Activity context, int todoItemLayout, @NonNull List<TodoItem> todoItemList) {
        super(context, todoItemLayout, todoItemList);
        mContext = context;
        mTodoItemLayout = todoItemLayout;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listView = convertView;
        if(listView == null) {
            LayoutInflater layoutInflater = mContext.getLayoutInflater();
            listView = layoutInflater.inflate(mTodoItemLayout, parent, false);
            ImageButton todoItemActionButton = listView.findViewById(R.id.todoItemActionButton);
            addListenerToTodoItemActionButton(todoItemActionButton);
        }

        return listView;
    }

    private void addListenerToTodoItemActionButton(ImageButton todoItemActionButton) {
        todoItemActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] locationCoordinates = locatePosition(view);
                showPopUpAtLocation(locationCoordinates);
            }
        });
    }

    private int[] locatePosition(View view) {
        int[] locationCoordinates = new int[2];
        view.getLocationOnScreen(locationCoordinates);
        int adjustedYAxisLocation = adjustLocationToViewItemHeight(locationCoordinates[1], view);
        locationCoordinates[1] = adjustedYAxisLocation;
        return locationCoordinates;
    }

    private int adjustLocationToViewItemHeight(int yAxisLoc, View view) {
        return view.getHeight() + yAxisLoc;
    }

    private void showPopUpAtLocation(int[] locationCoordinates) {
        LayoutInflater inflater = mContext.getLayoutInflater();
        //TODO: refactor popup window look
        View todoItemPopupView = inflater.inflate(R.layout.todo_item_popup, null);
        PopupWindow todoItemPopup = new PopupWindow(todoItemPopupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        todoItemPopup.setFocusable(true);
        todoItemPopup.showAsDropDown(todoItemPopupView, locationCoordinates[0], locationCoordinates[1]);
    }
}
