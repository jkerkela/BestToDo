package com.example.j.besttodo;


/**
 * Created by J on 10/13/2018.
 */
class TodoItem {


    private String todoItemText;
    private String defaultText = String.valueOf(R.string.todoItemText);

    public String getText() {
        return todoItemText;
    }

    public void setText(String text) {
        this.todoItemText = text;
    }

    public void resetDefaultText() {
        this.todoItemText = defaultText;
    }
}
