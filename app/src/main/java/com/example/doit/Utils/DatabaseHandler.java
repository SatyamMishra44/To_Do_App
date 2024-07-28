package com.example.doit.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.doit.model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "toDoListDatabase";
    private static final String ToDo_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + ToDo_TABLE + "(" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TASK + " TEXT, " +
            STATUS + " INTEGER)";

    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + ToDo_TABLE);

        // Create tables again
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void closeDatabase() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    public void insertTask(ToDoModel task) {
        openDatabase(); // Ensure the database is open
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTask());
        cv.put(STATUS, 0); // Assuming default status is 0
        db.insert(ToDo_TABLE, null, cv);
        closeDatabase(); // Close the database after operation
    }

    public List<ToDoModel> getAllTasks() {
        List<ToDoModel> taskList = new ArrayList<>();
        openDatabase(); // Ensure the database is open
        Cursor cursor = null;
        db.beginTransaction();
        try {
            cursor = db.query(ToDo_TABLE, null, null, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        ToDoModel task = new ToDoModel();
                        task.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                        task.setTask(cursor.getString(cursor.getColumnIndex(TASK)));
                        task.setStatus(cursor.getInt(cursor.getColumnIndex(STATUS)));
                        taskList.add(task);
                    } while (cursor.moveToNext());
                }
            }
            db.setTransactionSuccessful();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            db.endTransaction();
            closeDatabase(); // Close the database after operation
        }
        return taskList;
    }

    public void updateStatus(int id, int status) {
        openDatabase(); // Ensure the database is open
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(ToDo_TABLE, cv, ID + "=?", new String[]{String.valueOf(id)});
        closeDatabase(); // Close the database after operation
    }

    public void updateTask(int id, String task) {
        openDatabase(); // Ensure the database is open
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        db.update(ToDo_TABLE, cv, ID + "=?", new String[]{String.valueOf(id)});
        closeDatabase(); // Close the database after operation
    }

    public void deleteTask(int id) {
        openDatabase(); // Ensure the database is open
        db.delete(ToDo_TABLE, ID + "=?", new String[]{String.valueOf(id)});
        closeDatabase(); // Close the database after operation
    }
}
