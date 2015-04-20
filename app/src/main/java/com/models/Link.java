package com.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.controllers.NoteController;

/**
 * Created by JoseRamon on 20/04/2015.
 * Esta clase es el modelo de los links que puede contener una nota
 */
public class Link extends Entitie{
    public Link(){
        super(tableName, columNames);
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(columNames[NOTE_ID_POSITION], noteId);
        contentValues.put(columNames[LINKED_NOTE_POSITION], linkedNoteId);
        contentValues.put(columNames[SYNC_FLAG_POSITION], syncFlag);
        return contentValues;
    }

    @Override
    public void setContentValues(Cursor cursor) {
        setId(cursor.getInt(ID_POSITION));
        noteId = cursor.getInt(NOTE_ID_POSITION);
        linkedNoteId = cursor.getInt(LINKED_NOTE_POSITION);
        syncFlag = Boolean.parseBoolean(cursor.getString(SYNC_FLAG_POSITION));
    }

    @Override
    public Entitie getNewInstance() {
        return new Link();
    }

    public boolean isSyncFlag() {
        return syncFlag;
    }

    public void setSyncFlag(boolean syncFlag) {
        this.syncFlag = syncFlag;
    }

    public int getLinkedNoteId() {
        return linkedNoteId;
    }

    public void setLinkedNoteId(int linkedNoteId) {
        this.linkedNoteId = linkedNoteId;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public Note getLinkedNote() {
        return linkedNote;
    }

    public void setLinkedNote(Note linkedNote) {
        this.linkedNote = linkedNote;
    }

    //No se debe sustituir el id del entitie por que causa problemas
    private int noteId;
    private int linkedNoteId;
    private boolean syncFlag;
    private Note linkedNote;
    private static final String tableName = "links";
    private static final String[] columNames = {"id","note_id","linked_note_id", "sync_flag"};
    //Indices para colocar en el vector columNames
    private static final int ID_POSITION = 0;
    private static final int NOTE_ID_POSITION = 1;
    private static final int LINKED_NOTE_POSITION = 2;
    private static final int SYNC_FLAG_POSITION = 3;
}
