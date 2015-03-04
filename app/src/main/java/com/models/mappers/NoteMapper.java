package com.models.mappers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.models.Note;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by José Ramón Díaz on 25/02/2015.
 */
public class NoteMapper {
    public NoteMapper(Context context){
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
        Note wNote = (Note)dbManager.getById(note);
        wNote.setSons(getSonsFromDB(wNote.getId()));
        return wNote;
    }

    public ArrayList getAllNotes(){
        ArrayList  notes = dbManager.getAll(new Note());
        return loadSons(notes);
    }

    public ArrayList getUndeletedNotes(){
        Cursor cursor = db.rawQuery("SELECT * FROM notes WHERE status = 0", null);
        return loadSons(getNotesFromCursor(cursor));
    }

    public ArrayList getDeletedNotes(){
        Cursor cursor = db.rawQuery("SELECT * FROM notes WHERE status = 1", null);
        return loadSons(getNotesFromCursor(cursor));
    }

    public ArrayList<Note> getNotesByDate(Date date){
        Cursor cursor = db.rawQuery("SELECT * FROM notes WHERE created_at = " + date.getTime(), null);
        return  loadSons(getNotesFromCursor(cursor));
    }

    public ArrayList<Note> getFavoriteNotes(){
        Cursor cursor = db.rawQuery("SELECT * FROM notes WHERE favorite = 1", null);
        return loadSons(getNotesFromCursor(cursor));
    }

    public ArrayList<Note> getFatherNotes(){
        Cursor cursor = db.rawQuery("SELECT * FROM notes WHERE id_father IS NOT NULL", null);
        return loadSons(getNotesFromCursor(cursor));
    }

    public ArrayList<Note> getSonsFromDB(int id){
        Cursor cursor = db.rawQuery("SELECT * FROM notes WHERE id_father = " + id, null);
            return getNotesFromCursor(cursor);
    }

    public void delteNote(Note note){
        dbManager.update(note);

    }

    //Aqui se deben agregar los hijos y las notas incrustadas
    private ArrayList<Note> getNotesFromCursor(Cursor cursor) {
        ArrayList<Note> notes = new ArrayList<>();
        while(cursor.moveToNext()){
            Note note = new Note();
            note.setContentValues(cursor);
            notes.add(note);
        }
        return notes;
    }

    private ArrayList loadSons(ArrayList notes){
        for (Note note:(ArrayList<Note>) notes){
            note.setSons(getSonsFromDB(note.getId()));
        }
        return notes;
    }

    private DBManager dbManager;
    private SQLiteDatabase db;
}
