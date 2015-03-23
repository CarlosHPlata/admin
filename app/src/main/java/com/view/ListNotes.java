package com.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.controllers.NoteController;
import com.example.usuario.androidadmin.R;
import com.models.Note;
import com.models.StableArrayAdapter;
import com.models.mappers.NoteMapper;

import java.util.ArrayList;

/**
 *  Created by José Ramón Díaz on 13/02/2015.
 *  Vista que permite mostrar una lista de notas ActionBarActivity
 */

public class ListNotes extends Fragment {
    public ListView listview;
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       // getMenuInflater().inflate(R.menu.menu_list_notes, menu);
        inflater.inflate(R.menu.menu_list_notes, menu);
       // return true;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true); //Indicamos que este Fragment tiene su propio menu de opciones
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_list_notes, container, false);
        //rootView.findViewById();
        listview = (ListView) rootView.findViewById(R.id.listView);

        controller = new NoteController(getActivity().getApplicationContext());
        //  setContentView(R.layout.activity_list_notes);
        loadNotes();
        showNotes();
        return rootView;
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
        if (id == R.id.action_newNote) {
            Fragment fragment = new NewNote();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void passNote(Note note) {
        Bundle arguments = new Bundle();
        arguments.putInt("id",note.getId());
        Fragment fragment = ViewNote.newInstance(arguments);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, fragment).commit();
     /*   Intent intent = new Intent(this,ViewNote.class);
        intent.putExtra("id", note.getId());
        startActivity(intent);*/
    }

/*
    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
        showNotes();
    }
*/
    protected void loadNotes() {
       // NoteController noteController = new NoteController(this);
        notes = controller.getNotDeletedNotes();

    }

    private ArrayList<String> getNotesTitles(){
        ArrayList<String> titles = new ArrayList<>();
        for(Note note: notes){
            titles.add(note.getTitle());
        }
        return titles;
    }

    private void showNotes() {

        //final ListView listview = (ListView) findViewById(R.id.listView);
      //  final ListView listview = (ListView)  getActivity().findViewById(R.id.listView);

        final ArrayList<String> list = getNotesTitles();
        Log.e("ListNotes","Tamaño de list: "+list.get(0));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, list);
      //  final StableArrayAdapter adapter = new StableArrayAdapter( getActivity().getApplicationContext(),android.R.layout.simple_list_item_1, list);
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