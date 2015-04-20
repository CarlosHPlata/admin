package com.models.mappers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.controllers.NoteController;
import com.models.Link;
import com.models.Note;

import java.util.ArrayList;

/**
 * Clase que se encarga de guardar, obtener y eliminar los links de una nota
 * Created by JoseRamon on 20/04/2015.
 */
public class LinksMapper {

    public LinksMapper(Context context){
        dbManager = new DBManager(context);
        db = dbManager.getWritableDb();
        noteController = new NoteController(context);
    }

    public void addLink(Link link){
        dbManager.insert(link);
    }

    public void deleteLink(Link link){
        dbManager.delete(link);
    }

    public ArrayList<Link> getLinksFromNote(Note note){
        Cursor cursor = db.rawQuery("SELECT * FROM links WHERE note_id = " + note.getId(), null);
        return getLinksFromCursor(cursor);
    }

    private ArrayList<Link> getLinksFromCursor(Cursor cursor){
        ArrayList<Link> links = new ArrayList<>();
        while(cursor.moveToNext()){
            Link link = new Link();
            link.setContentValues(cursor);
            link.setLinkedNote(noteController.findOneById(link.getLinkedNoteId()));
            links.add(link);
        }
        return links;
    }

    private DBManager dbManager;
    private SQLiteDatabase db;
    private NoteController noteController;
}
