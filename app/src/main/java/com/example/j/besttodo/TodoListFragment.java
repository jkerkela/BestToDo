package com.example.j.besttodo;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class TodoListFragment extends Fragment {

    private List<TodoItem> mTodoItemsList = new ArrayList<>();
    TodoListAdapter mTodoListAdapter;
    private String name;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView todoListView = getView().findViewById(R.id.todo_list_view);
        todoListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        todoListView.setItemAnimator(new DefaultItemAnimator());
        mTodoListAdapter = new TodoListAdapter(mTodoItemsList, getActivity());
        todoListView.setAdapter(mTodoListAdapter);
    }

    public void addNewTodoItem() {
        TodoItem todoItem = new TodoItem();
        mTodoItemsList.add(todoItem);
        mTodoListAdapter.notifyItemInserted(mTodoItemsList.size() - 1);
        mTodoListAdapter.notifyDataSetChanged();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
