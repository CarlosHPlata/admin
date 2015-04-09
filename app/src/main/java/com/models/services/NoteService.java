package com.models.services;

import android.content.Context;
import android.util.Log;

import com.models.Note;
import com.models.Tag;
import com.models.mappers.NoteMapper;
import com.models.mappers.TagMapper;

import java.util.ArrayList;
import java.util.Date;

/** *
 *
 * Esta clase contiene la logica de negocio de las notas, se encarga de realizar la conección
 * con los mapper y modelos.
 *
 * @author Edgar
 *
 * @version 0.1, 01/03/2015.
 *
 * */
public class NoteService {

    public NoteService(Context context){
        noteMapper = new NoteMapper(context);
        tagMapper = new TagMapper(context);
    }

    public void deleteNotes(ArrayList notes){
        noteMapper.deleteNotes(notes);
    }

    public void restore(Note note){
        noteMapper.restore(note);
    }
    public boolean addNote(Note note){
        long longAux =  noteMapper.insertNote(note);
        if(longAux > 0){
            return true;
        }else{
            return false;
        }
    }

    public ArrayList<Note> insertMultipleNotes(ArrayList<Note> notes){
        return noteMapper.insertMultipleNotes(notes);
    }

    public Note findOneById(int id){
        Note note = new Note();
        note.setId(id);
        note = (Note) noteMapper.getById(note);
        ArrayList<Tag> tags = note.getTags();
        for(int x=0; x<tags.size();x++){
            Tag tagAux = tags.get(x);
            Tag tag = tagMapper.findOneById(tagAux);
            tags.set(x,tag);
        }
        note.setTags(tags);
        return note;
    }

    public ArrayList getAllNotes(){
        ArrayList<Note> notes =  noteMapper.getAllNotes();
        notes = this.findTagsOfArrayNote(notes);
        return notes;
    }

    public ArrayList getNotDeletedNotes(){
        ArrayList<Note> notes =  noteMapper.getNotDeletedNotes();
        notes = this.findTagsOfArrayNote(notes);
        return notes;
    }

    public ArrayList getDeletedNotes(){
        ArrayList<Note> notes = noteMapper.getDeletedNotes();
        notes = this.findTagsOfArrayNote(notes);
        return notes;
    }

    public ArrayList getNotesByDate(Date date){
        ArrayList<Note> notes = noteMapper.getNotesByDate(date);
        notes = this.findTagsOfArrayNote(notes);
        return notes;
    }

    public ArrayList getFavoriteNotes(){
        ArrayList<Note> notes = noteMapper.getFavoriteNotes();
        notes = this.findTagsOfArrayNote(notes);
        return notes;
    }

    public ArrayList getFatherNotes(){
        ArrayList<Note> notes = noteMapper.getFatherNotes();
        notes = this.findTagsOfArrayNote(notes);
        return notes;
    }

    public ArrayList getSons(Note note){
        ArrayList<Note> notes = noteMapper.getSonsFromDB(note.getId());
        notes = this.findTagsOfArrayNote(notes);
        return notes;
    }

    public void updateNote(Note note){
        noteMapper.updateNote(note);
     }

    public void deleteNote(Note note){
        noteMapper.delteNote(note);
    }

   public ArrayList<Note> findNotesSonByIdFather(int id){
       ArrayList<Note> notes = noteMapper.getSonsFromDB(id);
       notes = this.findTagsOfArrayNote(notes);
       return notes;
    }

    private ArrayList<Note> findTagsOfArrayNote(ArrayList<Note> notes){
        for (int i= 0; i<notes.size(); i++){
            Note note = notes.get(i);
            ArrayList<Tag> tags = note.getTags();
            for(int x=0; x<tags.size();x++){
                Tag tagAux = tags.get(x);
                Tag tag = tagMapper.findOneById(tagAux);
                tags.set(x,tag);
            }
            note.setTags(tags);
            notes.set(i,note);
        }
        return notes;
    }

    private NoteMapper noteMapper;
    private TagMapper tagMapper;

}
