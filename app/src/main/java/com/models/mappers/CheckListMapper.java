package com.models.mappers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.models.CheckList;

import java.util.ArrayList;


/**
 * Clase que se encarga de guardar y obtener los checklist de una nota
 * Created by Edgar on 11/04/2015.
 */
public class CheckListMapper {
    public CheckListMapper(Context context){
        dbManager = new DBManager(context);
        db = dbManager.getWritableDb();
    }

    public long insertCheckList(CheckList checkList){
        return dbManager.insert(checkList);
    }

    public ArrayList findAllCheckListByNoteId(int noteId){
        ArrayList<CheckList> checkLists = new ArrayList<CheckList>();
        String selectQuery = "SELECT  * FROM checklist WHERE note_id =" + noteId; // Select All Query

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CheckList checkList = new CheckList();
                checkList.setContentValues(cursor);
                // Adding contact to list
                checkLists.add(checkList);
            } while (cursor.moveToNext());
        }

        // return contact list
        return checkLists;
    }

    public void updateCheckList(CheckList checkList){
        dbManager.update(checkList);
    }

    public void deleteCheckList(CheckList checkList){
        dbManager.delete(checkList);
    }



    private DBManager dbManager;
    private SQLiteDatabase db;
}
