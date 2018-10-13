package com.example.j.besttodo;

/**
 * Created by J on 10/13/2018.
 */

class TodoItem {

    private String mDescription;

    TodoItem(String description) {
        mDescription = description;
    }

    String getDescription() {
        return mDescription;
    }

    void setDescription(String description) {
        mDescription = description;
    }
}
