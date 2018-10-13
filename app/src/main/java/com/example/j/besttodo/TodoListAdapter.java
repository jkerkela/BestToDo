package com.example.j.besttodo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.util.List;

public class TodoListAdapter extends ArrayAdapter<TodoItem> {

    private Context mContext;

    TodoListAdapter(@NonNull Context context, int resource, @NonNull List<TodoItem> todoItemList) {
        super(context, resource, todoItemList);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;

        if(listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.todo_item, parent, false);
        }

        TodoItem todoItem = getItem(position);

        if (todoItem != null) {
            EditText todoItemDescription = listItem.findViewById(R.id.todoItemDescription);
            todoItemDescription.setText(todoItem.getDescription());
        }

        return listItem;
    }
}
