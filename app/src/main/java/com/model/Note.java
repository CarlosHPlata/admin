package com.model;

import android.graphics.drawable.Drawable;
import android.media.Image;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by José Ramón Díaz on 13/02/2015.
 */
public class Note implements Serializable {
    public Note(int id, String title, String content, ArrayList<String> links, ArrayList<Note> incrustedNotes){
        this.id = id;
        this.title = title;
        this.content = content;
        this.links = links;
        this.incrustedNotes = incrustedNotes;
    }

    public Note(int id, String title, String content, ArrayList<String> links){
        this.id = id;
        this.title = title;
        this.content = content;
        this.links = links;
    }

    public boolean hasChilds(){
        return incrustedNotes!=null;
    }

    public boolean hasLinks(){
        return links != null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<String> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<String> links) {
        this.links = links;
    }

    public ArrayList<Note> getIncrustedNotes() {
        return incrustedNotes;
    }

    public void setIncrustedNotes(ArrayList<Note> incrustedNotes) {
        this.incrustedNotes = incrustedNotes;
    }

    private int id;
    private String title;
    private String content;
    private ArrayList<String> links;
    private ArrayList<Note> incrustedNotes;
}
