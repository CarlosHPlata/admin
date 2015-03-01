package com.model.mapper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.model.User;

/**
 * Created by Edgar on 28/02/2015.
 */
public class UserMapper {
    public UserMapper(Context context){
        dbManager = new DBManager(context);
        db = dbManager.getWritableDb();
    }

    public long insertUser(User user){
        return dbManager.insert(user);
    }

    public User findOneByEmailAndPassword(String email, String password){
        db = dbManager.getReadableDb();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = '"+email+"' AND password = '"+password+"'", null);
        User user = null;
        if(cursor.moveToFirst()) {
            do {
                user  = new User();
                user.setContentValues(cursor);
            } while (cursor.moveToNext());
        }
        return user;

    }

    public User updateUser(User user){
        return null;
    }


    private DBManager dbManager;
    private SQLiteDatabase db;
}
