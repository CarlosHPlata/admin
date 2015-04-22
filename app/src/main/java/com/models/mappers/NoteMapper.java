package com.models.mappers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.controllers.LinkController;
import com.models.Note;

import java.util.ArrayList;
import java.util.Date;

/**
 * Clase que se encarga de guardar y obtener las notas de la base de datos
 * @author Ramón Díaz
 * @version 0.1 25/02/2015.
 *
 */
public class NoteMapper {
    public NoteMapper(Context context){
        dbManager = new DBManager(context);
        db = dbManager.getWritableDb();
    }

    public ArrayList<Note> getNotDeletedNotesButThis(int noteId){
        Cursor cursor = db.rawQuery("SELECT * FROM notes WHERE status = 1 AND id != "+noteId, null);
        return loadSons(getNotesFromCursor(cursor));
    }

    public void deleteNotePermanently(Note note){
        db.execSQL("DELETE FROM notes WHERE id = " + note.getId());
        db.execSQL("DELETE FROM links WHERE note_id = " + note.getId() + " OR linked_note_id = " + note.getId());
        db.execSQL("DELETE FROM checklist WHERE id = " + note.getId());
        db.execSQL("DELETE FROM files WHERE id = " + note.getId());
    }

    public void dropNotes(){
        db.delete("notes",null,null);
        db.delete("links",null,null);
        db.delete("checklist",null,null);
        db.delete("files",null,null);
    }

    public void deleteNotes(ArrayList notes){
        for(Note note:(ArrayList<Note>) notes){
            delteNote(note);
        }
    }

    public void restore(Note note){
        note.setStatus(true);
        updateNote(note);
    }

    public long insertNote(Note note){
        return dbManager.insert(note);
    }

    public ArrayList<Note> insertMultipleNotes(ArrayList notes){
        return dbManager.multipleInsert(notes);
    }

    public void updateNote(Note note){
        note.setSyncFlag(false);
        dbManager.update(note);
    }

    public Note getById(Note note){
        Note wNote = (Note)dbManager.getById(note);
        wNote.setSons(getSonsFromDB(wNote.getId()));
        //Probablemente este metodo debe ser usado para cargar a cada nota sus links
        //wNote.setLinks(linkController.getLinksFromNoteId(wNote));
        return wNote;
    }

    public ArrayList getAllNotes(){
        ArrayList  notes = dbManager.getAll(new Note());
        return loadSons(notes);
    }

    public ArrayList getNotDeletedNotes(){
        Cursor cursor = db.rawQuery("SELECT * FROM notes WHERE status = 1", null);
        return loadSons(getNotesFromCursor(cursor));
    }

    public ArrayList getDeletedNotes(){
        Cursor cursor = db.rawQuery("SELECT * FROM notes WHERE status = 0", null);
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
        Cursor cursor = db.rawQuery("SELECT * FROM notes WHERE id_father = 0 AND status = 1 ORDER BY id ASC", null);
        return loadSons(getNotesFromCursor(cursor));
    }

    public ArrayList<Note> getSonsFromDB(int id){
        Cursor cursor = db.rawQuery("SELECT * FROM notes WHERE id_father = " + id +" AND status = 1", null);
        return getNotesFromCursor(cursor);
    }

    public void delteNote(Note note){
        note.setStatus(false);
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
    //El linkController no puede compartir el context
}
