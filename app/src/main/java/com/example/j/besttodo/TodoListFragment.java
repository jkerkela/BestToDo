package com.example.j.besttodo;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
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
import java.util.Objects;

public class TodoListFragment extends Fragment {

    private List<TodoItem> todoItems = new ArrayList<>();
    private TodoListAdapter todoListAdapter;
    private String name;
    private List<TodoItem> pendingTodoItems = new ArrayList<>();
    private Drawable menuIcon;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView todoListView = Objects.requireNonNull(getView()).findViewById(R.id.todo_list_view);
        attachAdapter(todoListView);
        addPendingTodoItemsToList();
    }

    private void attachAdapter(RecyclerView todoListView) {
        todoListAdapter = new TodoListAdapter(todoItems, getActivity());
        todoListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        todoListView.addItemDecoration(itemDecorator);
        todoListView.setItemAnimator(new DefaultItemAnimator());
        todoListView.setAdapter(todoListAdapter);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(todoListAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(todoListView);
    }

    private void addPendingTodoItemsToList() {
        for(TodoItem item: pendingTodoItems) {
            addTodoItem(item);
        }
    }

    public void addTodoItem(TodoItem item) {
            if(todoListAdapter != null) {
                todoItems.add(item);
                todoListAdapter.notifyItemInserted(todoItems.size() - 1);
                todoListAdapter.notifyDataSetChanged();
            } else {
                addPendingTodoItem(item);
            }
    }

    private void addPendingTodoItem(TodoItem item) {
        pendingTodoItems.add(item);
    }

    public void setName(String name) { this.name = name; }

    public String getName() { return name; }

    public List<TodoItem> getTodoItems() { return todoItems; }

    public void setMenuIcon(Drawable menuIcon) { this.menuIcon = menuIcon; }

    public Drawable getMenuIcon() { return menuIcon; }
}
