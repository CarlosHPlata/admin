package com.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.controllers.NoteController;
import com.example.usuario.androidadmin.R;
import com.models.Note;
import com.models.StableArrayAdapter;
import com.models.mappers.NoteMapper;

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_logOut) {
            controller.logOut();
            this.finish();
        }
        if (id == R.id.action_newNote) {
            Intent i = new Intent(this,NewNote.class);
            startActivity(i);
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notes);
        loadNotes();
        showNotes();
        controller = new NoteController(getApplicationContext());
    }

    protected void passNote(Note note) {
        Intent intent = new Intent(this,ViewNote.class);
        intent.putExtra("id", note.getId());
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
        showNotes();
    }

    protected void loadNotes() {

        NoteController noteController = new NoteController(this);
        notes = noteController.getNotDeletedNotes();

    }

    private ArrayList<String> getNotesTitles(){
        ArrayList<String> titles = new ArrayList<>();
        for(Note note: notes){
            titles.add(note.getTitle());
        }
        return titles;
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

    private static final String NOTE = "note";
    protected ArrayList<Note> notes;
    private NoteController controller;
}