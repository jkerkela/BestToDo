package com.example.j.besttodo.util.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.example.j.besttodo.R;
import com.example.j.besttodo.TodoListFragment;

import java.util.LinkedHashMap;

public class TodoListFragmentsHandler {

    private final FragmentManager fragmentManager;

    private LinkedHashMap<String, TodoListFragment> todoListFragments = new LinkedHashMap<>();
    private TodoListFragment currentVisibleTodoListFragment;

    public TodoListFragmentsHandler(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    void addFragment(TodoListFragment listFragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction
                .add(R.id.listFragmentContainer, listFragment)
                .commit();
        addFragmentEntry(listFragment);
    }

    private void addFragmentEntry(TodoListFragment todoListFragment) {
        String fragmentName = todoListFragment.getName();
        todoListFragments.put(fragmentName, todoListFragment);
    }

    void removeFragment(String fragmentName) {
        TodoListFragment todoListFragment = getFragmentByNameOrNull(fragmentName);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction
                .remove(todoListFragment)
                .commit();
        removeFragmentEntry(fragmentName);
    }

    private void removeFragmentEntry(String fragmentName) {
        todoListFragments.remove(fragmentName);
    }

    void renameFragment(String oldTodoListName, String newTodoListName) {
        TodoListFragment fragmentToRename = getFragmentByNameOrNull(oldTodoListName);
        removeFragmentEntry(oldTodoListName);
        fragmentToRename.setName(newTodoListName);
        addFragmentEntry(fragmentToRename);
    }

    public void setVisibleFragment(String todoListFragmentName) {
        hideCurrentFragment();
        TodoListFragment todoListFragment = getFragmentByNameOrNull(todoListFragmentName);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction
                .show(todoListFragment)
                .commit();
        currentVisibleTodoListFragment = todoListFragment;
    }

    private void hideCurrentFragment() {
        if(currentVisibleTodoListFragment != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction
                    .hide(currentVisibleTodoListFragment)
                    .commit();
        }
    }

    public TodoListFragment getVisibleFragment() {
        return currentVisibleTodoListFragment;
    }

    TodoListFragment getFragmentByNameOrNull(String fragmentName) {
        return todoListFragments.get(fragmentName);
    }

    boolean doesFragmentExistWithName(String listNameToCheck) {
        TodoListFragment todoListFragmentToNotExist = getFragmentByNameOrNull(listNameToCheck);
        return todoListFragmentToNotExist == null;
    }

    public LinkedHashMap<String, TodoListFragment> getTodoListFragments() { return todoListFragments; }
}
