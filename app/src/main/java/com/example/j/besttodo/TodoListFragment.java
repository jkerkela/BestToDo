package com.example.j.besttodo;

import android.app.ListFragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class TodoListFragment extends ListFragment {

    List<TodoItem> todoItemsList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayAdapter<TodoItem> todoListAdapter = new TodoListAdapter(getActivity(), R.layout.todo_item, todoItemsList);
        setListAdapter(todoListAdapter);
    }

    public List<TodoItem> getTodoList(){
        return todoItemsList;
    }
}