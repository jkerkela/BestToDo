package com.example.j.besttodo;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;


public class MainActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: move image button to fragment
        ImageButton imageButton = findViewById(R.id.addNewTodoItemButton);

        if (savedInstanceState == null) {
            Fragment newFragment = new TodoListFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.listFragment, newFragment).commit();
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: add new todo item to fragment
            }
        });
    }

}
