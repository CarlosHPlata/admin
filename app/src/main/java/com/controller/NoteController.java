package com.controller;

import android.content.Context;

import com.model.Note;
import com.model.service.LoginService;
import com.model.service.NoteService;

/**
 * Created by Edgar on 28/02/2015.
 */
public class NoteController {

    public NoteController(Context context){
        loginService = new LoginService(context);
        noteService = new NoteService();
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


    private NoteService noteService;
    private LoginService loginService;
}
