package com.controllers;

import android.content.Context;
import android.util.Log;

import com.models.Tag;
import com.models.User;
import com.models.services.LoginService;
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
        loginService = new LoginService(context);
    }

    public ArrayList fingAll(){
        return tagService.findAll();
    }

    public ArrayList findTagsOfUser(){
        return tagService.findTagsOfUser(loginService.getUserOfSession());
    }

    public Tag findOneById(Tag tag){
        return this.tagService.findOneById(tag);
    }


    public boolean addTag(String name){
        Tag tag = new Tag(name);
        tag.setExtId(0);
        tag.setSyncFlag(false);
        tag.setUserId(loginService.getUserOfSession().getId());
        return tagService.add(tag);
    }

    public void updateTag(Tag tag){
        tagService.update(tag);
    }

    public void deleteTag(Tag tag){
        tagService.delete(tag);
    }


    private TagService tagService;
    private LoginService loginService;
}
