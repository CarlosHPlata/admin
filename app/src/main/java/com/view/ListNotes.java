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
import com.model.mapper.NoteManager;

import java.util.ArrayList;

public class ListNotes extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notes);
        //loadNotes();
        //showNotes();
        controller = new NoteController(getApplicationContext());
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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

    private ArrayList<String> getNotesTitles(){
        ArrayList<String> titles = new ArrayList<>();
        for(Note note: notes){
            titles.add(note.getTitle());
        }
        return titles;
    }

    private void loadNotes() {
        NoteManager noteManager = new NoteManager(this);
        notes = noteManager.getUndeletedNotes();
    }

    private static final String NOTE = "note";
    private ArrayList<Note> notes;
    private NoteController controller;
}