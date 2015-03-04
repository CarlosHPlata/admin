package com.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.controller.NoteController;
import com.example.usuario.androidadmin.R;
import com.model.Note;
import com.model.StableArrayAdapter;

import java.util.ArrayList;

/**
 *  Created by José Ramón Díaz on 13/02/2015.
 *  Vista que permite mostrar una lista de notas
 */

public class ListNotes extends ActionBarActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notes);
        loadNotes();
        showNotes();
    }

    private void showNotes() {
        final ListView listview = (ListView) findViewById(R.id.listView);
        final ArrayList<String> list = getNotesTitles();
        final StableArrayAdapter adapter = new StableArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                passNote(notes.get(position));
            }

        });
    }

    private void passNote(Note note) {
        Intent intent = new Intent(OpenNote.class.getName());
        intent.putExtra(NOTE, note);
        startActivity(intent);
    }

    private ArrayList<String> getNotesTitles(){
        ArrayList<String> titles = new ArrayList<>();
        for(Note note: notes){
            titles.add(note.getTitle());
        }
        return titles;
    }

    private void loadNotes() {
        NoteController noteController = new NoteController(this);
        notes = noteController.getNotDeletedNotes();
    }

    private static final String NOTE = "note";
    private ArrayList<Note> notes;
}