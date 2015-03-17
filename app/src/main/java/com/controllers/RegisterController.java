package com.controllers;

import android.content.Context;

import com.models.User;
import com.models.mappers.UserMapper;

/**
 * Created by Usuario on 16/03/2015.
 */
public class RegisterController {

    public RegisterController(Context context){
        this.mapper = new UserMapper(context);
    }

    public void registUser(String email, String password){
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        mapper.insertUser(user);
    }

    public boolean isUserRegistered(String email, String password) {
        User user = mapper.findOneByEmailAndPassword(email, password);
        if (user == null)
            return false;
        else
            return true;
    }

    private UserMapper mapper;
}
