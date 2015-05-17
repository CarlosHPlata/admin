package com.models;

import android.content.ContentValues;
import android.database.Cursor;

/** *
 *
 * Esta clase contiene el modelo usuario, es la abstraccion de un usario
 * que accede a la aplicación.
 *
 * @author Edgar
 *
 * @version 0.1, 28/02/2015.
 *
 * */
public class User extends Entitie {

    public User(){
        super(tableName, columNames);
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues content = new ContentValues();
        content.put(columNames[EMAIL_POSITION],this.getEmail());
        content.put(columNames[PASSWORD_POSITION],this.getPassword());
        content.put(columNames[TOKEN_POSITION],this.getToken());
        return content;
    }

    @Override
    public void setContentValues(Cursor cursor) {
        this.setId(cursor.getInt(ID_POSITION));
        this.setEmail(cursor.getString(EMAIL_POSITION));
        this.setPassword(cursor.getString(PASSWORD_POSITION));
        this.setToken(cursor.getString(TOKEN_POSITION));
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
    private static final String tableName = "users";
    private static final String[] columNames = { "id", "email", "password", "token"};
    private static final int ID_POSITION = 0;
    private static final int EMAIL_POSITION = 1;
    private static final int PASSWORD_POSITION = 2;
    private static final int TOKEN_POSITION = 3;

}
