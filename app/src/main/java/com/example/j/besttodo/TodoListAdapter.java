package com.example.j.besttodo;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class TodoListAdapter extends ArrayAdapter<TodoItemFragment> {

    private Activity mContext;
    private int mResourceLayout;

    TodoListAdapter(@NonNull Activity context, int resourceLayout, @NonNull List<TodoItemFragment> todoItemList) {
        super(context, resourceLayout, todoItemList);
        mContext = context;
        mResourceLayout = resourceLayout;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listView = convertView;

        if(listView == null) {
            LayoutInflater layoutInflater = mContext.getLayoutInflater();
            listView = layoutInflater.inflate(mResourceLayout, parent, false);
        }

        return listView;
    }

}
