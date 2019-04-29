package com.example.j.besttodo.util.ui.storage;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;

import com.example.j.besttodo.TodoItem;
import com.example.j.besttodo.TodoListFragment;
import com.example.j.besttodo.TodoListFragmentsHandler;
import com.example.j.besttodo.util.ui.ViewHandler;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TodoListStorageHandler {

    private static final String SHARED_PREFS_TODO_LISTS_KEY = "TODO_LISTS";
    private static final String SHARED_PREFS_VISIBLE_TODO_LISTS_KEY = "TODO_LISTS_VISIBLE";
    private static final String DEFAULT_SCHEDULE_VALUE = "Schedule: N/A";
    private static final String DEFAULT_TODO_ITEM_DESCRIPTION_VALUE = "Add Todo Text Here";

    private final SharedPreferences sharedPreferences;
    private final ViewHandler viewHandler;
    private final TodoListFragmentsHandler todoListFragmentsHandler;
    private final ActionBar supportActionBar;

    public TodoListStorageHandler(SharedPreferences sharedPreferences,
                                  ActionBar supportActionBar,
                                  ViewHandler viewHandler,
                                  TodoListFragmentsHandler todoListFragmentsHandler) {
        this.sharedPreferences = sharedPreferences;
        this.supportActionBar = supportActionBar;
        this.viewHandler = viewHandler;
        this.todoListFragmentsHandler = todoListFragmentsHandler;
    }

    public void saveTodoListsToSharedPreferences() {
        //TODO: save todo list icons
        //TODO: fix saving / restoring of duplicate items
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        LinkedHashMap<String, TodoListFragment> todoListFragments = todoListFragmentsHandler.getTodoListFragments();
        Set<String> todoLists = getTodoListNames(todoListFragments);
        sharedPrefEditor.putStringSet(SHARED_PREFS_TODO_LISTS_KEY, todoLists);
        for (Map.Entry entry : todoListFragments.entrySet()) {
            TodoListFragment todoListFragment = (TodoListFragment) entry.getValue();
            saveTodoListItemsPrefs(todoListFragment, sharedPrefEditor);
        }
        saveVisibleFragmentToPrefs(sharedPrefEditor);
        sharedPrefEditor.apply();
    }

    private void saveVisibleFragmentToPrefs(SharedPreferences.Editor sharedPrefs) {
        String visibleFragment = viewHandler.getVisibleFragmentName();
        sharedPrefs.putString(SHARED_PREFS_VISIBLE_TODO_LISTS_KEY, visibleFragment);
    }

    private Set<String> getTodoListNames(LinkedHashMap<String, TodoListFragment> todoListFragments) {
        Set<String> todoLists = new LinkedHashSet<>();
        for (Map.Entry entry : todoListFragments.entrySet()) {
            String todoListName = entry.getKey().toString();
            todoLists.add(todoListName);
        }
        return todoLists;
    }

    private void saveTodoListItemsPrefs(TodoListFragment todoListFragment, SharedPreferences.Editor sharedPrefs) {
        String todoListName = todoListFragment.getName();
        List<TodoItem> todoItems = todoListFragment.getTodoItems();
        Set<String> todoItemAttributes = new LinkedHashSet<>();
        for(TodoItem item: todoItems) {
            String todoItemAttributeValues = constructTodoItemValues(item);
            todoItemAttributes.add(todoItemAttributeValues);
        }
        sharedPrefs.putStringSet(todoListName, todoItemAttributes);
    }

    private String constructTodoItemValues(TodoItem item) {
        return item.getText().replace(",", "\\,") + "," + item.getSchedule();
    }

    public void loadTodoItemListsFromPrefs() {
        Set<String> todoLists = sharedPreferences.getStringSet(SHARED_PREFS_TODO_LISTS_KEY, null);
        if (todoLists != null) {
            for(String todoList: todoLists) {
                TodoListFragment todoListFragment = viewHandler.addNewTodoListFragment(todoList);
                viewHandler.setVisibleFragmentByName(todoList);
                addTodoItemsToListFromPrefs(todoListFragment, sharedPreferences);
            }
            setVisibleTodoListFromPrefs(sharedPreferences);
        }
    }

    private void setVisibleTodoListFromPrefs(SharedPreferences prefs) {
        String visibleTodoList = prefs.getString(SHARED_PREFS_VISIBLE_TODO_LISTS_KEY, null);
        if (visibleTodoList != null) {
            viewHandler.setVisibleFragmentByName(visibleTodoList);
            supportActionBar.setTitle(visibleTodoList);
        }
    }

    private void addTodoItemsToListFromPrefs(TodoListFragment todoListFragment, SharedPreferences prefs) {
        Set<String> todoItemAttributeValues = prefs.getStringSet(todoListFragment.getName(), null);
        if (todoItemAttributeValues != null) {
            for (String values: todoItemAttributeValues) {
                TodoItem item = new TodoItem();
                String todoText = resolveTodoText(values);
                item.setText(todoText);
                String scheduleText = resolveScheduleText(values);
                item.setSchedule(scheduleText);
                todoListFragment.addTodoItem(item);
            }
        }
    }

    private String resolveTodoText(String values) {
        String[] intermediateValues = values.split(",");
        if (intermediateValues.length > 1) {
            return intermediateValues[0];
        }
        return DEFAULT_TODO_ITEM_DESCRIPTION_VALUE;
    }

    private String resolveScheduleText(String values) {
        String[] intermediateValues = values.split(",");
        if (intermediateValues.length > 1) {
            return intermediateValues[1];
        }
        return DEFAULT_SCHEDULE_VALUE;
    }
}
