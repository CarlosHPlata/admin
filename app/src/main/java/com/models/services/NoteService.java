package com.models.services;

import android.content.Context;

import com.models.Note;
import com.models.mappers.NoteMapper;

import java.util.ArrayList;
import java.util.Date;

/** *
 *
 * Esta clase contiene la logica de negocio de las notas, se encarga de realizar la conección
 * con los mapper y modelos.
 *
 * @author Edgar
 *
 * @version 0.1, 01/03/2015.
 *
 * */
public class NoteService {

    public NoteService(Context context){
        noteMapper = new NoteMapper(context);
    }

    public boolean addNote(Note note){
        long longAux =  noteMapper.insertNote(note);
        if(longAux > 0){
            return true;
        }else{
            return false;
        }
    }

    public ArrayList<Note> insertMultipleNotes(ArrayList<Note> notes){
        return noteMapper.insertMultipleNotes(notes);
    }

    public Note findOneById(int id){
        Note note = new Note();
        note.setId(id);
        note = (Note) noteMapper.getById(note);
        return note;
    }

    public ArrayList getAllNotes(){
        return  noteMapper.getAllNotes();
    }

    public ArrayList getNotDeletedNotes(){
        return noteMapper.getNotDeletedNotes();
    }

    public ArrayList getDeletedNotes(){
        return noteMapper.getDeletedNotes();
    }

    public ArrayList getNotesByDate(Date date){
        return noteMapper.getNotesByDate(date);
    }

    public ArrayList getFavoriteNotes(){
        return noteMapper.getFavoriteNotes();
    }

    public ArrayList getFatherNotes(){
        return noteMapper.getFatherNotes();
    }

    public ArrayList getSons(Note note){
        return noteMapper.getSonsFromDB(note.getId());
    }

    public void updateNote(Note note){
        noteMapper.updateNote(note);
     }

    public void deleteNote(Note note){
        noteMapper.delteNote(note);
    }

   public ArrayList<Note> findNotesSonByIdFather(int id){
        return noteMapper.getSonsFromDB(id);
    }

    private NoteMapper noteMapper;

}
