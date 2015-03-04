package com.controllers;

import android.content.Context;

import com.models.Note;
import com.models.services.LoginService;
import com.models.services.NoteService;

import java.util.ArrayList;
import java.util.Date;

/** *
 *
 * Esta clase se encarga de administrar las peticiones de la vista principal, ver, crear y editar nota.
 *
 * @author Edgar
 *
 * @version 0.1, 28/02/2015
 *
 * */
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

    public boolean addNote(String title, String body, int idFather){
        Note note = new Note(title,body);

        Date dateCreate = new Date();
        Date dateUpdate = new Date();

        note.setCreatedAt(dateCreate);
        note.setUpdatedAt(dateUpdate);

        note.setIdFather(idFather);
        note.setExtId(0);
        note.setFavorite(false);
        note.setStatus(false);
        note.setSyncFlag(false);
        note.setLabel("label");

        return this.noteService.addNote(note);
    }

    public Note findOneById(int id){
        Note note = noteService.findOneById(id);
        return note;
    }

    public ArrayList<Note> findAllNotesSonByIdFather(int id){
        return noteService.findNotesSonByIdFather(id);
    }

    public void updateNote(Note note){
        noteService.updateNote(note);
    }

    public void deleteNote(Note note){
        noteService.deleteNote(note);
    }





    private NoteService noteService;
    private LoginService loginService;
}
