package com.models.mappers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.models.Tag;

import java.util.ArrayList;

/**
 * Created by Edgar on 15/03/2015.
 */
public class TagMapper {
    public TagMapper(Context context){
        dbManager = new DBManager(context);
        db = dbManager.getWritableDb();
    }

    public long insertTag(Tag tag){
        return dbManager.insert(tag);
    }

    public ArrayList findAlldTags(){
       ArrayList tags = dbManager.getAll(new Tag());
        return tags;
    }

    private DBManager dbManager;
    private SQLiteDatabase db;
}
