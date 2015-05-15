package com.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.controllers.LinkController;
import com.controllers.NoteController;
import com.example.usuario.androidadmin.R;
import com.models.Note;
import com.view.items.NoteAdapter;

import java.util.ArrayList;

/**
 * Created by JoseRamon on 21/04/2015.
 * Clase encargada de mostrar las notas que son posibles de linkear o incrustar en otra nota
 */
public class ListNotesToLink extends ListNotes{
    public View listNotesToLink;

    public static ListNotesToLink newInstance(Bundle arguments){
        ListNotesToLink listNotesToLink = new ListNotesToLink();
        if(arguments != null){
            listNotesToLink.setArguments(arguments);
        }
        return listNotesToLink;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        //Colocamos el menu que no tiene botones en la parte de arriba
        inflater.inflate(R.menu.menu_main, menu);
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
        linkController = new LinkController(getActivity().getApplicationContext());
        View rootView = inflater.inflate(R.layout.activity_list_notes, container, false);
        listview = (ListView) rootView.findViewById(R.id.listView);
        searchView = (SearchView) rootView.findViewById(R.id.searchView);

        Bundle bundle = getArguments();
        if(bundle != null){
            this.noteId = bundle.getInt("noteId");
        }
        loadNotes();
        showNotes();
        setSearchEvents();
        return rootView;
    }

    @Override
    protected void loadNotes() {
        NoteController noteController = new NoteController(getActivity().getApplicationContext());
        //Es necesario eliminar la nota que esta incrustando notas para que no se incruste a si misma
        notes = noteController.getNotDeletedNotesButThis(noteId);
    }
    @Override
    protected void passNote(Note note) {
        //Aqui se maneja el evento del clic del usuario
        linkController.addLink(note, controller.findOneById(noteId));
        //Regresa a la nota que agrego un link
        //No se puede iniciar como activity
        Bundle arguments = new Bundle();
        arguments.putInt("id", noteId);
        Fragment fragment = ViewNote.newInstance(arguments);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

    }

    protected void showNotes() {
        final NoteAdapter adapter = new NoteAdapter(notes, getActivity().getApplicationContext());
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note note = (Note) adapter.getItem(position);
                passNote(note);
            }

        });

        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }



    private void initViewListNotesToLink(){
        loadNotes();
    }

    private int noteId;
    private LinkController linkController;
}
