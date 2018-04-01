package com.example.j.besttodo;

import android.app.ListFragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class TodoListFragment extends ListFragment {

    List<String> todoItemsList = new ArrayList<>();
    ArrayAdapter arrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, todoItemsList);
        setListAdapter(arrayAdapter);
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    public List<String> getTodoList(){
        return todoItemsList;
    }
}