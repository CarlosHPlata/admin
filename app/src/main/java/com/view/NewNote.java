package com.view;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.controllers.FileOpen;
import com.controllers.CheckListController;
import com.controllers.NoteController;
import com.controllers.TagController;
import com.example.usuario.androidadmin.R;
import com.models.CheckList;
import com.models.File;
import com.models.StableArrayAdapter;
import com.models.Tag;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Clase que se encarga de crear nuevas notas en la app,
 * puede agregar y quitar nuevas etiquetas a la nota.
 * @author Edgar
 * @version 0.1 16/02/2015.
 *
 */
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
        checkListController = new CheckListController(getActivity().getApplicationContext());
        checkLists = new ArrayList<>();
        viewTags = (TextView) viewNewNote.findViewById(R.id.newTags);
        tagsSelect = new ArrayList<>();
        files = new ArrayList<>();
        filesList = (ListView) viewNewNote.findViewById(R.id.nueva_nota_files);

        fileOpen =  new FileOpen();

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

        adapter = new StableArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, getArrayStringFiles());
        filesList.setAdapter(adapter);
        filesList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        filesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                // set title
                alertDialogBuilder.setTitle("Eliminar archivo");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Que desea hacer con el archivo?")
                        .setCancelable(true)
                        .setPositiveButton("Eliminar archivo",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                deleteFileFromArray(position);
                            }
                        }).setNegativeButton("Cancelar",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog,int id) {

                            }
                        }).setNeutralButton("Abrir archivo", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                java.io.File myFile = new java.io.File(files.get(position).getPath());
                                try {
                                    FileOpen.openFile(getActivity(), myFile);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Log.i("error", "eerror");
                                }
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.show();
            }
        });

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

        if(controller.addNote(title, body, this.ID_FATHER, tagsSelect, checkLists, files)){
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
        if (id == R.id.action_taskListNew) {
            listAllCheckList();
        }
        if (id == R.id.action_add_file) {
            addFile();
        }


        return super.onOptionsItemSelected(item);
    }


    public void listAllCheckList(){
        dialogCheckLists = new AlertDialog.Builder(getActivity());
        final EditText txtInput = new EditText(getActivity());

        listViewItems = new ListView(getActivity());
        listViewItems.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listViewItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
                final int positionAux = position;
                final long positionId = id;
                PopupMenu popup = new PopupMenu(getActivity(), view);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    //MENU FILA
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_eliminar:
                                deleteCheckList(positionAux);
                                updateListView();
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                    //FIN MENU FILA
                });
                popup.show();
                return true;
            }
        });
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        ArrayList<String> list = getDescriptionOfCheckList();

        StableArrayAdapter adapter = new StableArrayAdapter(getActivity(),android.R.layout.simple_list_item_multiple_choice, list);
        listViewItems.setAdapter(adapter);
        for (int i=0; i<checkLists.size(); i++){
            CheckList checkListAux = checkLists.get(i);
            if(checkListAux.isChecked()){
                listViewItems.setItemChecked(i, true);
            }else{
                listViewItems.setItemChecked(i, false);
            }
        }
        dialogBuilder.setTitle("Lista de tareas");
        dialogBuilder.setView(listViewItems);

        dialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //tagsSelect.clear();
                SparseBooleanArray sparse = listViewItems.getCheckedItemPositions();
                for (int x=0; x<checkLists.size();x++){
                    CheckList checkList = checkLists.get(x);
                    boolean isCheck = false;
                    for(int i=0; i<sparse.size();i++){
                        if(sparse.valueAt(i)){
                            CheckList checkList1 = checkLists.get(sparse.keyAt(i));
                            if(x == sparse.keyAt(i)){
                                isCheck = true;
                                break;
                            }
                        }
                    }
                    if(isCheck){
                        checkList.setChecked(true);
                    }else{
                        checkList.setChecked(false);
                    }
                    checkLists.set(x, checkList);
                    //checkListController.updateCheckList(checkList);
                }

            }
        });
        dialogBuilder.setNegativeButton("Cancelar", null);
        dialogBuilder.setNeutralButton("Nuevo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogCheckLists.setTitle("Nueva tarea");
                dialogCheckLists.setMessage("Descripción");
                dialogCheckLists.setView(txtInput);
                dialogCheckLists.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String description = txtInput.getText().toString();
                        if(description != "" && description.length() >0){
                            newCheckList(description);
                            listAllCheckList();
                        }
                    }
                });
                dialogCheckLists.setNegativeButton("Cancelar", null);
                AlertDialog dialogTag = dialogCheckLists.create();
                dialogTag.show();
            }
        });
        dialogCheckList = dialogBuilder.create();
        dialogCheckList.show();
    }


    private void updateListView(){
        ArrayList<String> list = getDescriptionOfCheckList();
        StableArrayAdapter adapter = new StableArrayAdapter(getActivity(),android.R.layout.simple_list_item_multiple_choice, list);
        listViewItems.setAdapter(adapter);
        for (int i=0; i<checkLists.size(); i++){
            CheckList checkListAux = checkLists.get(i);
            if(checkListAux.isChecked()){
                listViewItems.setItemChecked(i, true);
            }else{
                listViewItems.setItemChecked(i, false);
            }
        }

    }

    private ArrayList<String> getDescriptionOfCheckList(){
        ArrayList<String> list = new ArrayList<>();
        for (int i=0; i<checkLists.size(); i++){
            CheckList checkListAux = checkLists.get(i);
            list.add(checkListAux.getDescription());
        }
        return list;
    }

    private void deleteCheckList(int position){
        checkLists.remove(position);
    }

    private void newCheckList(String description){
       CheckList checkList = new CheckList();
       checkList.setDescription(description);
       checkList.setExtId(0);
       checkList.setChecked(false);
       checkList.setSyncFlag(false);
       checkList.setNoteId(0);

       checkLists.add(checkList);
    }

    private void addFile(){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, fileOpen.RESULT_LOAD_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == fileOpen.RESULT_LOAD_IMAGE && resultCode == getActivity().RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int ColumnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(ColumnIndex);
            cursor.close();

            String filename=picturePath.substring(picturePath.lastIndexOf("/")+1);

            File file = new File();
            file.setId(0);
            file.setNote_id(0);
            file.setExt_id(0);
            file.setSync_flag(false);
            file.setPath(picturePath);
            file.setName(filename);

            files.add(file);

            adapter = new StableArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, getArrayStringFiles());
            filesList.setAdapter(adapter);
            filesList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            adapter.notifyDataSetChanged();

            Toast toast1 =
                    Toast.makeText(getActivity().getApplicationContext(),
                            "archivo agregado", Toast.LENGTH_SHORT);

            toast1.show();
        }
    }

    public ArrayList<String> getArrayStringFiles(){
        ArrayList<String> strings = new ArrayList<String>();
        for (int i =0; i < files.size(); i++){
            strings.add(files.get(i).getName());
        }

        return strings;
    }

    public void deleteFileFromArray(int index){
        ArrayList<File> tempFiles = new ArrayList<File>();

        for (int i =0; i < files.size(); i++){
            if (i != index){
                tempFiles.add(files.get(i));
            }
        }

        files = tempFiles;

        adapter = new StableArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, getArrayStringFiles());
        filesList.setAdapter(adapter);
        filesList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        adapter.notifyDataSetChanged();
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

    private CheckListController checkListController;
    private AlertDialog.Builder dialogCheckLists;
    private ArrayList<CheckList> checkLists;
    private ArrayList<File> files;
    private ListView listViewItems;
    private AlertDialog dialogCheckList;

    private ListView filesList;

    private FileOpen fileOpen;
    private StableArrayAdapter adapter;
}
