package com.primayasa.todoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.primayasa.todoapp.db.TaskContract;
import com.primayasa.todoapp.db.TaskDBHelper;

public class HomeActivity extends AppCompatActivity {
    Todo[] todos;
    private TaskDBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        updateUI();
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
                String task = inputField.getText().toString();

                helper = new TaskDBHelper(HomeActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.clear();
                values.put(TaskContract.Columns.TASK, task);

                db.insertWithOnConflict(TaskContract.TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);

                updateUI();
            }
        });

        builder.setNegativeButton("Cancel", null);

        builder.create().show();
    }

    public void onEditButtonClick (View view) {
        View v = (View) view.getParent();
        TextView taskTextView = (TextView) v.findViewById(R.id.todoTV);
        String task = taskTextView.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Task");
        builder.setMessage("Task Title");
        final EditText inputField = new EditText(this);
        builder.setView(inputField);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String update = inputField.getText().toString();

                String sql = String.format("UPDATE %s SET %s = '%s' WHERE %s = '%s'",
                        TaskContract.TABLE, TaskContract.Columns.TASK,
                        update,TaskContract.Columns.TASK, task);

                helper = new TaskDBHelper(HomeActivity.this);
                SQLiteDatabase sqlDB = helper.getWritableDatabase();

                sqlDB.execSQL(sql);

                updateUI();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    public void onDeleteButtonClick(View view) {
        View v = (View) view.getParent();
        TextView taskTextView = (TextView) v.findViewById(R.id.todoTV);
        String task = taskTextView.getText().toString();

        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                TaskContract.TABLE, TaskContract.Columns.TASK, task);

        helper = new TaskDBHelper(HomeActivity.this);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        sqlDB.execSQL(sql);
        updateUI();
    }

    private void updateUI() {
        helper = new TaskDBHelper(HomeActivity.this);

        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        Cursor cursor = sqlDB.rawQuery("SELECT  * FROM tasks", null);

        int length = 0;
        if (cursor.moveToFirst()) {
            do {
                length++;
            } while (cursor.moveToNext());
        }

        todos = new Todo[length];
        int i = 0;
        if (cursor.moveToFirst()) {
            do {
                todos[i] = new Todo(cursor.getString(1));
                i++;
            } while (cursor.moveToNext());
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.todo_recycler_view);
        TodoAdapter adapter = new TodoAdapter(todos);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}