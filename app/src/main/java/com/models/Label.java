package com.models;

/**
 * Created by José Ramón Díaz on 28/02/2015.
 */
public class Label{

    public Label(String title){
       this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private String title;
}
