package com.example.j.besttodo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    TodoListFragmentHandler mTodoListFragmentHandler = new TodoListFragmentHandler(getFragmentManager());
    private NavigationView mNavigationView;
    private ActionBar mSupportActionBar;

    //TODO: split classes here for Fragment operations, nav view ui, listeners(?)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String mInitTodoListFragmentName = "TODO list";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            initiateBaseFocusHolder();
            initiateSideMenu(mInitTodoListFragmentName);
            initiateInitialTodoList(mInitTodoListFragmentName);
            setVisibleFragment(mInitTodoListFragmentName);
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
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        menuItem.setChecked(true);
                        if (menuItem.getGroupId() == R.id.group_todo_list_items) {
                            String todoListName = (String) menuItem.getTitle();
                            View popupView = getLayoutInflater().inflate(R.layout.todo_list_popup, null);
                            PopupWindow todoListActionsPopupWindow = PopUpProvider.providePopUpWindowOnItemLocation(popupView);
                            addListenerToTodoListActionsPopupWindow(popupView, todoListActionsPopupWindow, todoListName);
                        } else if (menuItem.getItemId() == R.id.add_new_todo_list) {
                            View popupView = getLayoutInflater().inflate(R.layout.text_input_popup, null);
                            PopupWindow textInputPopupWindow = PopUpProvider.providePopUpWindowOnViewAtCenter(popupView);
                            PopUpProvider.dimBackgroundOfPopup(textInputPopupWindow, getApplicationContext());
                            addListenerToNewTodoListNamingPopupWindow(popupView, textInputPopupWindow);
                        }
                        return true;
                    }
                }
        );
    }

    private void addListenerToTodoListActionsPopupWindow(View popupView, PopupWindow todoListActionsPopupWindow, final String todoListName) {
        TextView setTodoListAsCurrentButton = popupView.findViewById(R.id.SetCurrentTodoList);
        setTodoListAsCurrentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCurrentTodoList(todoListName);
                mDrawerLayout.closeDrawers();
            }
        });
        TextView renameTodoListButton = popupView.findViewById(R.id.RenameTodoList);
        renameTodoListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View popupView = getLayoutInflater().inflate(R.layout.text_input_popup, null);
                PopupWindow textInputPopupWindow = PopUpProvider.providePopUpWindowOnViewAtCenter(popupView);
                PopUpProvider.dimBackgroundOfPopup(textInputPopupWindow, getApplicationContext());
                addListenerToRenameTodoListNamingPopupWindow(popupView, textInputPopupWindow, todoListName);
            }
        });
        TextView removeTodoListButton = popupView.findViewById(R.id.RemoveTodoList);
        removeTodoListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeTodoListFragment(todoListName);
            }
        });
    }

    private void removeTodoListFragment(String todoListName) {
        removeTodoListFromNavigationView(todoListName);
        mTodoListFragmentHandler.removeFragment(todoListName);
    }

    private void removeTodoListFromNavigationView(String todoListName) {
        Menu navigationViewMenu = mNavigationView.getMenu();
        int todoListIdentifier = todoListName.hashCode();
        navigationViewMenu.removeItem(todoListIdentifier);
    }

    private void addListenerToRenameTodoListNamingPopupWindow(View popupView, final PopupWindow textInputPopupWindow, final String todoListName) {
        ImageButton setTodoListNameButton = popupView.findViewById(R.id.setTodoListNameButton);
        final EditText todoListNameText = popupView.findViewById(R.id.todoListName);
        setTodoListNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String InputText = todoListNameText.getText().toString();
                if(isTodoListNameUnique(InputText)) {
                    renameTodoListOnNavigationView(todoListName, InputText);
                    renameTodoList(todoListName, InputText);
                    textInputPopupWindow.dismiss();
                } else {
                    ToastProvider.showToastAtCenterOfScreen("TODO List with set name already exists!", getApplicationContext());
                }
            }
        });
    }

    private void renameTodoListOnNavigationView(String oldTodoListName, String newTodoListName) {
        removeTodoListFromNavigationView(oldTodoListName);
        addTodoListToNavigationView(newTodoListName);
    }

    private void renameTodoList(String oldTodoListName, String newTodoListName) {
        mTodoListFragmentHandler.renameFragment(oldTodoListName, newTodoListName);
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

    private void addListenerToNewTodoListNamingPopupWindow(View popupView, final PopupWindow textInputPopupWindow) {
        ImageButton setTodoListNameButton = popupView.findViewById(R.id.setTodoListNameButton);
        final EditText todoListNameText = popupView.findViewById(R.id.todoListName);
        setTodoListNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String InputText = todoListNameText.getText().toString();
                if(isTodoListNameUnique(InputText)) {
                    addNewTodoListFragment(InputText);
                    textInputPopupWindow.dismiss();
                } else {
                    ToastProvider.showToastAtCenterOfScreen("TODO List with set name already exists!", getApplicationContext());
                }
            }
        });
    }

    private boolean isTodoListNameUnique(String listNameToCheck) {
        return mTodoListFragmentHandler.doesFragmentExistWithName(listNameToCheck);
    }

    private void addNewTodoListFragment(String todoListName) {
        TodoListFragment listFragment = new TodoListFragment();
        listFragment.setName(todoListName);
        mTodoListFragmentHandler.addFragment(listFragment);
        addTodoListToNavigationView(todoListName);
    }

    private void addTodoListToNavigationView(String todoListName) {
        Menu navigationViewMenu = mNavigationView.getMenu();
        int todoListIdentifier = todoListName.hashCode();
        navigationViewMenu.add(R.id.group_todo_list_items , todoListIdentifier, Menu.NONE, todoListName);
    }

    private void initiateInitialTodoList(String mInitTodoListFragmentName) {
        addNewTodoListFragment(mInitTodoListFragmentName);
    }

    private void initiateToDoItemAdderButton() {
        FloatingActionButton floatingActionButton = findViewById(R.id.addNewTodoItemButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodoListFragment currentVisibleTodoList = mTodoListFragmentHandler.getCurrentVisibleTodoList();
                addNewTodoItemToTodoItemToList(currentVisibleTodoList);
            }
        });
    }

    private void addNewTodoItemToTodoItemToList(TodoListFragment todoListFragment){
        todoListFragment.addNewTodoItem();
    }

    private void setCurrentTodoList(String todoListName) {
        setVisibleFragment(todoListName);
        mSupportActionBar.setTitle(todoListName);
    }

    private void setVisibleFragment(String todoListFragmentName) {
        mTodoListFragmentHandler.setVisibleFragment(todoListFragmentName);
    }

}
