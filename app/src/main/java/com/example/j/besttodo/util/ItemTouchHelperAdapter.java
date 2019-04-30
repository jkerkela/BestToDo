package com.example.j.besttodo.util;

public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
