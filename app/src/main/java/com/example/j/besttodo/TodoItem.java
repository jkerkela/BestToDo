package com.example.j.besttodo;

public class TodoItem {


    private String todoItemText;
    private String schedule;

    public String getText() {
        return todoItemText;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setText(String text) {
        this.todoItemText = text;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

}
