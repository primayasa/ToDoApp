package com.primayasa.todoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    ArrayList<Todo> todos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Todo[] todos = new Todo[]{
                new Todo("Coba"),
                new Todo("Jajal"),
                new Todo("Hehehe")
        };

        // Lookup the recyclerview in activity layout
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.todo_recycler_view);
        // Create adapter passing in the sample user data
        TodoAdapter adapter = new TodoAdapter(todos);
        recyclerView.setHasFixedSize(true);
        // Set layout manager to position the items
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // That's all!
        recyclerView.setAdapter(adapter);
    }

    public void addTask(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Task");
        builder.setMessage("Task Title");
        final EditText inputField = new EditText(this);
        builder.setView(inputField);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("MainActivity",inputField.getText().toString());
            }
        });

        builder.setNegativeButton("Cancel", null);

        builder.create().show();
    }
}