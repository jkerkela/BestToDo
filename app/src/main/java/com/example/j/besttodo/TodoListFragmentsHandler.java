package com.example.j.besttodo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;

import java.util.LinkedHashMap;

public class TodoListFragmentsHandler {

    private LinkedHashMap<String, TodoListFragment> todoListfragments = new LinkedHashMap<>();
    private FragmentManager fragmentManager;
    private TodoListFragment currentVisibleTodoListFragment = null;

    TodoListFragmentsHandler(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void addFragment(TodoListFragment listFragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction
                .add(R.id.listFragmentContainer, listFragment)
                .commit();
        addFragmentEntry(listFragment);
    }

    private void addFragmentEntry(TodoListFragment todoListFragment) {
        String fragmentName = todoListFragment.getName();
        todoListfragments.put(fragmentName, todoListFragment);
    }

    public void removeFragment(String fragmentName) {
        TodoListFragment todoListFragment = getFragmentByNameOrNull(fragmentName);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction
                .remove(todoListFragment)
                .commit();
        removeFragmentEntry(fragmentName);
    }

    private void removeFragmentEntry(String fragmentName) {
        todoListfragments.remove(fragmentName);
    }

    public void renameFragment(String oldTodoListName, String newTodoListName) {
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

    public TodoListFragment getVisibleFragment() {
        return currentVisibleTodoListFragment;
    }
    private void hideCurrentFragment() {
        if (currentVisibleTodoListFragment != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction
                    .hide(currentVisibleTodoListFragment)
                    .commit();
        }
    }

    TodoListFragment getFragmentByNameOrNull(String fragmentName) {
        return todoListfragments.get(fragmentName);
    }

    TodoListFragment getVisibleTodoList() {
        return currentVisibleTodoListFragment;
    }

    public boolean doesFragmentExistWithName(String listNameToCheck) {
        TodoListFragment todoListFragmentToNotExist = getFragmentByNameOrNull(listNameToCheck);
        return todoListFragmentToNotExist == null;
    }

    public LinkedHashMap<String, TodoListFragment> getTodoListFragments() {
        return todoListfragments;
    }
}
