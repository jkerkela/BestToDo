package com.example.j.besttodo.util.ui.storage;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.util.Base64;

import com.example.j.besttodo.R;
import com.example.j.besttodo.TodoItem;
import com.example.j.besttodo.TodoListFragment;
import com.example.j.besttodo.util.ui.TodoListFragmentsHandler;
import com.example.j.besttodo.util.ui.NavigationViewHandler;

import java.io.ByteArrayOutputStream;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TodoListPersistentStorageHandler {

    private static final String SHARED_PREFS_TODO_LISTS_KEY = "TODO_LISTS";
    private static final String SHARED_PREFS_VISIBLE_TODO_LISTS_KEY = "TODO_LISTS_VISIBLE";
    private static final String SHARED_PREFS_TODO_LIST_ITEMS_KEY_POST_FIX = "_ITEMS";
    private static final String SHARED_PREFS_TODO_LIST_ICON_KEY_POST_FIX = "_ICON";

    private final SharedPreferences sharedPreferences;
    private final NavigationViewHandler navigationViewHandler;
    private final TodoListFragmentsHandler todoListFragmentsHandler;
    private final ActionBar supportActionBar;
    private final Activity activity;

    private enum PrefsType {
        TODO_ITEM,
        LIST_ICON
    }

    public TodoListPersistentStorageHandler(Activity activity,
                                            SharedPreferences sharedPreferences,
                                            ActionBar supportActionBar,
                                            NavigationViewHandler navigationViewHandler,
                                            TodoListFragmentsHandler todoListFragmentsHandler) {
        this.activity = activity;
        this.sharedPreferences = sharedPreferences;
        this.supportActionBar = supportActionBar;
        this.navigationViewHandler = navigationViewHandler;
        this.todoListFragmentsHandler = todoListFragmentsHandler;
    }

    public void saveTodoListsToSharedPreferences() {
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        LinkedHashMap<String, TodoListFragment> todoListFragments = todoListFragmentsHandler.getTodoListFragments();
        Set<String> todoLists = getTodoListNames(todoListFragments);
        sharedPrefEditor.putStringSet(SHARED_PREFS_TODO_LISTS_KEY, todoLists);
        for(Map.Entry entry : todoListFragments.entrySet()) {
            TodoListFragment todoListFragment = (TodoListFragment) entry.getValue();
            saveTodoListAttributesToPrefs(todoListFragment, sharedPrefEditor);
        }
        saveVisibleFragmentToPrefs(sharedPrefEditor);
        sharedPrefEditor.apply();
    }

    private Set<String> getTodoListNames(LinkedHashMap<String, TodoListFragment> todoListFragments) {
        Set<String> todoLists = new LinkedHashSet<>();
        for(Map.Entry entry : todoListFragments.entrySet()) {
            String todoListName = entry.getKey().toString();
            todoLists.add(todoListName);
        }
        return todoLists;
    }

    private void saveTodoListAttributesToPrefs(TodoListFragment todoListFragment, SharedPreferences.Editor sharedPrefs) {
        saveTodoListItemsToPrefs(todoListFragment, sharedPrefs);
        saveTodoListIconToPrefs(todoListFragment, sharedPrefs);
    }

    private void saveTodoListItemsToPrefs(TodoListFragment todoListFragment, SharedPreferences.Editor sharedPrefs) {
        List<TodoItem> todoItems = todoListFragment.getTodoItems();
        Set<String> todoItemAttributes = new LinkedHashSet<>();
        for(TodoItem item: todoItems) {
            String todoItemAttributeValues = constructTodoItemValues(item);
            todoItemAttributes.add(todoItemAttributeValues);
        }
        String sharedPrefsKey = generateTodoListAttrPrefsKey(todoListFragment.getName(), PrefsType.TODO_ITEM);
        sharedPrefs.putStringSet(sharedPrefsKey, todoItemAttributes);
    }

    private String constructTodoItemValues(TodoItem item) {
        return item.getText().replace(",", "\\,") + "," + item.getSchedule();
    }

    private String generateTodoListAttrPrefsKey(String todoListFragmentName, PrefsType prefsType) {
        switch(prefsType) {
            case TODO_ITEM:
                return todoListFragmentName + SHARED_PREFS_TODO_LIST_ITEMS_KEY_POST_FIX;
            case LIST_ICON:
                return todoListFragmentName + SHARED_PREFS_TODO_LIST_ICON_KEY_POST_FIX;
            default:
                return null;
        }
    }

    private void saveTodoListIconToPrefs(TodoListFragment todoListFragment, SharedPreferences.Editor sharedPrefs) {
        Drawable icon = todoListFragment.getMenuIcon();
        if(icon != null) {
            String iconAsString = convertDrawableToStringResource(icon);
            String sharedPrefsKey = generateTodoListAttrPrefsKey(todoListFragment.getName(), PrefsType.LIST_ICON);
            sharedPrefs.putString(sharedPrefsKey, iconAsString);
        }
    }

    private String convertDrawableToStringResource(Drawable drawable) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void saveVisibleFragmentToPrefs(SharedPreferences.Editor sharedPrefs) {
        String visibleFragmentName = todoListFragmentsHandler.getVisibleFragment().getName();
        sharedPrefs.putString(SHARED_PREFS_VISIBLE_TODO_LISTS_KEY, visibleFragmentName);
    }

    public void loadTodoItemListsFromPrefs() {
        Set<String> todoLists = sharedPreferences.getStringSet(SHARED_PREFS_TODO_LISTS_KEY, null);
        if(todoLists != null) {
            for (String todoList: todoLists) {
                TodoListFragment todoListFragment = navigationViewHandler.addNewTodoListFragment(todoList);
                todoListFragmentsHandler.setVisibleFragment(todoList);
                addIconToTodoListMenuItem(todoList, sharedPreferences);
                addTodoItemsToListFromPrefs(todoListFragment, sharedPreferences);
            }
            setVisibleTodoListFromPrefs(sharedPreferences);
        }
    }

    private void addIconToTodoListMenuItem(String todoList, SharedPreferences prefs) {
        String sharedPrefsKey = generateTodoListAttrPrefsKey(todoList, PrefsType.LIST_ICON);
        String todoItemIconValue = prefs.getString(sharedPrefsKey, null);
        if(todoItemIconValue != null) {
            Drawable icon = convertStringToDrawable(todoItemIconValue);
            navigationViewHandler.setTodoListMenuIcon(todoList, icon);
        }
    }

    private Drawable convertStringToDrawable(String drawableAsString){
        byte[] byteArray = Base64.decode(drawableAsString, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return new BitmapDrawable(activity.getResources(), bitmap);
    }

    private void addTodoItemsToListFromPrefs(TodoListFragment todoListFragment, SharedPreferences prefs) {
        String sharedPrefsKey = generateTodoListAttrPrefsKey(todoListFragment.getName(), PrefsType.TODO_ITEM);
        Set<String> todoItemAttributeValues = prefs.getStringSet(sharedPrefsKey, null);
        if (todoItemAttributeValues != null) {
            for(String values: todoItemAttributeValues) {
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
        if(intermediateValues.length > 1) {
            return intermediateValues[0];
        }
        return activity.getResources().getString(R.string.todoItemText);
    }

    private String resolveScheduleText(String values) {
        String[] intermediateValues = values.split(",");
        if(intermediateValues.length > 1) {
            return intermediateValues[1];
        }
        return activity.getResources().getString(R.string.todoItemSchedulePlaceholder);
    }

    private void setVisibleTodoListFromPrefs(SharedPreferences prefs) {
        String visibleTodoList = prefs.getString(SHARED_PREFS_VISIBLE_TODO_LISTS_KEY, null);
        if(visibleTodoList != null) {
            todoListFragmentsHandler.setVisibleFragment(visibleTodoList);
            supportActionBar.setTitle(visibleTodoList);
        }
    }
}
