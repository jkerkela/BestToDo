package com.example.j.besttodo;

import java.util.HashMap;

/**
 * Created by J on 12/15/2018.
 */

public class TodoListFragmentHolder {

    private HashMap<String, TodoListFragment> fragmentList = new HashMap<>();

    void addFragment(TodoListFragment todoListFragment) {
        String fragmentName = todoListFragment.getName();
        fragmentList.put(fragmentName, todoListFragment);
    }

    TodoListFragment getFragmentByName(String fragmentName) {
        return fragmentList.get(fragmentName);
    }
}
