package com.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import com.controllers.LinkController;
import com.controllers.NoteController;
import com.example.usuario.androidadmin.R;
import com.models.Note;

/**
 * Created by JoseRamon on 26/04/2015.
 * Clase encargada de desplegar las notas que el usuario ha buscado
 */
public class ListFoundNotes extends ListNotes{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static ListFoundNotes newInstance(Bundle arguments){
        ListFoundNotes listFoundNotes = new ListFoundNotes();
        if(arguments != null){
            listFoundNotes.setArguments(arguments);
        }
        return listFoundNotes;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true); //Indicamos que este Fragment tiene su propio menu de opciones
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_deleted_notes, menu);
        return true;
	}*/
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        controller = new NoteController(getActivity().getApplicationContext());
        //linkController = new LinkController(getActivity().getApplicationContext());
        View rootView = inflater.inflate(R.layout.activity_list_notes, container, false);
        listview = (ListView) rootView.findViewById(R.id.listView);
        searchView = (SearchView) rootView.findViewById(R.id.searchView);

        Bundle bundle = getArguments();
        if(bundle != null){
            this.query = bundle.getString("query");
        }
        loadNotes();
        showNotes();
        setSearchEvents();
        return rootView;
    }

    @Override
    protected void loadNotes() {
        NoteController noteController = new NoteController(getActivity().getApplicationContext());
        notes = noteController.findNotes(query);
    }

    private String query;
}
