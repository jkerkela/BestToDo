package com.example.j.besttodo;

import android.content.Context;
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

class MainActivity extends AppCompatActivity {

    TodoListFragmentHandler mTodoListFragmentHandler = new TodoListFragmentHandler(getFragmentManager());
    private ActionBar mSupportActionBar;
    private ViewHandler mViewHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String mInitTodoListFragmentName = "TODO list";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            initiateBaseFocusHolder();
            initiateCustomToolbar(mInitTodoListFragmentName);
            initiateNavigationMenu(mInitTodoListFragmentName);
        }
        initiateToDoItemAdderButton();
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

    private void initiateNavigationMenu(String todoListName) {
        mViewHandler = new ViewHandler(this, mTodoListFragmentHandler, mSupportActionBar);
        mViewHandler.initiateNavigationView();
        mViewHandler.addNewTodoListFragment(todoListName);
        mViewHandler.setVisibleFragment(todoListName);
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

    private void initiateToDoItemAdderButton() {
        FloatingActionButton floatingActionButton = findViewById(R.id.addNewTodoItemButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodoListFragment currentVisibleTodoList = mTodoListFragmentHandler.getCurrentVisibleTodoList();
                currentVisibleTodoList.addNewTodoItem();
            }
        });
    }

}
