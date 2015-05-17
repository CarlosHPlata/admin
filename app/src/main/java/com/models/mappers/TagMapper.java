package com.models.mappers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.models.Tag;
import com.models.User;

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

    public Tag findOneByExtId(Tag tag){
        Cursor cursor = db.query(tag.getTableName(), tag.getColumnNames(), "ext_id = ?", new String[]{String.valueOf(tag.getExtId())}, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0)
            cursor.moveToFirst();
        else
            return null;

        tag.setContentValues(cursor);

        return tag;
    }

    public void updateTag(Tag tag){
        dbManager.update(tag);
    }

    public void deleteTag(Tag tag){
        //db.delete(tag.getTableName(),"id = ?", new String[]{String.valueOf(tag.getId())});
        dbManager.delete(tag);
    }

    private ArrayList getTagsFromCursor(Cursor cursor){
        ArrayList<Tag> tags = new ArrayList<>();
        while(cursor.moveToNext()){
            Tag tag = new Tag();
            tag.setContentValues(cursor);
            tags.add(tag);
        }
        return tags;
    }

    private DBManager dbManager;
    private SQLiteDatabase db;
}
