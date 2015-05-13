package com.view;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.internal.view.menu.MenuView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.controllers.NoteController;
import com.controllers.TagController;
import com.example.usuario.androidadmin.R;
import com.models.Note;
import com.models.StableArrayAdapter;
import com.models.Tag;
import com.models.mappers.NoteMapper;
import com.models.services.TagService;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.view.items.NoteAdapter;

import java.util.ArrayList;

/**
 * Created by José Ramón Díaz on 13/02/2015.
 * Vista que permite mostrar una lista de notas ActionBarActivity
 */

public class   ListNotes extends Fragment {
    public ListView listview;
    private MenuItem deletedItem;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // getMenuInflater().inflate(R.menu.menu_list_notes, menu);
        inflater.inflate(R.menu.menu_list_notes, menu);
        MenuItem itemdeleted = menu.findItem(R.id.action_delete);
        itemdeleted.setVisible(false);
        this.deletedItem = itemdeleted;

      /*  MenuItem item = menu.findItem(R.id.action_newNote);
        item.setVisible(false);

        item = menu.findItem(R.id.action_findByFilter);
        item.setVisible(false);

        item = menu.findItem(R.id.action_move_note);
        item.setVisible(false); */
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
        searchView = (SearchView) rootView.findViewById(R.id.searchView);

        controller = new NoteController(getActivity().getApplicationContext());
        tagController = new TagController(getActivity().getApplicationContext());
        indexTagSelect = new ArrayList();
        tagsSelect = new ArrayList<>();
        //  setContentView(R.layout.activity_list_notes);
        loadNotes();
        showNotes();
        setSearchEvents();

        createLolipopMenu();

