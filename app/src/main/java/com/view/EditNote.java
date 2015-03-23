package com.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.controllers.NoteController;
import com.controllers.TagController;
import com.example.usuario.androidadmin.R;
import com.models.Note;
import com.models.Tag;

import java.util.ArrayList;

public class EditNote extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        controller = new NoteController(getApplicationContext());
        tagController = new TagController(getApplicationContext());
        viewTags = (TextView) findViewById(R.id.editTags);
        tagsSelect = new ArrayList<>();
        indexTagSelect = new ArrayList();
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            this.ID_NOTE = bundle.getInt("id");
            initViewEditNote();
        }
    }
    public void generateTagSelected(){
        tagsSelect = note.getTags();
        allTags = tagController.fingAll();
        String nameTags = "Tags:\n";
        for (int y=0; y<tagsSelect.size();y++) {
            Tag tagTwoAux = (Tag) tagsSelect.get(y);
            for (int x=0; x<allTags.size();x++) {
                Tag tagAux = (Tag) allTags.get(x);
                if (tagAux.getId() == tagTwoAux.getId()) {
                    nameTags += tagTwoAux.getName() + ", ";
                    indexTagSelect.add(x);
                }
            }
        }
        viewTags.setText(nameTags);
    }


    public void listAllTags(){
        final ArrayList indexAux = new ArrayList();
        final ArrayList indexDeleteAux = new ArrayList();
        dialogNewTag = new AlertDialog.Builder(this);
        final EditText txtInput = new EditText(this);
        allTags = tagController.fingAll();
        labelTags = "Tags:\n";
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
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


    public void initViewEditNote(){
        findNoteById();
        generateViewEditNote();
        generateTagSelected();
    }

    public void findNoteById(){
        this.note = controller.findOneById(this.ID_NOTE);
    }

    public void generateViewEditNote(){
      EditText titleEdit = (EditText) findViewById(R.id.titleEdit);
      EditText bodyEdit = (EditText) findViewById(R.id.bodyEdit);

        titleEdit.setText(this.note.getTitle());
        bodyEdit.setText(this.note.getBody());
    }

    public void updateNote(View v){
        EditText titleEdit = (EditText) findViewById(R.id.titleEdit);
        EditText bodyEdit = (EditText) findViewById(R.id.bodyEdit);

        this.note.setTitle(titleEdit.getText().toString());
        this.note.setBody(bodyEdit.getText().toString());
        this.note.setTags(tagsSelect);
        controller.updateNote(this.note);
        backView();
    }

    public void backView(){
        super.onBackPressed();
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_note, menu);
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
        if (id == R.id.action_edit_tag) {
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
    private int ID_NOTE;
    private NoteController controller;
    private TagController tagController;
    private Note note;
}
