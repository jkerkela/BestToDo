package com.example.j.besttodo;

import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.FloatingActionButton;
import android.widget.ArrayAdapter;

import java.util.List;

public class MainActivity extends FragmentActivity {

    TodoListFragment mListFragment = new TodoListFragment();
    FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton floatingActionButton = findViewById(R.id.addNewTodoItemButton);

        if (savedInstanceState == null) {
            addFragment(mListFragment);
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<TodoItem> todoList = mListFragment.getTodoList();
                addNewTodoItemToTodoItemToList(todoList);
                updateFragmentView(mListFragment);
            }
        });
    }

    private void addFragment(TodoListFragment listFragment) {
        mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.add(R.id.listFragment, listFragment).commit();
    }

    private void addNewTodoItemToTodoItemToList(List<TodoItem> currentTodoList){
        TodoItem todoItem = new TodoItem(getString(R.string.todoItemText));
        currentTodoList.add(todoItem);
    }

    private void updateFragmentView(TodoListFragment listFragment){
        ArrayAdapter mlistAdapter = (ArrayAdapter) listFragment.getListAdapter();
        mlistAdapter.notifyDataSetChanged();
    }

}
