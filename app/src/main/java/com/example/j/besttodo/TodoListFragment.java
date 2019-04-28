package com.example.j.besttodo;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.j.besttodo.util.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

public class TodoListFragment extends Fragment {

    private List<TodoItem> mTodoItems = new ArrayList<>();
    private TodoListAdapter mTodoListAdapter;
    private String name;
    private List<TodoItem> pendingTodoItems = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView todoListView = getView().findViewById(R.id.todo_list_view);
        attachAdapter(todoListView);
        addPendingTodoItemsToList();
    }

    private void attachAdapter(RecyclerView todoListView) {
        mTodoListAdapter = new TodoListAdapter(mTodoItems, getActivity());
        todoListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        todoListView.addItemDecoration(itemDecorator);
        todoListView.setItemAnimator(new DefaultItemAnimator());
        todoListView.setAdapter(mTodoListAdapter);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mTodoListAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(todoListView);
    }

    private void addPendingTodoItemsToList() {
        for(TodoItem item: pendingTodoItems) {
            addTodoItem(item);
        }
    }

    public void addTodoItem(TodoItem item) {
            if (mTodoListAdapter != null) {
                mTodoItems.add(item);
                mTodoListAdapter.notifyItemInserted(mTodoItems.size() - 1);
                mTodoListAdapter.notifyDataSetChanged();
            } else {
                addPendingTodoItem(item);
            }
    }

    private void addPendingTodoItem(TodoItem item) {
        pendingTodoItems.add(item);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<TodoItem> getTodoItems() {
        return mTodoItems;
    }
}
