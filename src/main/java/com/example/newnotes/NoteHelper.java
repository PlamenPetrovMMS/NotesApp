package com.example.newnotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

public class NoteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "NOTES_DATABASE";
    private static final String TABLE_NAME = "notes_table";
    private static final int VERSION = 1;

    public NoteHelper(@NonNull Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE notes_table(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, description TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS notes_table");
    }

    public long insertNote(String title, String description){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("description", description);
        long insertedNoteId = database.insert("notes_table", null, contentValues);
        return insertedNoteId;
    }
    public void updateNote(int id, String title, String description){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("description", description);
        database.update(TABLE_NAME, values, "id = ?", new String[]{String.valueOf(id)});
    }
    public Cursor showData(){
        SQLiteDatabase database = this.getReadableDatabase();
        // raw query not safe from SQL injections !
        return database.rawQuery("SELECT * FROM notes_table", null);
    }
    public void deleteNote(int id){
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(id)});
    }
}
