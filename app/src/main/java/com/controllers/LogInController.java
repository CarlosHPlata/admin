package com.controllers;

import android.content.Context;

import com.models.services.LoginService;

/** *
 *
 * Esta clase se encarga de administrar las peticiones de la vista Login.
 *
 * @author Edgar
 *
 * @version 0.1, 28/02/2015
 *
 * */
public class LogInController {


    public  LogInController(Context context){
        service = new LoginService(context);
    }

    public boolean logIn(String email, String password){
        if(service.logIn(email, password)){
            service.addUserToSession(email, password);
            return true;
        }else{
            return false;
        }
    }

    public boolean logOut(){
        return service.logOut();
    }

    public boolean validateLogIn(){
        return service.checkLogin();
    }

    public boolean isUserLogIn(){
        return service.isUserLoggedIn();
    }

    private LoginService service;
}
