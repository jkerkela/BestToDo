package com.example.j.besttodo;

import android.util.SparseArray;

import java.util.HashMap;
import java.util.List;

/**
 * Created by J on 12/15/2018.
 */

public class TodoListFragmentNameHolder {

    private SparseArray<String> fragmentMap = new SparseArray<>();

    void addFragment(int fragmentIndexInStack, String fragmentName) {
        fragmentMap.put(fragmentIndexInStack, fragmentName);
    }

    SparseArray getFragmentMap() {
        return fragmentMap;
    }
}
