package com.models.services;

import android.content.Context;

import com.models.Link;
import com.models.Note;
import com.models.mappers.LinksMapper;

import java.util.ArrayList;

/**
 * Created by JoseRamon on 20/04/2015.
 * Clase encargada de comunicar el LinkController y LinkMapper
 */
public class LinkService {
    public LinkService(Context context){
        linksMapper = new LinksMapper(context);
    }

    public void addLink(Link link){
        linksMapper.addLink(link);
    }

    public void deleteLink(Link link){
        linksMapper.deleteLink(link);
    }

    public ArrayList<Link> getLinksFromNoteId(int noteId) {
        return linksMapper.getLinksFromNoteId(noteId);
    }

    private LinksMapper linksMapper;
}
