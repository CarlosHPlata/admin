package com.model.service;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.usuario.androidadmin.R;
import com.model.mapper.NoteManager;
import com.model.Note;
import com.view.ListNotes;

import java.util.ArrayList;

public class ListNotesTest extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notes_test);
        passNotes();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_notes_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void passNotes() {
        Intent intent = new Intent(ListNotes.class.getName());
        ArrayList<Note> notes = getNotesFromDB();
        intent.putExtra("notes", notes);
        startActivity(intent);
    }

    private ArrayList<Note> generateNotes(){
        ArrayList<Note> notes = new ArrayList<>();
        for(int i = 0; i < 6; i++){
            ArrayList<String> links = new ArrayList<>();
            links.add("link 1."+i);
            links.add("link 2."+i);
            notes.add(new Note("NoteTitle"+i, "NoteContent"+i));
        }
        ArrayList<Note> incrustedNotes = new ArrayList<>();
        incrustedNotes.add(notes.get(1));
        incrustedNotes.add(notes.get(2));
        notes.get(0).setIncrustedNotes(incrustedNotes);
        notes.get(0).setSons(incrustedNotes);
        return notes;
    }

    private ArrayList<Note> getNotesFromDB(){
        NoteManager nm = new NoteManager(this);
        return nm.getAllNotes();
    }
}
