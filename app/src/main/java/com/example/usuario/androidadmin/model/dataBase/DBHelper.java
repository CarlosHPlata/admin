package com.example.usuario.androidadmin.model.dataBase;

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
        db.execSQL("CREATE TABLE IF NOT EXISTS notes ( id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, body TEXT, favorite INTEGER, status INTEGER, created_at REAL, updated_at REAL, id_father INTEGER, ext_id INTEGER, label TEXT, sync_flag INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
