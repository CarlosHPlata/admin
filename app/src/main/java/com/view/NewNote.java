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
import com.models.Tag;

import java.util.ArrayList;

public class NewNote extends ActionBarActivity {

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        indexTagSelect = new ArrayList();
        controller = new NoteController(getApplicationContext());
        tagController = new TagController(getApplicationContext());
        viewTags = (TextView) findViewById(R.id.newTags);
        tagsSelect = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            this.ID_FATHER = bundle.getInt("idFather");
        }
    }

    public void addNote(View v){
        EditText textTitle = (EditText) findViewById(R.id.titleEdit);
        EditText textBody = (EditText) findViewById(R.id.body);
        String body = textBody.getText().toString();
        String title = textTitle.getText().toString();

        if(controller.addNote(title, body, this.ID_FATHER, tagsSelect)){
            Toast.makeText(this, "Nota creada", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Error!!", Toast.LENGTH_LONG).show();
        }
        backView();

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
                Toast.makeText(getApplicationContext(), which+" Tamaño del array: "+tagsSelect.size(), Toast.LENGTH_SHORT).show();
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
        super.onBackPressed();
        this.finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_note, menu);
        return super.onCreateOptionsMenu(menu);
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
