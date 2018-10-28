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
                showPopUp();
            }
        });
    }

    private void showPopUp() {
        LayoutInflater inflater = mContext.getLayoutInflater();
        View todoItemPopupView = inflater.inflate(R.layout.todo_item_popup, null);
        PopupWindow todoItemPopup = new PopupWindow(todoItemPopupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        //TODO: needs to be shows at click location
        todoItemPopup.showAtLocation(todoItemPopupView, Gravity.CENTER,0,0);
    }
}
