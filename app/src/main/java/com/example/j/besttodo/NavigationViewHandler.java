package com.example.j.besttodo;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * Created by J on 1/4/2019.
 */
class NavigationViewHandler {

    private final NavigationView mNavigationView;
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private final ActionBar mActionBar;
    private DrawerLayout mDrawerLayout;
    private TodoListFragmentHandler mTodoListFragmentHandler;

    NavigationViewHandler(Activity context, TodoListFragmentHandler todoListFragmentHandler, ActionBar actionBar) {
        mContext = context;
        mNavigationView = context.findViewById(R.id.nav_view);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTodoListFragmentHandler = todoListFragmentHandler;
        mDrawerLayout = context.findViewById(R.id.drawer_layout);
        mActionBar = actionBar;
    }

    void iniateNavigationView() {
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        menuItem.setChecked(true);
                        if (menuItem.getGroupId() == R.id.group_todo_list_items) {
                            String todoListName = (String) menuItem.getTitle();
                            View popupView = mLayoutInflater.inflate(R.layout.todo_list_popup, null);
                            PopUpProvider.providePopUpWindowOnItemLocation(popupView);
                            addListenerToTodoListActionsPopupWindow(popupView, todoListName);
                        } else if (menuItem.getItemId() == R.id.add_new_todo_list) {
                            View popupView = mLayoutInflater.inflate(R.layout.text_input_popup, null);
                            PopupWindow textInputPopupWindow = PopUpProvider.providePopUpWindowOnViewAtCenter(popupView);
                            PopUpProvider.dimBackgroundOfPopup(textInputPopupWindow, mContext);
                            addListenerToNewTodoListNamingPopupWindow(popupView, textInputPopupWindow);
                        }
                        return true;
                    }
                }
        );
    }

    private void addListenerToTodoListActionsPopupWindow(View popupView, final String todoListName) {
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
                View popupView = mLayoutInflater.inflate(R.layout.text_input_popup, null);
                PopupWindow textInputPopupWindow = PopUpProvider.providePopUpWindowOnViewAtCenter(popupView);
                PopUpProvider.dimBackgroundOfPopup(textInputPopupWindow, mContext);
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
                    ToastProvider.showToastAtCenterOfScreen("TODO List with set name already exists!", mContext);
                }
            }
        });
    }

    private void renameTodoListOnNavigationView(String oldTodoListName, String newTodoListName) {
        removeTodoListFromNavigationView(oldTodoListName);
        addTodoListToNavigationView(newTodoListName);
    }

    void addNewTodoListFragment(String todoListName) {
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

    private void renameTodoList(String oldTodoListName, String newTodoListName) {
        mTodoListFragmentHandler.renameFragment(oldTodoListName, newTodoListName);
    }

    private void setCurrentTodoList(String todoListName) {
        setVisibleFragment(todoListName);
        mActionBar.setTitle(todoListName);
    }

    void setVisibleFragment(String todoListFragmentName) {
        mTodoListFragmentHandler.setVisibleFragment(todoListFragmentName);
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
                    ToastProvider.showToastAtCenterOfScreen("TODO List with set name already exists!", mContext);
                }
            }
        });
    }

    private boolean isTodoListNameUnique(String listNameToCheck) {
        return mTodoListFragmentHandler.doesFragmentExistWithName(listNameToCheck);
    }

    void openNavigationDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }
}
