package com.example.j.besttodo.util.ui;

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

import com.example.j.besttodo.R;
import com.example.j.besttodo.TodoListFragment;
import com.example.j.besttodo.TodoListFragmentHandler;

public class ViewHandler {

    private final NavigationView mNavigationView;
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private final ActionBar mActionBar;
    private DrawerLayout mDrawerLayout;
    private TodoListFragmentHandler mTodoListFragmentHandler;

    public ViewHandler(Activity context, TodoListFragmentHandler todoListFragmentHandler, ActionBar actionBar) {
        mContext = context;
        mNavigationView = context.findViewById(R.id.nav_view);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTodoListFragmentHandler = todoListFragmentHandler;
        mDrawerLayout = context.findViewById(R.id.drawer_layout);
        mActionBar = actionBar;
    }

    public void initiateNavigationView() {
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        if (menuItem.getGroupId() == R.id.group_todo_list_items) {
                            String todoListName = (String) menuItem.getTitle();
                            View popupView = mLayoutInflater.inflate(R.layout.todo_list_popup, null);
                            PopupWindow todoListActionPopupWindow = PopUpProvider.providePopUpWindowOnItemLocation(popupView);
                            addListenerToTodoListActionsPopupWindow(popupView, todoListActionPopupWindow, todoListName);
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

    private void addListenerToTodoListActionsPopupWindow(View popupView, final PopupWindow todoListActionPopupWindow, final String todoListName) {
        ImageButton setTodoListAsCurrentButton = popupView.findViewById(R.id.SetCurrentTodoListButton);
        setTodoListAsCurrentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todoListActionPopupWindow.dismiss();
                setCurrentTodoList(todoListName);
                mDrawerLayout.closeDrawers();
            }
        });
        ImageButton renameTodoListButton = popupView.findViewById(R.id.RenameTodoListButton);
        renameTodoListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todoListActionPopupWindow.dismiss();
                View popupView = mLayoutInflater.inflate(R.layout.text_input_popup, null);
                PopupWindow textInputPopupWindow = PopUpProvider.providePopUpWindowOnViewAtCenter(popupView);
                PopUpProvider.dimBackgroundOfPopup(textInputPopupWindow, mContext);
                addListenerToRenameTodoListNamingPopupWindow(popupView, textInputPopupWindow, todoListName);
            }
        });
        ImageButton removeTodoListButton = popupView.findViewById(R.id.RemoveTodoListButton);
        removeTodoListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todoListActionPopupWindow.dismiss();
                removeTodoListFragment(todoListName);
            }
        });
    }

    private void setCurrentTodoList(String todoListName) {
        setVisibleFragment(todoListName);
        mActionBar.setTitle(todoListName);
    }

    public void setVisibleFragment(String todoListFragmentName) {
        mTodoListFragmentHandler.setVisibleFragment(todoListFragmentName);
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

    private void addTodoListToNavigationView(String todoListName) {
        Menu navigationViewMenu = mNavigationView.getMenu();
        int todoListIdentifier = todoListName.hashCode();
        navigationViewMenu.add(R.id.group_todo_list_items , todoListIdentifier, Menu.NONE, todoListName);
    }

    public void addNewTodoListFragment(String todoListName) {
        TodoListFragment listFragment = new TodoListFragment();
        listFragment.setName(todoListName);
        mTodoListFragmentHandler.addFragment(listFragment);
        addTodoListToNavigationView(todoListName);
    }

    private void renameTodoList(String oldTodoListName, String newTodoListName) {
        mTodoListFragmentHandler.renameFragment(oldTodoListName, newTodoListName);
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

    public void openNavigationDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }
}
