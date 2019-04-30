package com.example.j.besttodo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.support.design.widget.FloatingActionButton;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.example.j.besttodo.util.ui.NavigationViewHandler;
import com.example.j.besttodo.util.ui.TodoListFragmentsHandler;
import com.example.j.besttodo.util.ui.storage.TodoListPersistentStorageHandler;

class MainActivity extends AppCompatActivity {

    private static final String INIT_TODO_LIST_NAME = "TODO list";
    private static final String MY_SHARED_PREFS_NAME = "todo_app_prefs";

    private ActionBar actionBar;
    private NavigationViewHandler navigationViewHandler;
    private TodoListPersistentStorageHandler storageHandler;
    private TodoListFragmentsHandler todoListFragmentsHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null) {
            initiateTodoListFragmentHandler();
            initiateViewComponents();
            initiatePersistentStorageHandler();
            storageHandler.loadTodoItemListsFromPrefs();
        }
        else if(todoListFragmentsHandler.getTodoListFragments().size() == 0) {
            addDefaultTodoList();
        }
        initiateToDoItemAdderButton();
    }

    private void initiateTodoListFragmentHandler() {
        this.todoListFragmentsHandler = new TodoListFragmentsHandler(getFragmentManager());
    }

    private void initiateViewComponents() {
        initiateBaseFocusHolder();
        initiateCustomToolbar();
        initiateNavigationMenu();
    }

    @ Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        storageHandler.saveTodoListsToSharedPreferences();
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigationViewHandler.openNavigationDrawer();
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

    private void initiateNavigationMenu() {
        navigationViewHandler = new NavigationViewHandler(this, todoListFragmentsHandler, actionBar);
        navigationViewHandler.initiateNavigationView();
    }

    private void initiateCustomToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
    }

    private void initiatePersistentStorageHandler() {
        SharedPreferences sharedpreferences = getSharedPreferences(MY_SHARED_PREFS_NAME, MODE_PRIVATE);
        this.storageHandler = new TodoListPersistentStorageHandler(
                this,
                sharedpreferences,
                actionBar,
                navigationViewHandler,
                todoListFragmentsHandler);
    }

    private void addDefaultTodoList() {
        navigationViewHandler.addNewTodoListFragment(INIT_TODO_LIST_NAME);
        todoListFragmentsHandler.setVisibleFragment(INIT_TODO_LIST_NAME);
        actionBar.setTitle(INIT_TODO_LIST_NAME);
    }

    private void initiateToDoItemAdderButton() {
        FloatingActionButton floatingActionButton = findViewById(R.id.addNewTodoItemButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodoListFragment currentVisibleTodoList = todoListFragmentsHandler.getVisibleFragment();
                TodoItem item = new TodoItem();
                item.setText(getResources().getString(R.string.todoItemText));
                item.setSchedule(getResources().getString(R.string.todoItemSchedulePlaceholder));
                currentVisibleTodoList.addTodoItem(item);
            }
        });
    }
}
