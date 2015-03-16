package com.view;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.controllers.NoteController;
import com.example.usuario.androidadmin.R;
import com.models.Note;

/**
 * Clase que se encarga de listar las notas borradas
 * @author Ramón Díaz
 * @version 0.1 13/03/2015.
 *
 */

public class ListDeletedNotes extends ListNotes {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notes);
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void loadNotes() {
        NoteController noteController = new NoteController(this);
        notes = noteController.getDeletedNotes();
    }
    @Override
    protected void passNote(Note note) {
        Intent intent = new Intent(this,ViewDeletedNote.class);
        intent.putExtra("id", note.getId());
        startActivity(intent);
    }
}
