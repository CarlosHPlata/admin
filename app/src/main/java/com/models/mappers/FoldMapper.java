package com.models.mappers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.models.Fold;

import java.util.ArrayList;

/**
 * Clase que se encarga de guardar y obtener las pliegues de la base de datos
 * Created by Edgar on 20/04/2015.
 */
public class FoldMapper {

    public FoldMapper(Context context){
        dbManager = new DBManager(context);
        db = dbManager.getWritableDb();
    }

    public long insertFold(Fold fold){
        return dbManager.insert(fold);
    }

    public Fold findOneById(Fold fold){
        Fold fold1 = (Fold)dbManager.getById(fold);
        return fold1;
    }

    public ArrayList<Fold> findAllByNoteId(int noteId){
        ArrayList<Fold> folds = new ArrayList<Fold>();
        String selectQuery = "SELECT  * FROM folds WHERE note_id =" + noteId; // Select All Query

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Fold fold = new Fold();
                fold.setContentValues(cursor);
                // Adding contact to list
                folds.add(fold);
            } while (cursor.moveToNext());
        }

        // return contact list
        return folds;

    }

    public void updateFold(Fold fold){
        dbManager.update(fold);
    }

    public void deleteFold(Fold fold){
        dbManager.delete(fold);
    }

    private DBManager dbManager;
    private SQLiteDatabase db;
}
