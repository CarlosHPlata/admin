package com.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.usuario.androidadmin.R;
import com.model.Note;
import com.model.StableArrayAdapter;

import java.util.ArrayList;

/**
 *  Created by José Ramón Díaz on 13/02/2015.
 *  Vista que permite mostrar el contenido de una nota
 */

public class OpenNote extends ActionBarActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_open_note, menu);
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
        setContentView(R.layout.activity_open_note);
        loadNote();
        showNoteData();
    }

    private void loadNote() {
        Intent intent = getIntent();
        note = (Note) intent.getSerializableExtra(NOTE_NAME);
        notes = note.getIncrustedNotes();
    }

    private void showNoteData() {
        showTitle();
        showContent();
        showSons();
        showImg();
        showIncrustedNotes();

    }

    private void showIncrustedNotes() {
        if(!note.hasIncrustedNotes())
            return;
        final ListView listview = (ListView) findViewById(R.id.listIncrustedNotes);
        final ArrayList<String> list = getNotesTitles(notes);
        final StableArrayAdapter adapter = new StableArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                passNote(notes.get(position));
            }

        });
    }

    private void showImg() {
        ImageView imgView = (ImageView) findViewById(R.id.imageView);

        imgView.setImageResource(R.drawable.ic_launcher);
    }

    private void showContent() {
        EditText txtContent = (EditText) findViewById(R.id.txtContent);
        txtContent.setText(note.getBody());
    }

    private void showTitle() {
        TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(note.getTitle());
    }

    private void showSons() {
        if(!note.hasSons())
            return;
        final ListView listview = (ListView) findViewById(R.id.listSonsNotes);
        final ArrayList<String> list = getNotesTitles(note.getSons());
        final StableArrayAdapter adapter = new StableArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                passNote(note.getSons().get(position));
            }

        });
    }

    private void passNote(Note note) {
        //Se crea un intent y se pasa el nombre de la clase
        Intent intent = new Intent(OpenNote.class.getName());
        //Se pone la nota en el intent
        intent.putExtra(NOTE_NAME, note);
        //Se inicia la actividad
        startActivity(intent);
    }

    private ArrayList<String> getNotesTitles(ArrayList<Note> notes){
        ArrayList<String> titles = new ArrayList<>();
        for(Note note: notes){
            titles.add(note.getTitle());
        }
        return titles;
    }

    private ArrayList<Note> notes;
    private static final String NOTE_NAME = "note";
    private Note note;
}