        return rootView;
    }

    public void createLolipopMenu(){
        //creating floating menu

        if (((MainActivity )getActivity()).actionMenu != null){
            ((MainActivity )getActivity()).actionMenu.close(true);
        }

        ImageView icon = new ImageView(getActivity()); // Create an icon
        icon.setImageDrawable(getResources().getDrawable(R.drawable.icon_app));

        ((MainActivity )getActivity()).actionButton= new FloatingActionButton.Builder(getActivity())
                .setContentView(icon)
                .setBackgroundDrawable(getResources().getDrawable(R.drawable.lolipop_floating_buttom))
                .build();

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(getActivity());
// repeat many times:
        ImageView itemIcon = new ImageView(getActivity());
        itemIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_new_note));
        SubActionButton button1 = itemBuilder.setContentView(itemIcon).setBackgroundDrawable(getResources().getDrawable(R.drawable.lolipop_floating_buttom)).build();

        itemIcon = new ImageView(getActivity());
        itemIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_filter));
        SubActionButton button2 = itemBuilder.setContentView(itemIcon).setBackgroundDrawable(getResources().getDrawable(R.drawable.lolipop_floating_buttom)).build();


        ((MainActivity )getActivity()).actionMenu = new FloatingActionMenu.Builder(getActivity())
                .addSubActionView(button1)
                .addSubActionView(button2)
                .attachTo(((MainActivity) getActivity()).actionButton)
                .build();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new NewNote();

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listAllTags();
            }
        });
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
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        if (id == R.id.action_findByFilter) {
            listAllTags();
        }
       /* if (id == R.id.action_move_note) {
            //Aqui se manejaria el mover las notas
            if (selection) {
                ArrayList<Note> notesToMove = getSelectedNotes();
                showPosibleFathers(notesToMove);

            }

        }*/
        if (id == R.id.action_delete) {
            deleteSelectedNotes();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void passNote(Note note) {
        Bundle arguments = new Bundle();
        arguments.putInt("id", note.getId());
        Fragment fragment = ViewNote.newInstance(arguments);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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
        notes = controller.getFatherNotes();
    }

    private ArrayList<String> getNotesTitles() {
        ArrayList<String> titles = new ArrayList<>();
        for (Note note : notes) {
            titles.add(note.getTitle());
        }
        return titles;
    }

    protected void showNotes() {
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
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selection = !selection;
                if (selection) {
                    setSelectionView();
                } else {
                    removeSelectionView();
                }

                return true;
            }
        });
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }

    //Setea los eventos para la busqueda de notas
    protected void setSearchEvents(){

        //*** setOnQueryTextListener ***
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub

                //Toast.makeText(getActivity().getApplicationContext(), query,Toast.LENGTH_SHORT).show();
                //Se llama a un fragment para que haga la busqueda y despliegue el resultado
                Bundle arguments = new Bundle();
                arguments.putString("query", query);
                Fragment fragment = ListFoundNotes.newInstance(arguments);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, fragment).commit();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void deleteSelectedNotes() {

        ArrayList<Note> notesToDelete = getSelectedNotes();
        controller.deleteNotes(notesToDelete);
        Fragment fragment = new ListNotes();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, fragment).commit();

            /*Intent i = new Intent(this, ListNotes.class);
            startActivity(i);
            this.finish();*/

    }

    private void listAllTags() {
        final ArrayList indexAux = new ArrayList();
        final ArrayList indexDeleteAux = new ArrayList();
        dialogNewTag = new AlertDialog.Builder(getActivity());
        // final EditText txtInput = new EditText(getActivity());
        allTags = tagController.fingAll();
        // labelTags = "Tags:\n";
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        final String[] nameTags = new String[allTags.size()];
        final boolean[] allSelected = new boolean[allTags.size()];
        for (int i = 0; i < allTags.size(); i++) {
            Tag tagAux = (Tag) allTags.get(i);
            if (tagsSelect.size() != 0) {
                boolean isSelected = false;
                for (int y = 0; y < tagsSelect.size(); y++) {
                    Tag tagTwoAux = (Tag) tagsSelect.get(y);
                    if (tagAux.getId() == tagTwoAux.getId()) {
                        isSelected = true;
                        break;
                    }
                }
                if (isSelected) {
                    allSelected[i] = true;
                } else {
                    allSelected[i] = false;
                }
            } else {
                allSelected[i] = false;
            }
            nameTags[i] = tagAux.getName();
        }
        dialogBuilder.setTitle("Buscar por tags");
        dialogBuilder.setMultiChoiceItems(nameTags, allSelected, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    indexAux.add(which);
                    indexDeleteAux.remove(Integer.valueOf(which));
                } else {
                    indexAux.remove(Integer.valueOf(which));
                    indexDeleteAux.add(which);
                }
                //  Toast.makeText(getApplicationContext(), which+" Tamaño del array: "+tagsSelect.size(), Toast.LENGTH_SHORT).show();
            }
        });
        dialogBuilder.setPositiveButton("Buscar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                tagsSelect.clear();
                for (int x = 0; x < indexAux.size(); x++) {
                    indexTagSelect.add(indexAux.get(x));
                }
                for (int x = 0; x < indexDeleteAux.size(); x++) {
                    indexTagSelect.remove(Integer.valueOf((int) indexDeleteAux.get(x)));
                }
                for (int x = 0; x < indexTagSelect.size(); x++) {
                    // labelTags += nameTags[(Integer) indexTagSelect.get(x)] + ", ";
                    tagsSelect.add((Tag) allTags.get((Integer) indexTagSelect.get(x)));
                }
                if (tagsSelect.size() == 0) {
                    loadNotes();
                    showNotes();
                } else {
                    findNotesByTags();
                    showNotes();
                }
                //   viewTags.setText(labelTags);
            }
        });
        AlertDialog dialogT = dialogBuilder.create();
        dialogT.show();
    }

    private void findNotesByTags() {
        notes.clear();
        notes = controller.findNotesByArrayTags(tagsSelect);
    }

    private void setSelectionView() {
        deletedItem.setVisible(true);
        StableArrayAdapter adapter = new StableArrayAdapter(getActivity(), R.layout.item_note_checked, getNotesTitles());
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
            }

        });
    }

    private void removeSelectionView() {
        deletedItem.setVisible(false);
        showNotes();
    }

    private ArrayList<Note> getSelectedNotes() {
        final ListView listview = (ListView) getActivity().findViewById(R.id.listView);
        SparseBooleanArray selected = listview.getCheckedItemPositions();
        if (selected != null && selected.size() > 0) {
            ArrayList<Note> selectedNotes = new ArrayList<>();
            for (int i = 0; i < notes.size(); i++) {
                if (selected.get(i)) {
                    selectedNotes.add(notes.get(i));
                }
            }
            return selectedNotes;
        }
        return null;
    }

    private void showPosibleFathers(final ArrayList<Note> selectedNotes) {

        for (Note note : selectedNotes) {
            notes.remove(note);
        }
        StableArrayAdapter adapter = new StableArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, getNotesTitles());
        listview.setAdapter(adapter);
        //Cambiar el evento para que se mueva la nota
        //SE TIENE QUE REGRESAR A ABRIR NOTA DESPUES DE QUE SE TERMINE LA OPERACION
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                controller.setMultipleNotesFather(selectedNotes, notes.get(position));

                //Se regresa el evento para que abra las notas al hacer clic
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view,
                                            int position, long id) {
                        final String item = (String) parent.getItemAtPosition(position);
                        passNote(notes.get(position));

                    }

                });
            }

        });
    }

    protected NoteController controller;
    protected ArrayList<Note> notes;
    protected SearchView searchView;
    private AlertDialog.Builder dialogNewTag;
    private ArrayList allTags; //son todos los tags de la BD
    private ArrayList indexTagSelect; //son todos los index de los tags
    // private TextView viewTags; // es el elemento de la vista para colocar el label de tags
    // private  String  labelTags; // es el label de todos los tags separados por comas
    private ArrayList tagsSelect; // son todos los objectos de tagsSeleccionados
    private TagController tagController;
    private boolean selection = false;
    private static final String NOTE = "note";

}