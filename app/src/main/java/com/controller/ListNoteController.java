package com.controller;

import android.content.Context;

import com.model.service.LoginService;

/**
 * Created by Edgar on 28/02/2015.
 */
public class ListNoteController {

    public ListNoteController(Context context){
        loginService = new LoginService(context);
    }

    public boolean logOut(){
        return loginService.logOut();
    }

    public boolean isUserLogIn(){
        return loginService.isUserLoggedIn();
    }


    private LoginService loginService;
}
