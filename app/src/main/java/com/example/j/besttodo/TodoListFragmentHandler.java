package com.example.j.besttodo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;

import java.util.HashMap;

class TodoListFragmentHandler {

    private HashMap<String, TodoListFragment> fragmentList = new HashMap<>();
    private FragmentManager mFragmentManager;
    private TodoListFragment currentVisibleTodoListFragment = null;

    TodoListFragmentHandler(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    void addFragment(TodoListFragment listFragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction
                .add(R.id.listFragmentContainer, listFragment)
                .commit();
        addFragmentEntry(listFragment);
    }

    private void addFragmentEntry(TodoListFragment todoListFragment) {
        String fragmentName = todoListFragment.getName();
        fragmentList.put(fragmentName, todoListFragment);
    }

    void removeFragment(String fragmentName) {
        TodoListFragment todoListFragment = getFragmentByNameOrNull(fragmentName);
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction
                .remove(todoListFragment)
                .commit();
        removeFragmentEntry(fragmentName);
    }

    private void removeFragmentEntry(String fragmentName) {
        fragmentList.remove(fragmentName);
    }

    void renameFragment(String oldTodoListName, String newTodoListName) {
        TodoListFragment fragmentToRename = getFragmentByNameOrNull(oldTodoListName);
        removeFragmentEntry(oldTodoListName);
        fragmentToRename.setName(newTodoListName);
        addFragmentEntry(fragmentToRename);
    }

    void setVisibleFragment(String todoListFragmentName) {
        hideCurrentFragment();
        TodoListFragment todoListFragment = getFragmentByNameOrNull(todoListFragmentName);
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction
                .show(todoListFragment)
                .commit();
        currentVisibleTodoListFragment = todoListFragment;
    }

    private void hideCurrentFragment() {
        if (currentVisibleTodoListFragment != null) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction
                    .hide(currentVisibleTodoListFragment)
                    .commit();
        }
    }

    TodoListFragment getFragmentByNameOrNull(String fragmentName) {
        return fragmentList.get(fragmentName);
    }

    TodoListFragment getCurrentVisibleTodoList() {
        return currentVisibleTodoListFragment;
    }

    boolean doesFragmentExistWithName(String listNameToCheck) {
        TodoListFragment todoListFragmentToNotExist = getFragmentByNameOrNull(listNameToCheck);
        return todoListFragmentToNotExist == null;
    }
}
