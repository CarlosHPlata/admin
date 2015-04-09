package com.controllers;

import android.content.Context;
import android.util.Log;

import com.models.Tag;
import com.models.services.TagService;

import java.util.ArrayList;

/**
 * Esta clase se encarga de administrar las peticiones de la vista principal, ver, crear y editar tags.
 *
 * Created by Edgar on 15/03/2015.
 */
public class TagController {

    public TagController(Context context){
        tagService = new TagService(context);
    }

    public ArrayList fingAll(){
        return tagService.findAll();
    }

    public Tag findOneById(Tag tag){
        return this.tagService.findOneById(tag);
    }


    public boolean addTag(String name){
        Tag tag = new Tag(name);
        tag.setExtId(1);
        tag.setSyncFlag(false);
        return tagService.add(tag);
    }


    private TagService tagService;
}
