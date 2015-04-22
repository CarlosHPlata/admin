package com.models;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Esta clase es la abstracción del objeto fold
 * Los objetos fold son despliegues que se pueden agregar a la nota
 * Created by Edgar on 20/04/2015.
 */
public class Fold extends Entitie{

    public Fold(String content){
        super(tableName, columNames);
        this.content = content;
    }

    public Fold(){
        super(tableName, columNames);
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(columNames[CONTENT_POSITION], content);
        contentValues.put(columNames[EXT_ID_POSITION], extId);
        contentValues.put(columNames[NOTE_ID_POSITION], noteId);
        contentValues.put(columNames[SYNC_FLAG_POSITION], syncFlag);
        return contentValues;
    }

    @Override
    public void setContentValues(Cursor cursor) {
        setId(cursor.getInt(ID_POSITION));
        extId = cursor.getInt(EXT_ID_POSITION);
        content = cursor.getString(CONTENT_POSITION);
        noteId = cursor.getInt(NOTE_ID_POSITION);
        syncFlag = (cursor.getInt(SYNC_FLAG_POSITION) != 0);
    }

    @Override
    public Entitie getNewInstance() {
        return new Fold();
    }

    public int getExtId() {
        return extId;
    }

    public void setExtId(int extId) {
        this.extId = extId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public boolean isSyncFlag() {
        return syncFlag;
    }

    public void setSyncFlag(boolean syncFlag) {
        this.syncFlag = syncFlag;
    }

    private int id;
    private int extId;
    private String content;
    private int noteId;
    private boolean syncFlag;
    private static final String tableName = "folds";
    private static final String[] columNames = {"id","ext_id","content", "note_id", "sync_flag"};
    private static final int ID_POSITION = 0;
    private static final int EXT_ID_POSITION = 1;
    private static final int CONTENT_POSITION = 2;
    private static final int NOTE_ID_POSITION = 3;
    private static final int SYNC_FLAG_POSITION = 4;
}
