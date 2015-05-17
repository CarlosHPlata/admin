package com.models.services;

import android.content.Context;
import android.util.Log;

import com.controllers.NoteController;
import com.models.Note;
import com.models.Tag;
import com.models.User;
import com.models.mappers.TagMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Edgar on 15/03/2015.
 */
public class TagService {

    public TagService(Context context){
        tagMapper = new TagMapper(context);
        noteService = new NoteService(context);
    }
    public TagService(){
    }

    public boolean add(Tag tag){
        long longAux = tagMapper.insertTag(tag);
        if(longAux > 0){
            return true;
        }else{
            return false;
        }
    }

    public void update(Tag tag){
        tagMapper.updateTag(tag);
    }

    public void delete(Tag tag){
        ArrayList<Note> notes = noteService.getAllNotes();

        for(int i=0; i<notes.size(); i++){
            Note note = notes.get(i);
            ArrayList<Tag> tags = note.getTags();
            for (int x=0;x<tags.size();x++){
                Tag tagAux = tags.get(x);
                if(tagAux.getId() == tag.getId()){
                    tags.remove(x);
                    note.setTags(tags);
                    noteService.updateNote(note);
                    break;
                }
            }
        }
        tagMapper.deleteTag(tag);
    }

    public ArrayList findAll(){
        return tagMapper.findAlldTags();
    }

    public Tag findOneById(Tag tag){
        return tagMapper.findOneById(tag);
    }

    public ArrayList<Tag> convertJsonToObjTags(String jsonArray){
        ArrayList<Tag> tags = new ArrayList<>();
        try {
            JSONArray jsonA = new JSONArray(jsonArray);
            if(jsonA != null){
                for (int i=0; i<jsonA.length(); i++){
                    JSONObject tagAux = (JSONObject) jsonA.get(i);
                    Tag tag = new Tag();
                    tag.setId(tagAux.getInt("id"));
                    //Tag tagFound = this.tagMapper.findOneById(tag);
                    /*tag.setExtId((Integer)tagAux.get("ext_id"));
                    if(Boolean.getBoolean(tagAux.get("sync_flag").toString())){
                        tag.setSyncFlag(false);
                    }else{
                        tag.setSyncFlag(true);
                    }
                    tag.setName((String) tagAux.get("name"));*/
                    tags.add(tag);
                }
                return tags;
            }else{
                return tags;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return tags;
        }
    }

    public String convertArrayTagToJson(ArrayList<Tag> tags){
        JSONArray jsonA = new JSONArray();
        for(int x = 0; x<tags.size(); x++){
            Tag tagAux = tags.get(x);
            jsonA.put(tagAux.toJSON());
        }

        return jsonA.toString();
    }

    private TagMapper tagMapper;
    private NoteService noteService;
}
