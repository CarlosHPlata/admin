package com.models.mappers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.models.Tag;

import java.util.ArrayList;

/**
 * Clase que se encarga de guardar y obtener las tags de la base de datos
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

    public Tag findOneById(Tag tag){
        Tag tagAux = (Tag)dbManager.getById(tag);
        return tagAux;
    }

    public void updateTag(Tag tag){
        dbManager.update(tag);
    }

    public void deleteTag(Tag tag){
        //db.delete(tag.getTableName(),"id = ?", new String[]{String.valueOf(tag.getId())});
        dbManager.delete(tag);
    }

    private DBManager dbManager;
    private SQLiteDatabase db;
}
