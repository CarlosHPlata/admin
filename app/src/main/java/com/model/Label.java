package com.model;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by José Ramón Díaz on 28/02/2015.
 */
public class Label extends Entitie {

    public Label(String title){
        super(tableName, columNames);
        this.title = title;
    }

    public Label(){
        super(tableName, columNames);
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(columNames[TITLE_POSITION], title);
        return contentValues;
    }

    @Override
    public void setContentValues(Cursor cursor) {
        setId(cursor.getInt(ID_POSITION));
        title = cursor.getString(TITLE_POSITION);
    }

    @Override
    public Entitie getNewInstance() {
        return new Label();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;
    private static final String tableName = "labels";
    private static final String[] columNames = {"id","title"};
    private static final int ID_POSITION = 0;
    private static final int TITLE_POSITION = 1;
}