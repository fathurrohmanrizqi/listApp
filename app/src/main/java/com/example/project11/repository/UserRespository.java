package com.example.project11.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.project11.database.DatabaseHelper;
import com.example.project11.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserRespository {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private Context context;

    public UserRespository(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
        if (database != null && database.isOpen()) {
            Log.d("Database Connected", "open: Successfully to connected database");
        } else {
            Toast.makeText(context, "Failed to connect to the database", Toast.LENGTH_SHORT).show();
        }
    }

    public void close() {
        if (database != null && database.isOpen()) {
            database.close();
        }
        dbHelper.close();
    }

    public long createUser(String name, String domisili) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_DOMISILI, domisili);
        return database.insert(DatabaseHelper.TABLE_USER, null, values);
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = database.query(DatabaseHelper.TABLE_USER,
                    new String[]{DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_DOMISILI},
                    null, null, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                int idIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
                int nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME);
                int domisiliIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DOMISILI);

                if (idIndex != -1 && nameIndex != -1 && domisiliIndex != -1) {
                    cursor.moveToFirst();
                    do {
                        long id = cursor.getLong(idIndex);
                        String name = cursor.getString(nameIndex);
                        String domisili = cursor.getString(domisiliIndex);
                        users.add(new User(id, name, domisili));
                    } while (cursor.moveToNext());
                } else {
                    Log.e("UserRepository", "One or more columns are missing in the cursor");
                }
            } else {
                Log.e("UserRepository", "Cursor is null or empty");
            }
        } catch (Exception e) {
            Log.e("UserRepository", "Error while fetching users: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return users;
    }

    public User getUserById(long id) {
        User user = null;
        Cursor cursor = null;
        try {
            cursor = database.query(DatabaseHelper.TABLE_USER,
                    new String[]{DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_DOMISILI},
                    DatabaseHelper.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(id)},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
                int nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME);
                int domisiliIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DOMISILI);

                if (idIndex != -1 && nameIndex != -1 && domisiliIndex != -1) {
                    long userId = cursor.getLong(idIndex);
                    String name = cursor.getString(nameIndex);
                    String domisili = cursor.getString(domisiliIndex);
                    user = new User(userId, name, domisili);
                } else {
                    Log.e("UserRepository", "One or more columns are missing in the cursor");
                }
            } else {
                Log.e("UserRepository", "Cursor is null or empty");
            }
        } catch (Exception e) {
            Log.e("UserRepository", "Error while fetching user by ID: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return user;
    }

    public int updateUser(long id, String name, String domisili) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_DOMISILI, domisili);
        return database.update(DatabaseHelper.TABLE_USER, values,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public void deleteUser(long id) {
        database.delete(DatabaseHelper.TABLE_USER,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
    }
}
