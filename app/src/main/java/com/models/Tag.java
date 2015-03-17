package com.models;

import android.content.ContentValues;
import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by José Ramón Díaz on 28/02/2015.
 * Esta clase es la abstracción del objeto Label
 * Los label son etiquetas que se pueden agregar a las notas
 */
public class Tag extends Entitie {

    public Tag(String name){
        super(tableName, columNames);
        this.name = name;
    }

    public Tag(){
        super(tableName, columNames);
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(columNames[NAME_POSITION], name);
        contentValues.put(columNames[EXT_ID_POSITION], extId);
        contentValues.put(columNames[SYNC_FLAG_POSITION], syncFlag);
        return contentValues;
    }

    @Override
    public void setContentValues(Cursor cursor) {
        setId(cursor.getInt(ID_POSITION));
        extId = cursor.getInt(EXT_ID_POSITION);
        name = cursor.getString(NAME_POSITION);
        syncFlag = Boolean.parseBoolean(cursor.getString(SYNC_FLAG_POSITION));
    }

    @Override
    public Entitie getNewInstance() {
        return new Tag();
    }

    public int getExtId() {
        return extId;
    }

    public void setExtId(int extId) {
        this.extId = extId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSyncFlag() {
        return syncFlag;
    }

    public void setSyncFlag(boolean syncFlag) {
        this.syncFlag = syncFlag;
    }

    public JSONObject toJSON(){
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put(columNames[ID_POSITION], getId());
            jsonObject.put(columNames[EXT_ID_POSITION], getExtId());
            jsonObject.put(columNames[NAME_POSITION], getName());
            jsonObject.put(columNames[SYNC_FLAG_POSITION],isSyncFlag());

            return jsonObject;
        } catch (org.json.JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    private int id;
    private int extId;
    private String name;
    private boolean syncFlag;
    private static final String tableName = "tags";
    private static final String[] columNames = {"id","ext_id","name","sync_flag"};
    private static final int ID_POSITION = 0;
    private static final int EXT_ID_POSITION = 1;
    private static final int NAME_POSITION = 2;
    private static final int SYNC_FLAG_POSITION = 3;
}
