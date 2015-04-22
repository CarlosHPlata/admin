package com.controllers;

import android.content.Context;

import com.models.CheckList;
import com.models.File;
import com.models.Fold;
import com.models.Note;
import com.models.Tag;
import com.models.services.LoginService;
import com.models.services.NoteService;

import java.util.ArrayList;
import java.util.Date;

/** *
 *
 * Esta clase se encarga de administrar las peticiones de la vista principal, ver, crear y editar nota.
 *
 * @author Edgar
 *
 * @version 0.1, 28/02/2015
 *
 * */
public class NoteController {

    public NoteController(Context context){
        loginService = new LoginService(context);
        noteService = new NoteService(context);
    }

    public void setMultipleNotesFather(ArrayList<Note> notes,Note father){
        for(Note note : notes){
            note.setIdFather(father.getId());
            updateNote(note);
        }
    }

    public void deleteNotes(ArrayList<Note> notes){
        noteService.deleteNotes(notes);
    }

    public void restore(Note note){
        noteService.restore(note);
    }

    public boolean logOut(){
        return loginService.logOut();
    }

    public boolean isUserLogIn(){
        return loginService.isUserLoggedIn();
    }

    public boolean addNote(String title, String body, int idFather, ArrayList<Tag> tags, ArrayList<CheckList> checkLists, ArrayList<File> files, ArrayList<Fold> folds){
        Note note = new Note(title,body);

        Date dateCreate = new Date();
        Date dateUpdate = new Date();

        note.setCreatedAt(dateCreate);
        note.setUpdatedAt(dateUpdate);

        note.setIdFather(idFather);
        note.setExtId(0);
        note.setFavorite(false);
        note.setStatus(true);
        note.setSyncFlag(false);
        note.setTags(tags);
        note.setFiles(files);
        note.setCheckLists(checkLists);
        note.setFolds(folds);
        return this.noteService.addNote(note);
    }

    public ArrayList<Note> insertMultipleNotes(ArrayList<Note> notes){
        return noteService.insertMultipleNotes(notes);
    }

    public Note findOneById(int id){
        Note note = noteService.findOneById(id);
        return note;
    }

    public ArrayList<Note> findAllNotesSonByIdFather(int id){
        return noteService.findNotesSonByIdFather(id);
    }

    public ArrayList<Note> findNotesByArrayTags(ArrayList tags){
        ArrayList<Note> notes = noteService.getNotDeletedNotes();
        ArrayList<Note> notesFound = new ArrayList<>();
        for (int i=0; i<notes.size(); i++){
            Note noteAux = notes.get(i);
            ArrayList<Tag> tagsAux = noteAux.getTags();
            boolean isTag = false;
            for (int x =0; x<tagsAux.size(); x++){
                Tag tagAux = tagsAux.get(x);
                for (int y=0; y<tags.size(); y++){
                    Tag tagToFind = (Tag) tags.get(y);
                    if(tagAux.getId() == tagToFind.getId()){
                        isTag = true;
                        break;
                    }
                }
                if(isTag){
                    break;
                }
            }
            if(isTag){
                notesFound.add(noteAux);
            }
        }
        return notesFound;
    }

    public void updateNote(Note note){
        noteService.updateNote(note);
    }

    public ArrayList getAllNotes(){
        return  noteService.getAllNotes();
    }

    public ArrayList getNotDeletedNotes(){
        return noteService.getNotDeletedNotes();
    }

    public ArrayList getDeletedNotes(){
        return noteService.getDeletedNotes();
    }

    public ArrayList getNotesByDate(Date date){
        return noteService.getNotesByDate(date);
    }

    public ArrayList getFavoriteNotes(){
        return noteService.getFavoriteNotes();
    }

    public ArrayList getFatherNotes(){
        return noteService.getFatherNotes();
    }

    public ArrayList getSons(Note note){
        return noteService.getSons(note);
    }

    public void deleteNote(Note note){
        noteService.deleteNote(note);
    }


    private NoteService noteService;
    private LoginService loginService;
}
