package com.models;

import android.content.ContentValues;
import android.database.Cursor;

import java.io.Serializable;

/**
 * Created by Usuario on 19/04/2015.
 */
public class File extends Entitie implements Serializable{

    public File() {
        super(TABLE_NAME, COLUMN_NAMES);
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues content = new ContentValues();
        content.put(COLUMN_NAMES[EXT_ID_POSITION], getExt_id());
        content.put(COLUMN_NAMES[PATH_POSITION], getPath());
        content.put(COLUMN_NAMES[NAME_POSITION], getName());
        content.put(COLUMN_NAMES[NOTE_ID_POSITION], getNote_id());
        content.put(COLUMN_NAMES[SYNC_FLAG_POSITION], isSync_flag());
        return content;
    }

    @Override
    public void setContentValues(Cursor cursor) {
        setId(cursor.getInt(ID_POSITION));
        setExt_id(cursor.getInt(EXT_ID_POSITION));
        setPath(cursor.getString(PATH_POSITION));
        setName(cursor.getString(NAME_POSITION));
        setNote_id(cursor.getInt(NOTE_ID_POSITION));
        setSync_flag(Boolean.parseBoolean(cursor.getString(SYNC_FLAG_POSITION)));
    }

    @Override
    public Entitie getNewInstance() {
        return null;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getExt_id() {
        return ext_id;
    }

    public void setExt_id(int ext_id) {
        this.ext_id = ext_id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSync_flag() {
        return sync_flag;
    }

    public void setSync_flag(boolean sync_flag) {
        this.sync_flag = sync_flag;
    }

    public int getNote_id() {
        return note_id;
    }

    public void setNote_id(int note_id) {
        this.note_id = note_id;
    }

    private static final String TABLE_NAME = "files";
    private static final String[] COLUMN_NAMES = {"id", "ext_id", "path", "name", "note_id", "sync_flag"};
    private static final int ID_POSITION = 0;
    private static final int EXT_ID_POSITION = 1;
    private static final int PATH_POSITION = 2;
    private static final int NAME_POSITION = 3;
    private static final int NOTE_ID_POSITION = 4;
    private static final int SYNC_FLAG_POSITION = 5;

    private int id;
    private int ext_id;
    private String path;
    private String name;
    private int note_id;
    private boolean sync_flag;
}
