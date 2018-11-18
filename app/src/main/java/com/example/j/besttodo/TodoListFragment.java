package com.example.j.besttodo;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class TodoListFragment extends Fragment {

    private List<TodoItem> mTodoItemsList = new ArrayList<>();
    private RecyclerView recyclerView;
    TodoListAdapter todoListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = getView().findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        todoListAdapter = new TodoListAdapter(mTodoItemsList, getActivity());
        recyclerView.setAdapter(todoListAdapter);
    }

    public void addNewTodoItem() {
        TodoItem todoItem = new TodoItem();
        mTodoItemsList.add(todoItem);
        todoListAdapter.notifyItemInserted(mTodoItemsList.size() - 1);
        todoListAdapter.notifyDataSetChanged();
    }

}
