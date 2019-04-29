package com.example.j.besttodo.util.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.example.j.besttodo.TodoListFragmentsHandler;

import java.util.ArrayList;

public class ViewHandler {

    private final static String NON_UNIQUE_NAME_ERROR_TEXT = "TODO List with set name already exists!";
    private final NavigationView mNavigationView;
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private final ActionBar mActionBar;
    private DrawerLayout mDrawerLayout;
    private TodoListFragmentsHandler mTodoListFragmentsHandler;

    public ViewHandler(Activity context, TodoListFragmentsHandler todoListFragmentsHandler, ActionBar actionBar) {
        mContext = context;
        mNavigationView = context.findViewById(R.id.nav_view);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTodoListFragmentsHandler = todoListFragmentsHandler;
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

    public TodoListFragment addNewTodoListFragment(String todoListName) {
        TodoListFragment listFragment = new TodoListFragment();
        listFragment.setName(todoListName);
        mTodoListFragmentsHandler.addFragment(listFragment);
        addTodoListToNavigationView(todoListName);
        return listFragment;
    }

    public void setVisibleFragmentByName(String todoListFragmentName) {
        mTodoListFragmentsHandler.setVisibleFragment(todoListFragmentName);
    }

    public String getVisibleFragmentName(){
        return mTodoListFragmentsHandler.getVisibleFragment().getName();
    }
    public void openNavigationDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    private void addTodoListToNavigationView(String todoListName) {
        Menu navigationViewMenu = mNavigationView.getMenu();
        int todoListIdentifier = todoListName.hashCode();
        navigationViewMenu.add(R.id.group_todo_list_items,
                todoListIdentifier,
                Menu.NONE,
                todoListName);
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
                renameTodoList(todoListName);
            }
        });
        ImageButton setTodoListIconButton = popupView.findViewById(R.id.SetTodoListIconButton);
        setTodoListIconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todoListActionPopupWindow.dismiss();
                setTodoListIcon(todoListName);
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
        setVisibleFragmentByName(todoListName);
        mActionBar.setTitle(todoListName);
    }

    private void renameTodoList(String todoListName) {
        View popupView = mLayoutInflater.inflate(R.layout.text_input_popup, null);
        PopupWindow textInputPopupWindow = PopUpProvider.providePopUpWindowOnViewAtCenter(popupView);
        PopUpProvider.dimBackgroundOfPopup(textInputPopupWindow, mContext);
        addListenerToRenameTodoListNamingPopupWindow(popupView, textInputPopupWindow, todoListName);
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
                    renameTodoListFragment(todoListName, InputText);
                    textInputPopupWindow.dismiss();
                } else {
                    ToastProvider.showToastAtCenterOfScreen(NON_UNIQUE_NAME_ERROR_TEXT, mContext);
                }
            }
        });
    }

    private boolean isTodoListNameUnique(String listNameToCheck) {
        return mTodoListFragmentsHandler.doesFragmentExistWithName(listNameToCheck);
    }

    private void renameTodoListOnNavigationView(String oldTodoListName, String newTodoListName) {
        removeTodoListFromNavigationView(oldTodoListName);
        addTodoListToNavigationView(newTodoListName);
    }

    private void renameTodoListFragment(String oldTodoListName, String newTodoListName) {
        mTodoListFragmentsHandler.renameFragment(oldTodoListName, newTodoListName);
    }

    private void removeTodoListFragment(String todoListName) {
        removeTodoListFromNavigationView(todoListName);
        mTodoListFragmentsHandler.removeFragment(todoListName);
    }

    private void removeTodoListFromNavigationView(String todoListName) {
        Menu navigationViewMenu = mNavigationView.getMenu();
        int todoListIdentifier = todoListName.hashCode();
        navigationViewMenu.removeItem(todoListIdentifier);
    }

    private void setTodoListIcon(String todoListName) {
        View popupView = mLayoutInflater.inflate(R.layout.todo_list_icon_popup, null);
        final PopupWindow iconChangePopupWindow = PopUpProvider.providePopUpWindowOnViewAtCenter(popupView);
        PopUpProvider.dimBackgroundOfPopup(iconChangePopupWindow, mContext);
        ArrayList<View> iconButtons = popupView.getTouchables();
        addListenerToPopUpIconButtons(iconButtons, popupView, todoListName, iconChangePopupWindow);
    }

    private void addListenerToPopUpIconButtons(ArrayList<View> iconButtons,
                                               View popupView,
                                               final String todoListName,
                                               final PopupWindow iconChangePopupWindow) {
        for (int i = 0; i < iconButtons.size(); i++) {
            int viewId = iconButtons.get(i).getId();
            ImageButton setTodoListNameButton = popupView.findViewById(viewId);
            final Drawable todoIconToSet = setTodoListNameButton.getDrawable();
            setTodoListNameButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setTodoListMenuIcon(todoListName, todoIconToSet);
                    iconChangePopupWindow.dismiss();
                }
            });
        }
    }

    public void setTodoListMenuIcon(String todoListName, Drawable menuIcon) {
        MenuItem todoListMenuItem = getTodoListIdentifierByName(todoListName);
        todoListMenuItem.setIcon(menuIcon);
        TodoListFragment todoList = mTodoListFragmentsHandler.getFragmentByNameOrNull(todoListName);
        todoList.setMenuIcon(menuIcon);
    }

    private MenuItem getTodoListIdentifierByName(String todoListName) {
        Menu navigationViewMenu = mNavigationView.getMenu();
        int todoListIdentifier = todoListName.hashCode();
        return navigationViewMenu.findItem(todoListIdentifier);
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
                    ToastProvider.showToastAtCenterOfScreen(NON_UNIQUE_NAME_ERROR_TEXT, mContext);
                }
            }
        });
    }
}
