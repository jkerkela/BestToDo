package com.example.j.besttodo;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class TodoListFragment extends ListFragment {

    List<TodoItemFragment> mTodoItemsList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter<TodoItemFragment> todoListAdapter = new TodoListAdapter(getActivity(), R.layout.todo_item_fragment, mTodoItemsList);
        setListAdapter(todoListAdapter);
    }

    public void addNewTodoItem() {
        TodoItemFragment todoItemFragment = new TodoItemFragment();
        mTodoItemsList.add(todoItemFragment);
    }

}
