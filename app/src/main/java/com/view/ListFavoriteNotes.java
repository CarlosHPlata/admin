package com.view;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.controllers.NoteController;
import com.models.Note;
import com.view.items.NoteAdapter;

/**
 * Created by JoseRamon.
 * Clase encargada de desplegar las notas favoritas
 */
public class ListFavoriteNotes extends ListNotes{
    @Override
    protected void loadNotes() {
        NoteController noteController = new NoteController(getActivity().getApplicationContext());
        notes = noteController.getFavoriteNotes();
    }

    protected void showNotes() {
        loadNotes();
        final NoteAdapter adapter = new NoteAdapter(notes, getActivity().getApplicationContext());
        // Log.e("ListNotes","Tamaño de list: "+list.size());
        //**  ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, list);
        //  final StableArrayAdapter adapter = new StableArrayAdapter( getActivity().getApplicationContext(),android.R.layout.simple_list_item_1, list);
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
}
