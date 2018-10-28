package com.example.j.besttodo;

import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.support.design.widget.FloatingActionButton;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    TodoListFragment mListFragment = new TodoListFragment();
    FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitiateBaseFocusHolder();
        InitiateToDoItemAdderButton();

        if (savedInstanceState == null) {
            addFragment(mListFragment);
        }
    }

    private void InitiateBaseFocusHolder() {
        final RelativeLayout baseLayout = findViewById(R.id.baseLayout);
        baseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseLayout.requestFocus();
                closeSoftKeyboard();
            }
        });
    }

    private void closeSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        View rootView = findViewById(android.R.id.content);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
        }
    }

    private void InitiateToDoItemAdderButton() {
        FloatingActionButton floatingActionButton = findViewById(R.id.addNewTodoItemButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewTodoItemToTodoItemToList(mListFragment);
                updateFragmentView(mListFragment);
            }
        });
    }

    private void addFragment(TodoListFragment listFragment) {
        mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.add(R.id.listFragmentContainer, listFragment).commit();
    }

    private void addNewTodoItemToTodoItemToList(TodoListFragment currentTodoList){
        currentTodoList.addNewTodoItem();
    }

    private void updateFragmentView(TodoListFragment listFragment){
        ArrayAdapter mlistAdapter = (ArrayAdapter) listFragment.getListAdapter();
        mlistAdapter.notifyDataSetChanged();
    }

}
