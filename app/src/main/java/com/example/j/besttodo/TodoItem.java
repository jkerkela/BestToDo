package com.example.j.besttodo;

public class TodoItem {


    private final TodoListAdapter mAdapter;
    private String todoItemText;
    private int day;
    private int month;
    private int year;

    public TodoItem(TodoListAdapter todoListAdapter) {
        this.mAdapter = todoListAdapter;
    }

    public TodoListAdapter getAdapter() {
        return mAdapter;
    }

    String getText() {
        return todoItemText;
    }

    void setText(String text) {
        this.todoItemText = text;
        mAdapter.notifyDataSetChanged();
    }

    public void setDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
        mAdapter.notifyDataSetChanged();
    }

}
