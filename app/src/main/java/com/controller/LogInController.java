package com.controller;

import android.content.Context;

import com.model.service.LoginService;

/**
 * Created by Edgar on 28/02/2015.
 */
public class LogInController {


    public  LogInController(Context context){
        service = new LoginService(context);
    }

    /**
     * Se verifica que los datos existan en la BD y abre una nueva sesion
     * @param email
     * @param password
     * @return boolean
     */
    public boolean logIn(String email, String password){
        if(service.logIn(email, password)){
            service.addUserToSession(email, password);
            return true;
        }else{
            return false;
        }
    }

    /**
     * Elimina datos de usuario de session y manda a la vista de login
     * @return boolean
     */
    public boolean logOut(){
        return service.logOut();
    }

    /**
     * Valida si existe un usuario en session, en caso contrario manda a la vista
     * de login
     * @return boolean
     */
    public boolean validateLogIn(){
        return service.checkLogin();
    }

    /**
     * verifica si existe un session abierta
     * @return boolean
     */
    public boolean isUserLogIn(){
        return service.isUserLoggedIn();
    }

    private LoginService service;
}
