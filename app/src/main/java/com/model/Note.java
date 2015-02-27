package com.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.usuario.androidadmin.model.Entitie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by José Ramón Díaz on 13/02/2015.
 */
public class Note extends Entitie implements Serializable {
    public static final int NO_INSERTED_NOTE = -1;

    public Note(){
        setTableName(tableName);
        setColumnNames(columNames);
    }
    /*
    public Note(int id, String title, String body, ArrayList<Note> sons, ArrayList<Note> incrustedNotes){
        setId(id);
        this.title = title;
        this.body = body;
        this.sons = sons;
        this.incrustedNotes = incrustedNotes;
    }

    public Note(int id, String title, String body, ArrayList<Note> sons){
        setId(id);
        this.title = title;
        this.body = body;
        this.sons = sons;
    }*/

    public Note(String title, String body){
        super(tableName, columNames);
        this.title = title;
        this.body = body;
        setDate();
    }

    /*public Note(int id, String title, String body, ArrayList<Note> sons, ArrayList<Note> incrustedNotes, String tableName, String[] columNames){
        super(id, tableName, columNames);
        this.title = title;
        this.body = body;
        this.sons = sons;
        this.incrustedNotes = incrustedNotes;
    }*/

    public boolean hasChilds(){
        return incrustedNotes!=null;
    }

    public boolean hasLinks(){
        return sons != null;
    }

    @Override
    public ContentValues getContentValues() {
       // {"id", "title", "body", "favorite", "status", "created_at", "updated_at", "id_father", "ext_id", "label", "sync_flag"};
        ContentValues content = new ContentValues();
        content.put(columNames[1], title);
        content.put(columNames[2], body);
        content.put(columNames[3], favorite);
        content.put(columNames[4], status);
        content.put(columNames[5], createdAt.getTime());
        content.put(columNames[6], updatedAt.getTime());
        content.put(columNames[7], idFather);
        content.put(columNames[8], extId);
        content.put(columNames[9], label);
        content.put(columNames[10], syncFlag);
        return content;
    }

    @Override
    public void setContentValues(Cursor cursor) {
        setId(cursor.getInt(0));
        title = cursor.getString(1);
        body = cursor.getString(2);
        favorite = Boolean.parseBoolean(cursor.getString(3));
        status = Boolean.parseBoolean(cursor.getString(4));
        createdAt = new Date(cursor.getLong(5));
        updatedAt = new Date(cursor.getLong(6));
        idFather = cursor.getInt(7);
        extId = cursor.getInt(8);
        label = cursor.getString(9);
        syncFlag = Boolean.parseBoolean(cursor.getString(10));
        //cursor.close(); al parecer no se puede cerrar el cursor
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
    private ArrayList<Note> sons;//Hay que cambiar todas las vistas donde se mostraban los links a los hijos
    private ArrayList<Note> incrustedNotes;
    private static final String tableName = "notes";
    private static final String[] columNames = {"id", "title", "body", "favorite", "status", "created_at", "updated_at", "id_father", "ext_id", "label", "sync_flag"};
}
