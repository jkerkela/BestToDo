package com.example.j.besttodo;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.util.List;

public class TodoListAdapter extends ArrayAdapter<TodoItem> {

    private Activity mContext;
    private int mResourceLayout;

    TodoListAdapter(@NonNull Activity context, int resourceLayout, @NonNull List<TodoItem> todoItemList) {
        super(context, resourceLayout, todoItemList);
        mContext = context;
        mResourceLayout = resourceLayout;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;

        if(listItem == null) {
            LayoutInflater layoutInflater = mContext.getLayoutInflater();
            listItem = layoutInflater.inflate(mResourceLayout, parent, false);
        }

        TodoItem todoItem = getItem(position);
        if (todoItem != null) {
            EditText todoItemDescription = listItem.findViewById(R.id.todoItemDescription);
            todoItemDescription.setText(todoItem.getDescription());
        }

        return listItem;
    }
}
