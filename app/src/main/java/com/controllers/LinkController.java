package com.controllers;

import android.content.Context;

import com.models.Link;
import com.models.Note;
import com.models.services.LinkService;

import java.util.ArrayList;

/**
 * Created by JoseRamon on 20/04/2015.
 * Clase encargada de realizar las operaciones con los links
 */
public class LinkController {

    public LinkController(Context context) {
        linkService = new LinkService(context);
    }

    public void addLink(Note linkedNote, Note note){
        Link link = new Link();
        link.setLinkedNoteId(linkedNote.getId());
        link.setNoteId(note.getId());
        linkService.addLink(link);
    }

    public ArrayList<Link> getLinksFromNoteId(int noteId){
        return linkService.getLinksFromNoteId(noteId);
    }

    public void deleteLink(Link link){
        linkService.deleteLink(link);
    }

    private LinkService linkService;
}


