package com.view;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
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

import com.controllers.CheckListController;
import com.controllers.NoteController;
import com.controllers.TagController;
import com.example.usuario.androidadmin.R;
import com.models.CheckList;
import com.models.Note;
import com.models.StableArrayAdapter;
import com.models.Tag;

import java.util.ArrayList;

public class EditNote extends Fragment {
    public View viewEditNote;
    public static EditNote newInstance(Bundle arguments){
        EditNote editNote = new EditNote();
        if(arguments != null){
            editNote.setArguments(arguments);
        }
        return editNote;
    }

    public EditNote(){

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true); //Indicamos que este Fragment tiene su propio menu de opciones
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewEditNote = inflater.inflate(R.layout.activity_edit_note, container, false);

        controller = new NoteController(getActivity().getApplicationContext());
        tagController = new TagController(getActivity().getApplicationContext());
        checkListController = new CheckListController(getActivity().getApplicationContext());
        viewTags = (TextView) viewEditNote.findViewById(R.id.editTags);
        tagsSelect = new ArrayList<>();
        indexTagSelect = new ArrayList();

        Button createNote = (Button) viewEditNote.findViewById(R.id.btnUpdateNote);
        createNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNote();
            }
        });

        Bundle bundle = getArguments();
        if(bundle != null){
            this.ID_NOTE = bundle.getInt("id");
            initViewEditNote();
        }

        return viewEditNote;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        checkLists = this.note.getCheckLists();
    }

    public void generateViewEditNote(){
      EditText titleEdit = (EditText) viewEditNote.findViewById(R.id.titleEdit);
      EditText bodyEdit = (EditText) viewEditNote.findViewById(R.id.bodyEdit);

        titleEdit.setText(this.note.getTitle());
        bodyEdit.setText(this.note.getBody());
    }

    public void updateNote(){
        EditText titleEdit = (EditText) viewEditNote.findViewById(R.id.titleEdit);
        EditText bodyEdit = (EditText) viewEditNote.findViewById(R.id.bodyEdit);

        this.note.setTitle(titleEdit.getText().toString());
        this.note.setBody(bodyEdit.getText().toString());
        this.note.setTags(tagsSelect);
        this.note.setCheckLists(checkLists);
        controller.updateNote(this.note);
        backView();
    }

    public void backView(){
        Bundle arguments = new Bundle();
        arguments.putInt("id",this.ID_NOTE);
        Fragment fragment = ViewNote.newInstance(arguments);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, fragment).commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_edit_note, menu);
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
        if (id == R.id.action_task_list_edit) {
            listAllCheckList();
        }

        return super.onOptionsItemSelected(item);
    }


    public void listAllCheckList(){
        dialogCheckLists = new AlertDialog.Builder(getActivity());
        final EditText txtInput = new EditText(getActivity());
       // checkLists = checkListController.findAllByNoteId(this.ID_NOTE);

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
                            //CheckList checkList1 = checkLists.get(sparse.keyAt(i));
                            if(x == sparse.keyAt(i)){
                                isCheck = true;
                                break;
                            }
                        }else{

                        }
                    }
                    if(isCheck){
                        checkList.setChecked(true);
                    }else{
                        checkList.setChecked(false);
                    }
                    //checkListController.updateCheckList(checkList);
                    checkLists.set(x,checkList);
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
        //checkLists = checkListController.findAllByNoteId(this.ID_NOTE);
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
        //CheckList checkList = checkLists.get(position);
        //checkListController.deleteCheckList(checkList);
    }

    private void newCheckList(String description){
        //checkListController.addCheckList(description, this.ID_NOTE);
        CheckList checkList = new CheckList();
        checkList.setDescription(description);
        checkList.setExtId(0);
        checkList.setChecked(false);
        checkList.setSyncFlag(false);
        checkList.setNoteId(this.ID_NOTE);

        checkLists.add(checkList);
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

    private CheckListController checkListController;
    private AlertDialog.Builder dialogCheckLists;
    private ArrayList<CheckList> checkLists;
    private ListView listViewItems;
    private AlertDialog dialogCheckList;
}
