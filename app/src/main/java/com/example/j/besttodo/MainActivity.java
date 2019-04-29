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

import com.example.j.besttodo.util.ui.ViewHandler;
import com.example.j.besttodo.util.ui.storage.TodoListStorageHandler;

class MainActivity extends AppCompatActivity {

    private static final String INIT_TODO_LIST_NAME = "TODO list";
    private static final String MY_SHARED_PREFS_NAME = "todo_app_prefs";

    private ActionBar mSupportActionBar;
    private ViewHandler mViewHandler;
    private TodoListStorageHandler storageHandler;

    TodoListFragmentsHandler mTodoListFragmentsHandler = new TodoListFragmentsHandler(getFragmentManager());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            initiateBaseFocusHolder();
            initiateCustomToolbar();
            initiateNavigationMenu();
            initiateViewStorageHandler();
            storageHandler.loadTodoItemListsFromPrefs();
        }
        else if (mTodoListFragmentsHandler.getTodoListFragments().size() == 0) {
            addDefaultTodoList();
        }
        initiateToDoItemAdderButton();
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
                mViewHandler.openNavigationDrawer();
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
        mViewHandler = new ViewHandler(this, mTodoListFragmentsHandler, mSupportActionBar);
        mViewHandler.initiateNavigationView();
    }

    private void initiateCustomToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        mSupportActionBar = getSupportActionBar();
        if (mSupportActionBar != null) {
            mSupportActionBar.setDisplayHomeAsUpEnabled(true);
            mSupportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
    }

    private void initiateViewStorageHandler() {
        SharedPreferences sharedpreferences = getSharedPreferences(MY_SHARED_PREFS_NAME, MODE_PRIVATE);
        this.storageHandler = new TodoListStorageHandler(
                sharedpreferences,
                mSupportActionBar,
                mViewHandler,
                mTodoListFragmentsHandler);
    }

    private void addDefaultTodoList() {
        mViewHandler.addNewTodoListFragment(INIT_TODO_LIST_NAME);
        mViewHandler.setVisibleFragmentByName(INIT_TODO_LIST_NAME);
        mSupportActionBar.setTitle(INIT_TODO_LIST_NAME);
    }

    private void initiateToDoItemAdderButton() {
        FloatingActionButton floatingActionButton = findViewById(R.id.addNewTodoItemButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodoListFragment currentVisibleTodoList = mTodoListFragmentsHandler.getVisibleTodoList();
                TodoItem item = new TodoItem();
                item.setText(getResources().getString(R.string.todoItemText));
                item.setSchedule(getResources().getString(R.string.todoItemSchedulePlaceholder));
                currentVisibleTodoList.addTodoItem(item);
            }
        });
    }
}
