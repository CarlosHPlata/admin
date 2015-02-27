package com.example.usuario.androidadmin.model.dataBase;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.model.Note;
import com.test.ListNotesTest;
import com.view.ListNotes;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import androidadmin.note.New;

/**
 * Created by José Ramón Díaz on 25/02/2015.
 */
public class NoteManager {
    public NoteManager(Context context){
        dbManager = new DBManager(context);
        db = dbManager.getWritableDb();
    }

    public long insertNote(Note note){
        return dbManager.insert(note);
    }

    public ArrayList<Note> insertMultipleNotes(ArrayList notes){
        return dbManager.multipleInsert(notes);
    }

    public void updateNote(Note note){
        dbManager.update(note);
    }

    public Note getById(Note note){
        return (Note)dbManager.getById(note);
    }

    public ArrayList getAllNotes(){
        return dbManager.getAll(new Note());
    }

    public ArrayList<Note> getNotesByDate(Date date){
        Cursor cursor = db.rawQuery("SELECT * FROM notes WHERE created_at = '" + date.toString() + "'", null);
        return  getNotesFromCursor(cursor);
    }

    public ArrayList<Note> getFavoriteNotes(){
        Cursor cursor = db.rawQuery("SELECT * FROM notes WHERE favorit = 1", null);
        return getNotesFromCursor(cursor);
    }

    public ArrayList<Note> getFatherNotes(){
        Cursor cursor = db.rawQuery("SELECT * FROM notes WHERE id_father IS NOT NULL", null);
        return getNotesFromCursor(cursor);
    }

    private ArrayList<Note> getNotesFromCursor(Cursor cursor) {
        ArrayList<Note> notes = new ArrayList<>();
        while(cursor.moveToNext()){
            Note note = new Note();
            note.setContentValues(cursor);
            notes.add(note);
        }
        cursor.close();
        return notes;
    }

    private DBManager dbManager;
    private SQLiteDatabase db;
}
