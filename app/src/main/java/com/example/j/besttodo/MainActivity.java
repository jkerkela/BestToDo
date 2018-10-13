package com.example.j.besttodo;

import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.FloatingActionButton;
import android.widget.ArrayAdapter;

import java.util.List;

public class MainActivity extends FragmentActivity {

    TodoListFragment listFragment;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton floatingActionButton = findViewById(R.id.addNewTodoItemButton);

        if (savedInstanceState == null) {
            listFragment = new TodoListFragment();
            fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.listFragment, listFragment).commit();
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewTodoItemToTodoItem(listFragment.getTodoList());
                ArrayAdapter listAdapter = (ArrayAdapter) listFragment.getListAdapter();
                notifyTodoListAdapter(listAdapter);
            }
        });
    }

    private void addNewTodoItemToTodoItem(List<TodoItem> currentTodoList){
        TodoItem todoItem = new TodoItem(getString(R.string.todoItemText));
        currentTodoList.add(todoItem);
    }

    private void notifyTodoListAdapter(ArrayAdapter todoListAdapter){
        todoListAdapter.notifyDataSetChanged();
    }

}
