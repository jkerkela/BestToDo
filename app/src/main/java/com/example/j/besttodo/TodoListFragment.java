package com.example.j.besttodo;

import android.app.ListFragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class TodoListFragment extends ListFragment {

    List<EditText> todoItemsList = new ArrayList<>();
    ArrayAdapter todoListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.todo_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        todoListAdapter = new ArrayAdapter<>(getActivity(), R.layout.editable_text_item, todoItemsList);
        setListAdapter(todoListAdapter);
        //TODO: editText needs to be @+id/todoItemText
        EditText editText = new EditText(this.getContext());
        todoItemsList.add(editText);
        todoListAdapter.notifyDataSetChanged();
    }

    public List<EditText> getTodoList(){
        return todoItemsList;
    }
}