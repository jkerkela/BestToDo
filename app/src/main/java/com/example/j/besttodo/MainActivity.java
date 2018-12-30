package com.example.j.besttodo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
            currentVisibleTodoList = mTodoListFragmentHolder.getFragmentByNameOrNull(mInitTodoListFragmentName);
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
        final TodoListFragment todoListFragment = mTodoListFragmentHolder.getFragmentByNameOrNull(todoListName);
        TextView setTodoListAsCurrentButton = popupView.findViewById(R.id.SetCurrentTodoList);
        setTodoListAsCurrentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCurrentTodoListFragment(todoListFragment);
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
                removeTodoListFragment(todoListFragment, todoListName);
            }
        });
    }

    private void removeTodoListFragment(TodoListFragment todoListFragment, String todoListName) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction
                .remove(todoListFragment)
                .commit();
        mTodoListFragmentHolder.removeFragment(todoListName);
        removeTodoListFromNavigationView(todoListFragment.getId());
    }

    private void removeTodoListFromNavigationView(int todoListIdentifier) {
        Menu navigationViewMenu = mNavigationView.getMenu();
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

    private void renameTodoListOnNavigationView(String oldTodoListName,String newTodoListName) {
        //TODO: this renames sometimes wrong list
        Menu navigationViewMenu = mNavigationView.getMenu();
        TodoListFragment todoListToRename = mTodoListFragmentHolder.getFragmentByNameOrNull(oldTodoListName);
        int todoListIdentifier = todoListToRename.getId();
        navigationViewMenu.removeItem(todoListIdentifier);
        navigationViewMenu.add(R.id.group_todo_list_items , todoListIdentifier, Menu.NONE, newTodoListName);
    }

    private void renameTodoList(String oldTodoListName, String newTodoListName) {
        mTodoListFragmentHolder.renameFragment(oldTodoListName, newTodoListName);
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

    private boolean isTodoListNameUnique(String inputText) {
        TodoListFragment fragmentIfExists = mTodoListFragmentHolder.getFragmentByNameOrNull(inputText);
        return fragmentIfExists == null;
    }

    private void addNewTodoListFragment(String todoListName) {
        TodoListFragment listFragment = new TodoListFragment();
        listFragment.setName(todoListName);
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction
                .add(R.id.listFragmentContainer, listFragment)
                .commit();
        mTodoListFragmentHolder.addFragment(listFragment);
        addTodoListToNavigationView(todoListName, listFragment.getId());
    }

    private void addTodoListToNavigationView(String todoListName, int todoListIdentifier) {
        Menu navigationViewMenu = mNavigationView.getMenu();
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
