package com.example.j.besttodo;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import java.util.List;

class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.MyViewHolder> {

    private final List<TodoItem> mTodoItemList;
    private Context mContext;

    TodoListAdapter(List<TodoItem> todoItemList, Activity context) {
        mTodoItemList = todoItemList;
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View todoItemView = layoutInflater.inflate(R.layout.todo_item, parent, false);
        return new MyViewHolder(todoItemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
    }

    @Override
    public void onViewRecycled(MyViewHolder holder) {
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
                    addListenerToRemoveTodoItemButtonOnPopupWindow(popupView, todoItemPopup);
                }
            });
        }

        private void addListenerToRemoveTodoItemButtonOnPopupWindow(View todoItemPopupView, final PopupWindow todoItemPopup) {
            ImageButton todoItemRemoveButton = todoItemPopupView.findViewById(R.id.TodoItemRemoveButton);
            todoItemRemoveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getLayoutPosition();
                    mTodoItemList.remove(position);
                    notifyItemRemoved(position);
                    todoItemPopup.dismiss();
                }
            });
        }
    }
}
