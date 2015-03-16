package com.controllers;

import android.content.Context;
import android.util.Log;

import com.models.Tag;
import com.models.services.TagService;

import java.util.ArrayList;

/**
 * Created by Edgar on 15/03/2015.
 */
public class TagController {

    public TagController(Context context){
        tagService = new TagService(context);
    }

    public ArrayList fingAll(){
        return tagService.findAll();
    }



    public boolean addTag(String name){
        Log.e("Controller: ", "Si entro");
        Tag tag = new Tag(name);
        tag.setExtId(1);
        tag.setSyncFlag(false);
        return tagService.add(tag);
    }


    private TagService tagService;
}
