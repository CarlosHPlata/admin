package com.models;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Edgar on 11/04/2015.
 * Esta clase es el modelo de los checklist de las notas,
 * los checklist son tareas que puden ser agregados a una nota y verificados.
 */
public class CheckList extends Entitie{

    public CheckList(){
        super(tableName, columNames);
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(columNames[EXT_ID_POSITION], extId);
        contentValues.put(columNames[DESCRIPTION_POSITION], description);
        contentValues.put(columNames[CHECKED_POSITION], checked);
        contentValues.put(columNames[NOTE_ID_POSITION], noteId);
        contentValues.put(columNames[SYNC_FLAG_POSITION], syncFlag);
        return contentValues;
    }

    @Override
    public void setContentValues(Cursor cursor) {
        setId(cursor.getInt(ID_POSITION));
        extId = cursor.getInt(EXT_ID_POSITION);
        description = cursor.getString(DESCRIPTION_POSITION);
        /*if(cursor.getInt(CHECKED_POSITION) == 1){
            checked = true;
        }else{
            checked = false;
        }*/
        checked = (cursor.getInt(CHECKED_POSITION) != 0);
        //checked = Boolean.parseBoolean(cursor.getString(CHECKED_POSITION));
        noteId = cursor.getInt(NOTE_ID_POSITION);
        syncFlag = Boolean.parseBoolean(cursor.getString(SYNC_FLAG_POSITION));
    }

    @Override
    public Entitie getNewInstance() {
        return new CheckList();
    }

    public int getExtId() {
        return extId;
    }

    public void setExtId(int extId) {
        this.extId = extId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
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

    private int extId;
    private String description;
    private boolean checked;
    private int noteId;
    private boolean syncFlag;
    private static final String tableName = "checklist";
    private static final String[] columNames = {"id","ext_id","description", "checked", "note_id", "sync_flag"};
    private static final int ID_POSITION = 0;
    private static final int EXT_ID_POSITION = 1;
    private static final int DESCRIPTION_POSITION = 2;
    private static final int CHECKED_POSITION = 3;
    private static final int NOTE_ID_POSITION = 4;
    private static final int SYNC_FLAG_POSITION = 5;
}
