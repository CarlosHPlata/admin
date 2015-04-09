package com.view;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.controllers.NoteController;
import com.controllers.TagController;
import com.example.usuario.androidadmin.R;
import com.models.Tag;

import java.util.ArrayList;

public class NewNote extends Fragment {
public View viewNewNote;
    public static NewNote newInstance(Bundle arguments){
        NewNote newNote = new NewNote();
        if(arguments != null){
            newNote.setArguments(arguments);
        }
        return newNote;
    }

    public NewNote(){

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true); //Indicamos que este Fragment tiene su propio menu de opciones
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewNewNote = inflater.inflate(R.layout.activity_new_note, container, false);

        indexTagSelect = new ArrayList();
        controller = new NoteController(getActivity().getApplicationContext());
        tagController = new TagController(getActivity().getApplicationContext());
        viewTags = (TextView) viewNewNote.findViewById(R.id.newTags);
        tagsSelect = new ArrayList<>();

        Button createNote = (Button) viewNewNote.findViewById(R.id.btnCreateNote);
        createNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNote();
            }
        });

       // Bundle bundle = getIntent().getExtras();
        Bundle bundle = getArguments();
        if(bundle != null){
            this.ID_FATHER = bundle.getInt("idFather");
        }
        return viewNewNote;
    }

   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void addNote(){
        EditText textTitle = (EditText) viewNewNote.findViewById(R.id.titleEdit);
        EditText textBody = (EditText) viewNewNote.findViewById(R.id.body);
        String body = textBody.getText().toString();
        String title = textTitle.getText().toString();

        if(controller.addNote(title, body, this.ID_FATHER, tagsSelect)){
            Toast.makeText(getActivity(), "Nota creada", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getActivity(), "Error!!", Toast.LENGTH_LONG).show();
        }
        backView();
    }

    public void listAllTags(){
        final ArrayList indexAux = new ArrayList();
        final ArrayList indexDeleteAux = new ArrayList();
        dialogNewTag = new AlertDialog.Builder(getActivity());
        final EditText txtInput = new EditText(getActivity());
        allTags = tagController.fingAll();
        labelTags = "Tags:\n";
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        final String[] nameTags = new String[allTags.size()];
        final boolean[] allSelected = new boolean[allTags.size()];
        for (int i=0; i<allTags.size(); i++){
            Tag tagAux = (Tag) allTags.get(i);
            if(tagsSelect.size() != 0){
                boolean isSelected = false;
                for (int y=0; y<tagsSelect.size();y++){
                    Tag tagTwoAux = (Tag) tagsSelect.get(y);
                    if(tagAux.getId() == tagTwoAux.getId()){
                        isSelected = true;
                        break;
                    }
                }
                if(isSelected){
                    allSelected[i] = true;
                }else{
                    allSelected[i] = false;
                }
            }else{
                allSelected[i] = false;
            }
            nameTags[i] = tagAux.getName();
        }
        dialogBuilder.setTitle("Seleccione los tags");
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
        dialogBuilder.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {

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
                    labelTags += nameTags[(Integer) indexTagSelect.get(x)] + ", ";
                    tagsSelect.add((Tag) allTags.get((Integer) indexTagSelect.get(x)));
                }
                viewTags.setText(labelTags);
            }
        });
        dialogBuilder.setNegativeButton("Cancelar", null);
        dialogBuilder.setNeutralButton("Nuevo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogNewTag.setTitle("Nuevo tag");
                dialogNewTag.setMessage("Agregar el nombre del nuevo tag");
                dialogNewTag.setView(txtInput);
                dialogNewTag.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       String nameTag = txtInput.getText().toString();
                        if(nameTag != ""){
                            tagController.addTag(nameTag);
                            listAllTags();
                        }
                    }
                });
                dialogNewTag.setNegativeButton("Cancelar", null);
                AlertDialog dialogTag = dialogNewTag.create();
                dialogTag.show();
            }
        });
        AlertDialog dialogT = dialogBuilder.create();
        dialogT.show();
    }

    public void backView(){
        Fragment fragment = new ListNotes();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, fragment).commit();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_new_note, menu);
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
        if (id == R.id.action_new_tag) {
            listAllTags();
        }

        return super.onOptionsItemSelected(item);
    }
    private ArrayList allTags; //son todos los tags de la BD
    private ArrayList indexTagSelect; //son todos los index de los tags
    private TextView viewTags; // es el elemento de la vista para colocar el label de tags
    private  String  labelTags; // es el label de todos los tags separados por comas
  //  private AlertDialog.Builder dialogBuilder;
    private AlertDialog.Builder dialogNewTag;
    private ArrayList tagsSelect; // son todos los objectos de tagsSeleccionados
    private TagController tagController;
    private NoteController controller;
    private int ID_FATHER = 0;
}
