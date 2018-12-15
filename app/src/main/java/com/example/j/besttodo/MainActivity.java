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

    private DrawerLayout mDrawerLayout;
    private final FragmentManager mFragmentManager = getFragmentManager();
    TodoListFragmentHolder mTodoListFragmentHolder = new TodoListFragmentHolder();
    private TodoListFragment currentVisibleTodoList;
    private NavigationView mNavigationView;
    private ActionBar mSupportActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String mInitTodoListFragmentName = "TODO list";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            initiateBaseFocusHolder();
            initiateSideMenu(mInitTodoListFragmentName);
            initiateInitialTodoList(mInitTodoListFragmentName);
            currentVisibleTodoList = mTodoListFragmentHolder.getFragmentByName(mInitTodoListFragmentName);
        }
        initiateToDoItemAdderButton();
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

    private void initiateSideMenu(String todoListName) {
        initiateCustomToolbar(todoListName);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        if (menuItem.getGroupId() == R.id.group_todo_list_items) {
                            String todoListName = (String) menuItem.getTitle();
                            TodoListFragment todoListFragment = mTodoListFragmentHolder.getFragmentByName(todoListName);
                            setCurrentTodoListFragment(todoListFragment);
                        } else if (menuItem.getItemId() == R.id.add_new_todo_list) {
                            //TODO: launch pop-up window asking for list name
                            String mockListName = "TEST";
                            addNewTodoListFragment(mockListName);
                        }
                        return true;
                    }
                }
        );
    }

    private void initiateCustomToolbar(String todoListName) {
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        mSupportActionBar = getSupportActionBar();
        if (mSupportActionBar != null) {
            mSupportActionBar.setDisplayHomeAsUpEnabled(true);
            mSupportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            mSupportActionBar.setTitle(todoListName);
        }
    }

    private void addNewTodoListFragment(String todoListName) {
        TodoListFragment listFragment = new TodoListFragment();
        listFragment.setName(todoListName);
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction
                .add(R.id.listFragmentContainer, listFragment)
                .commit();
        mTodoListFragmentHolder.addFragment(listFragment);
        addTodoListToNavigationView(todoListName);
    }

    private void addTodoListToNavigationView(String todoListName) {
        Menu navigationViewMenu = mNavigationView.getMenu();
        navigationViewMenu.add(R.id.group_todo_list_items ,Menu.NONE,Menu.NONE, todoListName);
    }

    private void initiateInitialTodoList(String mInitTodoListFragmentName) {
        addNewTodoListFragment(mInitTodoListFragmentName);
    }

    private void initiateToDoItemAdderButton() {
        FloatingActionButton floatingActionButton = findViewById(R.id.addNewTodoItemButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewTodoItemToTodoItemToList(currentVisibleTodoList);
            }
        });
    }

    private void addNewTodoItemToTodoItemToList(TodoListFragment todoListFragment){
        todoListFragment.addNewTodoItem();
    }

    private void setCurrentTodoListFragment(TodoListFragment listFragment) {
        changeVisibleFragmentTo(listFragment);
        String fragmentName = listFragment.getName();
        mSupportActionBar.setTitle(fragmentName);
        currentVisibleTodoList = listFragment;
    }

    private void changeVisibleFragmentTo(TodoListFragment todoListFragment) {
        hideCurrentFragment();
        setFragmentVisible(todoListFragment);
    }

    private void hideCurrentFragment() {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction
                .hide(currentVisibleTodoList)
                .commit();
    }

    private void setFragmentVisible(TodoListFragment todoListFragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction
                .show(todoListFragment)
                .commit();
    }

}
