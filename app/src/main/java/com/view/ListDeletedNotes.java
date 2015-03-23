package com.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true); //Indicamos que este Fragment tiene su propio menu de opciones
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list_deleted_notes, menu);
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
        NoteController noteController = new NoteController(getActivity().getApplicationContext());
        notes = noteController.getDeletedNotes();
    }
    @Override
    protected void passNote(Note note) {
        Bundle arguments = new Bundle();
        arguments.putInt("id",note.getId());
        Fragment fragment = ViewDeletedNote.newInstance(arguments);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, fragment).commit();
    }

}
