package com.example.j.besttodo;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import com.example.j.besttodo.util.ui.PopUpProvider;
import com.example.j.besttodo.util.ItemTouchHelperAdapter;

import java.util.Collections;
import java.util.List;

class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.MyViewHolder>
        implements ItemTouchHelperAdapter {

    private final List<TodoItem> mTodoItemList;
    private Context mContext;

    TodoListAdapter(List<TodoItem> todoItemList, Activity context) {
        mTodoItemList = todoItemList;
        mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View todoItemView = layoutInflater.inflate(R.layout.todo_item, parent, false);
        return new MyViewHolder(todoItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
    }

    @Override
    public void onViewRecycled(@NonNull MyViewHolder holder) {
        if(isAddedToListAsNew(holder)) {
            String defaultTodoItemText = mContext.getResources().getString(R.string.todoItemText);
            holder.todoItemText.setText(defaultTodoItemText);
        }

    }

    private boolean isAddedToListAsNew(MyViewHolder holder) {
        return holder.getLayoutPosition() == -1;
    }

    @Override
    public int getItemCount() {
        return mTodoItemList.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mTodoItemList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mTodoItemList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        mTodoItemList.remove(position);
        notifyItemRemoved(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        EditText todoItemText;
        ImageButton todoItemActionButton;

        MyViewHolder(View view) {
            super(view);
            todoItemText = view.findViewById(R.id.todoItemDescription);
            todoItemText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus) {
                        mTodoItemList.get(getLayoutPosition()).setText(todoItemText.getText().toString());
                        notifyDataSetChanged();
                    }
                }
            });

            //TODO: placeholder button for TODO item actions
            todoItemActionButton = view.findViewById(R.id.todoItemActionButton);
            todoItemActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE);
                    View popupView = null;
                    if (layoutInflater != null) {
                        popupView = layoutInflater.inflate(R.layout.todo_item_popup, null);
                    }
                    PopupWindow todoItemPopup = PopUpProvider.providePopUpWindowOnViewAtClickLocation(popupView, view);
                }
            });
        }
    }
}
