package com.example.j.besttodo;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import java.util.List;

public class TodoListAdapter extends ArrayAdapter<TodoItemFragment> {

    private Activity mContext;
    private int mTodoItemLayout;

    TodoListAdapter(@NonNull Activity context, int todoItemLayout, @NonNull List<TodoItemFragment> todoItemList) {
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
            //TODO: open poop-up window here
            }
        });
    }

}
