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
import android.widget.TextView;

import java.util.List;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.MyViewHolder> {

    private final List<TodoItem> mTodoItemList;
    private Context mContext;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TodoItem todoItem = mTodoItemList.get(position);
        //TODO: implement getter+setter for todoItemText
        //holder.todoItemText.setText(todoItem.getText());
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
            todoItemActionButton = view.findViewById(R.id.todoItemActionButton);
            addListenerToTodoItemActionButton(todoItemActionButton);
        }

        private void addListenerToTodoItemActionButton(ImageButton todoItemActionButton) {
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
            todoItemPopup.showAsDropDown(todoItemPopupView, locationCoordinates[0], locationCoordinates[1]);
            addListenerToRemoveTodoItemButtonOnPopupWindow(todoItemPopupView, todoItemPopup);
        }

        private void addListenerToRemoveTodoItemButtonOnPopupWindow(View todoItemPopupView, final PopupWindow todoItemPopup) {
            TextView placeholder = todoItemPopupView.findViewById(R.id.TodoItemRemoveButtonPlaceholder);
            placeholder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getLayoutPosition();
                    mTodoItemList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mTodoItemList.size());
                    todoItemPopup.dismiss();
                }
            });
        }
    }

    TodoListAdapter(List<TodoItem> todoItemList, Activity context) {
        mTodoItemList = todoItemList;
        mContext = context;
    }

}
