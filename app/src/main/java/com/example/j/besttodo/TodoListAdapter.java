package com.example.j.besttodo;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import com.example.j.besttodo.util.DatePickerFragment;
import com.example.j.besttodo.util.ui.PopUpProvider;
import com.example.j.besttodo.util.ItemTouchHelperAdapter;

import java.util.Collections;
import java.util.List;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.MyViewHolder>
        implements ItemTouchHelperAdapter {

    private final List<TodoItem> todoItemList;
    private Context context;

    TodoListAdapter(List<TodoItem> todoItemList, Activity context) {
        this.todoItemList = todoItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View todoItemView = layoutInflater.inflate(R.layout.todo_item, parent, false);
        return new MyViewHolder(todoItemView);
    }

    @Override
    public int getItemCount() {
        return todoItemList.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(todoItemList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(todoItemList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        todoItemList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TodoItem todoItem = todoItemList.get(position);
        String existingText = todoItem.getText();
        holder.todoItemText.setText(existingText);
        String existingSchedule = todoItem.getSchedule();
        holder.todoItemDate.setText(existingSchedule);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        EditText todoItemText;
        ImageButton todoItemActionButton;
        EditText todoItemDate;

        MyViewHolder(View view) {
            super(view);
            todoItemText = view.findViewById(R.id.todoItemDescription);
            todoItemText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus) {
                        todoItemList.get(getLayoutPosition()).setText(todoItemText.getText().toString());
                    }
                }
            });
            todoItemDate = view.findViewById(R.id.todoItemSchedule);
            todoItemActionButton = view.findViewById(R.id.todoItemActionButton);
            todoItemActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE);
                    if (layoutInflater != null) {
                        View popupView = layoutInflater.inflate(R.layout.todo_item_popup, null);
                        PopupWindow todoItemPopup = PopUpProvider.providePopUpWindowOnViewAtClickLocation(popupView, view);
                        addListenerToTodoItemDateButtonOnPopupWindow(popupView, todoItemPopup);
                    }
                }
            });
        }

        private void addListenerToTodoItemDateButtonOnPopupWindow(View todoItemPopupView, final PopupWindow todoItemPopup) {
            ImageButton todoItemSetDateButton = todoItemPopupView.findViewById(R.id.TodoItemSetDateButton);
            todoItemSetDateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TodoItem activeTodoItem = todoItemList.get(getLayoutPosition());
                    DatePickerFragment datePickerFragment = new DatePickerFragment();
                    datePickerFragment.setTodoItemHandle(activeTodoItem, todoItemDate);
                    FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                    datePickerFragment.show(fragmentManager, "date picker");
                    todoItemPopup.dismiss();
                }
            });
        }
    }
}
