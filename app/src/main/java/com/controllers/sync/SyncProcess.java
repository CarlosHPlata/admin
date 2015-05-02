package com.controllers.sync;

import android.content.Context;

import com.controllers.sync.interfaces.SyncInterface;
import com.models.Note;
import com.models.User;
import com.models.mappers.CheckListMapper;
import com.models.mappers.FileMapper;
import com.models.mappers.LinksMapper;
import com.models.mappers.NoteMapper;
import com.models.mappers.TagMapper;
import com.models.mappers.UserMapper;

import java.util.ArrayList;

/**
 * @(#)JceSecurity.java 1.50 04/04/14
 *
 * Esta clase se encarga de la sincronizacion asincrona
 *
 * @author Carlos Herrera
 *
 * @version 1.50, 014/04/15
 * @since 1.4
 */
public class SyncProcess implements SyncInterface{

    public SyncProcess (Context context, String userToken){
        checkListMapper = new CheckListMapper(context);
        fileMapper = new FileMapper(context);
        linksMapper = new LinksMapper(context);
        noteMapper = new NoteMapper(context);
        tagMapper = new TagMapper(context);
        userMapper = new UserMapper(context);
        this.userToken = userToken;

        notesHandler = new SyncNotesHandler(context, this);
    }

    public void startLoginSync(User user){
        //paso 1 obtener las notas
        noteMapper.dropNotes();
        notesHandler.getNotesFromuser(user.getToken());
    }

    public void startMidSync(){
        //sync tags


        //sync notes
        ArrayList notesForSync = getNotesForSync();
        ArrayList<Note> updatedNotes = (ArrayList<Note>) notesForSync.get(0);
        ArrayList<Note> newNotes = (ArrayList<Note>) notesForSync.get(1);

        for (int i=0; i<newNotes.size(); i++){
            notesHandler.createNote(userToken, newNotes.get(i));
        }

        for (int i=0; i<newNotes.size(); i++){
            notesHandler.updateNote(userToken, updatedNotes.get(i));
        }



    }

    public ArrayList getNotesForSync(){
        ArrayList<Note> notes = noteMapper.getAllNotes();

        ArrayList<Note> updatedNotes = new ArrayList<Note>();
        ArrayList<Note> newNotes = new ArrayList<Note>();

        Note noteTemp;
        for (int i=0; i<notes.size(); i++){
            noteTemp = notes.get(i);
            if(!noteTemp.isSyncFlag()){
                if (noteTemp.getExtId() != 0) updatedNotes.add(noteTemp);
                else newNotes.add(noteTemp);
            }
        }

        ArrayList notesForSync = new ArrayList();
        notesForSync.add(updatedNotes);
        notesForSync.add(newNotes);

        return notesForSync;
    }

    @Override
    public void onResponse(Object response) {

    }

    @Override
    public void onError(int StatusCode, String error) {

    }

    private CheckListMapper checkListMapper;
    private FileMapper fileMapper;
    private LinksMapper linksMapper;
    private NoteMapper noteMapper;
    private TagMapper tagMapper;
    private UserMapper userMapper;
    private String userToken;

    private SyncNotesHandler notesHandler;
}
