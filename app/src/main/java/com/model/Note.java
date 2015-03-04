package com.model;

import android.content.ContentValues;
import android.database.Cursor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by José Ramón Díaz on 13/02/2015.
 * Abstracción del objeto Nota
 */
public class Note extends Entitie implements Serializable {
    public static final int NO_INSERTED_NOTE = -1;

    public Note(){
        setTableName(tableName);
        setColumnNames(columNames);
    }

    public Note(String title, String body){
        super(tableName, columNames);
        sons = new ArrayList<>();
        this.title = title;
        this.body = body;
        setDate();
    }

    public boolean hasIncrustedNotes(){
        return incrustedNotes!=null;
    }

    public boolean hasSons(){
        return sons != null;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues content = new ContentValues();
        content.put(columNames[TITLE_POSITION], title);
        content.put(columNames[BODY_POSITION], body);
        content.put(columNames[FAVORITE_POSITION], favorite);
        content.put(columNames[STATUS_POSITION], status);
        content.put(columNames[CREATED_AT_POSITION], createdAt.getTime());
        content.put(columNames[UPDATED_POSITION], updatedAt.getTime());
        content.put(columNames[ID_FATHER_POSITION], idFather);
        content.put(columNames[EXT_ID_POSITION], extId);
        content.put(columNames[LABEL_POSITION], label);
        content.put(columNames[SYNC_FLAG_POSITION], syncFlag);
        return content;
    }

    @Override
    public void setContentValues(Cursor cursor) {
        setId(cursor.getInt(ID_POSITION));
        title = cursor.getString(TITLE_POSITION);
        body = cursor.getString(BODY_POSITION);
        favorite = Boolean.parseBoolean(cursor.getString(FAVORITE_POSITION));
        status = Boolean.parseBoolean(cursor.getString(STATUS_POSITION));
        createdAt = new Date(cursor.getLong(CREATED_AT_POSITION));
        updatedAt = new Date(cursor.getLong(UPDATED_POSITION));
        idFather = cursor.getInt(ID_FATHER_POSITION);
        extId = cursor.getInt(EXT_ID_POSITION);
        label = cursor.getString(LABEL_POSITION);
        syncFlag = Boolean.parseBoolean(cursor.getString(SYNC_FLAG_POSITION));
    }

    @Override
    public Entitie getNewInstance() {
        return new Note();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ArrayList<Note> getSons() {
        return sons;
    }

    public void setSons(ArrayList<Note> sons) {
        this.sons = sons;
    }

    public ArrayList<Note> getIncrustedNotes() {
        return incrustedNotes;
    }

    public void setIncrustedNotes(ArrayList<Note> incrustedNotes) {
        this.incrustedNotes = incrustedNotes;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public int getIdFather() {
        return idFather;
    }

    public void setIdFather(int idFather) {
        this.idFather = idFather;
    }

    public int getExtId() {
        return extId;
    }

    public void setExtId(int extId) {
        this.extId = extId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isSyncFlag() {
        return syncFlag;
    }

    public void setSyncFlag(boolean syncFlag) {
        this.syncFlag = syncFlag;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    private void setDate() {
        if(getId() == NO_INSERTED_NOTE){
            createdAt = new Date();
            updatedAt = new Date();
        }
    }

    private int idFather;
    private int extId;
    private String label;
    private boolean syncFlag;
    private String title;
    private String body;
    private boolean favorite;
    private boolean status;
    private Date createdAt;
    private Date updatedAt;
    private ArrayList<Note> sons;
    private ArrayList<Note> incrustedNotes;
    private static final String tableName = "notes";
    private static final String[] columNames = {"id", "title", "body", "favorite", "status", "created_at", "updated_at", "id_father", "ext_id", "label", "sync_flag"};
    private static final int ID_POSITION = 0;
    private static final int TITLE_POSITION = 1;
    private static final int BODY_POSITION = 2;
    private static final int FAVORITE_POSITION = 3;
    private static final int STATUS_POSITION = 4;
    private static final int CREATED_AT_POSITION = 5;
    private static final int UPDATED_POSITION = 6;
    private static final int ID_FATHER_POSITION = 7;
    private static final int EXT_ID_POSITION = 8;
    private static final int LABEL_POSITION = 9;
    private static final int SYNC_FLAG_POSITION = 10;
}
