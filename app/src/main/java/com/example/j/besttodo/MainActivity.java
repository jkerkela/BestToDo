package com.example.j.besttodo;

import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.design.widget.FloatingActionButton;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private TodoListFragment mListFragment = new TodoListFragment();
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private ActionBar supportActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: https://code.tutsplus.com/tutorials/how-to-code-a-navigation-drawer-in-an-android-app--cms-30263
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeButtonEnabled(true);

        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        InitiateBaseFocusHolder();
        InitiateToDoItemAdderButton();

        if (savedInstanceState == null) {
            InitiateTodoListFragment(mListFragment);
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
            }
        });
    }

    private void InitiateTodoListFragment(TodoListFragment listFragment) {
        FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.add(R.id.listFragmentContainer, listFragment).commit();
    }

    private void addNewTodoItemToTodoItemToList(TodoListFragment todoListFragment){
        todoListFragment.addNewTodoItem();
    }

}
