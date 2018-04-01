package com.example.j.besttodo;

import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.support.design.widget.FloatingActionButton;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

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
                ArrayAdapter todoListAdapter = (ArrayAdapter) listFragment.getListAdapter();
                List<String> currentTodoList = listFragment.getTodoList();
                //TODO: add new todo item with default text to fragment
                currentTodoList.add("testADD");
                todoListAdapter.notifyDataSetChanged();
            }
        });
    }

}
