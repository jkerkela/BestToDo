package com.example.j.besttodo;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

public class TodoListAdapter extends ArrayAdapter<TodoItem> {

    private final List<TodoItem> mTodoItemList;
    private Activity mContext;
    private int mTodoItemLayout;
    private LayoutInflater layoutInflater;

    TodoListAdapter(@NonNull Activity context, int todoItemLayout, @NonNull List<TodoItem> todoItemList) {
        super(context, todoItemLayout, todoItemList);
        mContext = context;
        mTodoItemLayout = todoItemLayout;
        mTodoItemList = todoItemList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View todoItemLayout, @NonNull ViewGroup parent) {

        TodoItem todoItem = getItem(position);
        //TODO: the positioning is not correct here, may be related that todo item is final when setting listeners
        todoItem.setPosition(position);
        if(todoItemLayout == null) {
            layoutInflater = mContext.getLayoutInflater();
            todoItemLayout = layoutInflater.inflate(mTodoItemLayout, parent, false);
            addListenerToTodoItemActionButtonOnLayoutWithPosition(todoItemLayout, todoItem);
        }

        return todoItemLayout;
    }

    private void addListenerToTodoItemActionButtonOnLayoutWithPosition(final View todoItemLayout, final TodoItem todoItem) {
        ImageButton todoItemActionButton = todoItemLayout.findViewById(R.id.todoItemActionButton);
        todoItemActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] locationCoordinates = locatePosition(view);
                showPopUpAtLocationInLayout(locationCoordinates, todoItem);
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

    private void showPopUpAtLocationInLayout(int[] locationCoordinates, TodoItem todoItem) {
        //TODO: add "remove item" button to popup
        View todoItemPopupView = layoutInflater.inflate(R.layout.todo_item_popup, null);
        PopupWindow todoItemPopup = new PopupWindow(todoItemPopupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        todoItemPopup.setFocusable(true);
        todoItemPopup.showAsDropDown(todoItemPopupView, locationCoordinates[0], locationCoordinates[1]);
        addListenerToRemoveTodoItemButtonOnPopupWindow(todoItemPopupView, todoItem);
    }

    private void addListenerToRemoveTodoItemButtonOnPopupWindow(View todoItemPopupView, final TodoItem todoItem) {
        TextView placeholder = todoItemPopupView.findViewById(R.id.TodoItemRemoveButtonPlaceholder);
        placeholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTodoItemList.remove(todoItem.getPosition());
                notifyDataSetChanged();
            }
        });
    }

}
