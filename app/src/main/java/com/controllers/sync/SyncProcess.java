package com.controllers.sync;

import android.content.Context;

import com.controllers.sync.interfaces.SyncInterface;
import com.models.CheckList;
import com.models.Note;
import com.models.Tag;
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
        tagsHandler = new SyncTagsHandler(context, this);
        syncChecks = new SyncChecks(context, this);
    }

    public void startLoginSync(User user){
        //paso 1 obtener las notas
        noteMapper.dropNotes();
        notesHandler.getNotesFromuser(user.getToken());
    }

    public void startMidSync(SyncInterface listener){
        //sync tags
        ArrayList tagsForSync = getTagsForSync();
        ArrayList<Tag> updatedTags = (ArrayList<Tag>) tagsForSync.get(0);
        ArrayList<Tag> newTags = (ArrayList<Tag>) tagsForSync.get(1);

        for (int i=0; i<newTags.size(); i++){
            tagsHandler.createTag(userToken, newTags.get(i));
        }

        for (int i=0; i<updatedTags.size(); i++){
            tagsHandler.updateTag(userToken, newTags.get(i));
        }

        //------------------------------------------------------------

        //sync notes
        ArrayList notesForSync = getNotesForSync();
        ArrayList<Note> updatedNotes = (ArrayList<Note>) notesForSync.get(0);
        ArrayList<Note> newNotes = (ArrayList<Note>) notesForSync.get(1);

        Note temp;
        ArrayList<CheckList> cheks;
        for (int i=0; i<newNotes.size(); i++){
            temp = newNotes.get(i);
            notesHandler.createNote(userToken, temp);
            cheks = temp.getCheckLists();
            for (int o=0; o<cheks.size(); o++){
                syncChecks.createCheck(userToken, cheks.get(o));
            }
        }

        for (int i=0; i<updatedNotes.size(); i++){
            temp = updatedNotes.get(i);
            notesHandler.updateNote(userToken, temp);
            cheks = temp.getCheckLists();
            for (int o=0; o<cheks.size(); o++){
                syncChecks.updateCheck(userToken, cheks.get(o));
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {}
            }
        });
        listener.onResponse(true);
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

    public ArrayList<Tag> getTagsForSync(){
        ArrayList<Tag> tags = tagMapper.findAlldTags();

        ArrayList<Tag> updatedTags = new ArrayList<Tag>();
        ArrayList<Tag> newTags = new ArrayList<Tag>();

        Tag tagTemp;
        for (int i = 0; i<tags.size(); i++){
            tagTemp = tags.get(i);
            if (!tagTemp.isSyncFlag()){
                if (tagTemp.getExtId() != 0) updatedTags.add(tagTemp);
                else newTags.add(tagTemp);
            }
        }

        ArrayList tagsForSync = new ArrayList();
        tagsForSync.add(updatedTags);
        tagsForSync.add(newTags);

        return tagsForSync;
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
    private SyncTagsHandler tagsHandler;
    private SyncChecks syncChecks;
}
