package com.models.services;

import android.content.Context;
import android.util.Log;

import com.models.Tag;
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

    public ArrayList findAll(){
        return tagMapper.findAlldTags();
    }

    public ArrayList<Tag> convertJsonToObjTags(String jsonArray){
        ArrayList<Tag> tags = new ArrayList<>();
        try {
            JSONArray jsonA = new JSONArray(jsonArray);
            if(jsonA != null){
                for (int i=0; i<jsonA.length(); i++){
                    JSONObject tagAux = (JSONObject) jsonA.get(i);
                    Tag tag = new Tag();
                    tag.setId((Integer) tagAux.get("id"));
                    tag.setExtId((Integer)tagAux.get("ext_id"));
                    if(Boolean.getBoolean(tagAux.get("sync_flag").toString())){
                        tag.setSyncFlag(false);
                    }else{
                        tag.setSyncFlag(true);
                    }
                    tag.setName((String) tagAux.get("name"));
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
}
