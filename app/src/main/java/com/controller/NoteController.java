package com.controller;

import android.content.Context;

import com.model.Note;
import com.model.service.LoginService;
import com.model.service.NoteService;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by José Ramón Díaz on 03/03/2015.
 */
public class NoteController {
    public NoteController(Context context){
        loginService = new LoginService(context);
        noteService = new NoteService(context);
    }

    public boolean logOut(){
        return loginService.logOut();
    }

    public boolean isUserLogIn(){
        return loginService.isUserLoggedIn();
    }

    public boolean addNote(Note note){

        return false;
    }

    public long insertNote(Note note){
        return noteService.insertNote(note);
    }

    public ArrayList<Note> insertMultipleNotes(ArrayList<Note> notes){
        return noteService.insertMultipleNotes(notes);
    }

    public void updateNote(Note note){
        noteService.updateNote(note);
    }

    public Note getNoteById(Note note){
        return noteService.getNoteById(note);
    }

    public ArrayList getAllNotes(){
        return  noteService.getAllNotes();
    }

    public ArrayList getNotDeletedNotes(){
        return noteService.getNotDeletedNotes();
    }

    public ArrayList getDeletedNotes(){
        return noteService.getDeletedNotes();
    }

    public ArrayList getNotesByDate(Date date){
        return noteService.getNotesByDate(date);
    }

    public ArrayList getFavoriteNotes(){
        return noteService.getFavoriteNotes();
    }

    public ArrayList getFatherNotes(){
        return noteService.getFatherNotes();
    }

    public ArrayList getSons(Note note){
        return noteService.getSons(note);
    }

    public void deleteNote(Note note){
        noteService.deleteNote(note);
    }

    private NoteService noteService;

    private LoginService loginService;
}
