package com.example.usuario.androidadmin.model.dataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.usuario.androidadmin.model.Entitie;

import java.util.ArrayList;

/**
 * Esta clase se encarga de las conexiones a bases de datos, generales, genera una estructura con los metodos basicos, y provee metodos que devuelven base de datos, para crear metodos propios y agenos que cumplan con caracteristicas distintas.
 *
 * @author Carlos Herrera
 *
 * @version 1.50, 17/02/2015
 *
 */
public class DBManager {

    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    public long insert (Entitie entitie) {
        long NO_INSERTED_ENTITIE = -1;
        long id = NO_INSERTED_ENTITIE;

        getWritableDb();

        if (entitie.getId() == NO_INSERTED_ENTITIE){
           id = db.insert(entitie.getTableName(), null, entitie.getContentValues());
           entitie.setId( (int) id );
        }
        return id;
    }

    public ArrayList multipleInsert (ArrayList<Entitie> entities){
        ArrayList ids = new ArrayList();
        long tempId;

        getWritableDb();

        for (int i = 0; i < entities.size(); i++) {
            tempId = this.insert(entities.get(i));
            ids.add(tempId);
        }

        return ids;
    }


    public void update (Entitie entitie) {
        getWritableDb();

        db.update(entitie.getTableName(),entitie.getContentValues(), "id = ?", new String[]{String.valueOf(entitie.getId())});
    }


    public Entitie getById (Entitie dummyEntitie) {
        getReadableDb();

        Cursor cursor = db.query(dummyEntitie.getTableName(), dummyEntitie.getColumnNames(), "id = ?", new String[]{String.valueOf(dummyEntitie.getId())}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        dummyEntitie.setContentValues(cursor);

        return dummyEntitie;
    }

    public ArrayList<Entitie> getAll(Entitie dummyEntitie){
        ArrayList<Entitie> entities = new ArrayList<Entitie>();

        String selectQuery = "SELECT  * FROM " + dummyEntitie.getTableName(); // Select All Query

        getWritableDb();

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                dummyEntitie = dummyEntitie.getNewInstance();
                dummyEntitie.setContentValues(cursor);
                // Adding contact to list
                entities.add(dummyEntitie);
            } while (cursor.moveToNext());
        }

        // return contact list
        return entities;
    }


    public SQLiteDatabase getReadableDb() {
        this.db = helper.getReadableDatabase();
        return this.db;
    }

    public SQLiteDatabase getWritableDb() {
        this.db = helper.getWritableDatabase();
        return this.db;
    }
}
