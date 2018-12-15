package com.example.j.besttodo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.design.widget.FloatingActionButton;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private String defaultFragmentName = "TODO list";
    private TodoListFragment mListFragment = new TodoListFragment();
    private DrawerLayout mDrawerLayout;
    private final FragmentManager mFragmentManager = getFragmentManager();
    TodoListFragmentHolder mTodoListFragmentHolder = new TodoListFragmentHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            addNewTodoListFragment(mListFragment, defaultFragmentName);
            mTodoListFragmentHolder.addFragment(mListFragment);
        }

        initiateSideMenu();
        initiateBaseFocusHolder();
        initiateToDoItemAdderButton();
    }

    private void initiateSideMenu() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        // TODO: Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        String todoListName = (String) menuItem.getTitle();
                        TodoListFragment todoListFragment = mTodoListFragmentHolder.getFragmentByName(todoListName);
                        setCurrentTodoListFragment(todoListFragment);
                        return true;
                    }
                }
        );
        intiateSideMenuTodoList(navigationView, defaultFragmentName);
        initiateCustomToolbar(defaultFragmentName);
    }

    private void intiateSideMenuTodoList(NavigationView navigationView, String todoListName) {
        Menu navigationViewMenu = navigationView.getMenu();
        navigationViewMenu.add(R.id.group_todo_list_items ,Menu.NONE,Menu.NONE, todoListName);
    }

    private void initiateCustomToolbar(String todoListName) {
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setTitle(todoListName);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initiateBaseFocusHolder() {
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

    private void initiateToDoItemAdderButton() {
        FloatingActionButton floatingActionButton = findViewById(R.id.addNewTodoItemButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewTodoItemToTodoItemToList(mListFragment);
            }
        });
    }

    private void addNewTodoListFragment(TodoListFragment listFragment, String fragmentName) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction
                .add(R.id.listFragmentContainer, listFragment)
                .commit();
        listFragment.setName(fragmentName);
    }

    private void setCurrentTodoListFragment(TodoListFragment listFragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction
                .show(listFragment)
                .commit();
    }

    private void addNewTodoItemToTodoItemToList(TodoListFragment todoListFragment){
        todoListFragment.addNewTodoItem();
    }

}
