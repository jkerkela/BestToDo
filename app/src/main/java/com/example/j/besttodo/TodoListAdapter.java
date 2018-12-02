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

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.MyViewHolder> {

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
        TodoItem todoItem = mTodoItemList.get(position);
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
            //TODO: when list item is deleted and recreated, text should be set to default
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
                    int[] locationCoordinates = locatePosition(view);
                    showPopUpAtClickLocation(locationCoordinates);
                }
            });
        }

        private int[] locatePosition(View view) {
            int[] locationCoordinates = new int[2];
            view.getLocationOnScreen(locationCoordinates);
            int adjustedYAxisLocation = adjustLocationToViewItemHeight(locationCoordinates[1], view);
            locationCoordinates[1] = adjustedYAxisLocation;
            return locationCoordinates;
        }

        private int adjustLocationToViewItemHeight(int yAxisLoc, View view) {
            return view.getHeight() + yAxisLoc;
        }

        private void showPopUpAtClickLocation(int[] locationCoordinates) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            View todoItemPopupView = layoutInflater.inflate(R.layout.todo_item_popup, null);
            PopupWindow todoItemPopup = new PopupWindow(todoItemPopupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            todoItemPopup.setFocusable(true);
            todoItemPopup.setElevation(40);
            todoItemPopup.showAsDropDown(todoItemPopupView, locationCoordinates[0], locationCoordinates[1]);
            addListenerToRemoveTodoItemButtonOnPopupWindow(todoItemPopupView, todoItemPopup);
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
