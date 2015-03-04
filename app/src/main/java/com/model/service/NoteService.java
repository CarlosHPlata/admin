package com.model.service;

import android.content.Context;

import com.model.Note;
import com.model.mapper.NoteMapper;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by José Ramón Díaz on 03/03/2015.
 */
public class NoteService {
    public NoteService(Context context){
        noteMapper = new NoteMapper(context);
    }

    public long insertNote(Note note){
        return noteMapper.insertNote(note);
    }

    public ArrayList<Note> insertMultipleNotes(ArrayList<Note> notes){
        return noteMapper.insertMultipleNotes(notes);
    }

    public void updateNote(Note note){
        noteMapper.updateNote(note);
    }

    public Note getNoteById(Note note){
        return noteMapper.getById(note);
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

    public void deleteNote(Note note){
        noteMapper.deleteNote(note);
    }

    private NoteMapper noteMapper;
}
