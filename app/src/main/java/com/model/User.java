package com.model;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Edgar on 28/02/2015.
 */
public class User extends Entitie {
    @Override
    public ContentValues getContentValues() {
        ContentValues content = new ContentValues();
        content.put("email",this.getEmail());
        content.put("password",this.getPassword());
        content.put("token",this.getToken());
        return content;
    }

    @Override
    public void setContentValues(Cursor cursor) {
        this.setId(cursor.getInt(0));
        this.setEmail(cursor.getString(1));
        this.setPassword(cursor.getString(2));
        this.setToken(cursor.getString(3));
    }

    @Override
    public Entitie getNewInstance() {
        return new User();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private int id;
    private String email;
    private String password;
    private String token;

}
