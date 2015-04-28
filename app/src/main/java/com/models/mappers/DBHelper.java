package com.models.mappers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Esta clase es una clase que administra la creacion de la base de datos y sus procesos.
 *
 * @author Carlos Herrera
 *
 * @version 1.50, 15/02/2015
 *
 */
public class DBHelper extends SQLiteOpenHelper{

    private static final String DB_NAME  = "ideon.sqlite";
    private static final int DB_VERSION  = 1;

    public DBHelper (Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    /*
    * Hay que agregar las sentencias create, para cada tabla, cuando creen el modelo.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS notes ( id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, body TEXT, favorite INTEGER, status INTEGER, created_at REAL, updated_at REAL, id_father INTEGER, ext_id INTEGER, tag TEXT, sync_flag INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS users ( id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, password TEXT, token INTEGER)");
        db.execSQL("INSERT INTO users(email, password,token) values ('usuario','usuario',1)");
        db.execSQL("CREATE TABLE IF NOT EXISTS tags ( id INTEGER PRIMARY KEY AUTOINCREMENT, ext_id INTEGER, name TEXT, sync_flag INTEGER)");
        db.execSQL("INSERT INTO tags(ext_id, name,sync_flag) values (0,'Java',0)");
        db.execSQL("INSERT INTO tags(ext_id, name,sync_flag) values (0,'C++',0)");
        db.execSQL("INSERT INTO tags(ext_id, name,sync_flag) values (0,'Php',0)");
        db.execSQL("INSERT INTO tags(ext_id, name,sync_flag) values (0,'Ruby',0)");
        db.execSQL("INSERT INTO tags(ext_id, name,sync_flag) values (0,'Objective c',0)");
        //checklist_items
        db.execSQL("CREATE TABLE IF NOT EXISTS checklist ( id INTEGER PRIMARY KEY AUTOINCREMENT, ext_id INTEGER, description TEXT, checked INTEGER, note_id INTEGER, sync_flag INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS folds ( id INTEGER PRIMARY KEY AUTOINCREMENT, ext_id INTEGER, content TEXT, note_id INTEGER, sync_flag INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS files ( id INTEGER PRIMARY KEY AUTOINCREMENT, ext_id INTEGER, path TEXT, name TEXT, note_id INTEGER, sync_flag INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS links ( id INTEGER PRIMARY KEY AUTOINCREMENT, note_id INTEGER, linked_note_id INTEGER, sync_flag INTEGER, ext_id INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST users");
        db.execSQL("CREATE TABLE IF NOT EXISTS users ( id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, password TEXT, token INTEGER)");
        db.execSQL("DROP TABLE IF EXIST tags");
        db.execSQL("CREATE TABLE IF NOT EXISTS tags ( id INTEGER PRIMARY KEY AUTOINCREMENT, ext_id INTEGER, name TEXT, sync_flag INTEGER)");
    }
}
