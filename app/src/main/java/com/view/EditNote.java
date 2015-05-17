package com.view;

import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.controllers.CheckListController;
import com.controllers.FileOpen;
import com.controllers.NoteController;
import com.controllers.TagController;
import com.example.usuario.androidadmin.R;
import com.models.CheckList;
import com.models.ExpandableListAdapter;
import com.models.File;
import com.models.Fold;
import com.models.Note;
import com.models.StableArrayAdapter;
import com.models.Tag;
import com.models.mappers.FileMapper;
import com.models.services.AlertDialogService;
import com.view.ExpandableLisView.InfoDetailsAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EditNote extends Fragment {
    public View viewEditNote;
    public ExpandableListView expandList;
    public ExpandableListAdapter adapterExpandableListView;
    public List<String> group;
    public List<List<String>> child;
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
        expandList = (ExpandableListView) viewEditNote.findViewById(R.id.expandableListView2);
        viewTags = (TextView) viewEditNote.findViewById(R.id.editTags);
        tagsSelect = new ArrayList<>();
        indexTagSelect = new ArrayList();

        FileMapper fileMapper = new FileMapper(getActivity().getApplicationContext());
        files = fileMapper.findAllFIlesByNote(this.ID_NOTE);
        filesList = (ListView) viewEditNote.findViewById(R.id.edit_nota_files);
        fileOpen =  new FileOpen();

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

        Bundle bundle = getArguments();
        if(bundle != null){
            this.ID_NOTE = bundle.getInt("id");
            initViewEditNote();
            initExpandableListView();
        }

        return viewEditNote;
    }

    public void initExpandableListView(){
        initialDataFold();
        adapterExpandableListView = new ExpandableListAdapter(getActivity(), this.group, this.child);
        expandList.setAdapter(adapterExpandableListView);
        expandList.setGroupIndicator(null);

        expandList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView arg0, View arg1,
                                        int arg2, long arg3) {
                // TODO Auto-generated method stub
                return false;
            }
        });
        expandList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView arg0, View arg1,
                                        int arg2, int arg3, long arg4) {
                // TODO Auto-generated method stub
                editFold(arg2);
                return false;
            }
        });
        expandList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
                                if(positionId >= 0){
                                    deleteFoldToGroup(positionAux);
                                }else{
                                    deleteFoldToGroup(positionAux -1);
                                }
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

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public void generateTagSelected(){
        tagsSelect = note.getTags();
        allTags = tagController.findTagsOfUser();
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
        dialogAlert = new AlertDialog.Builder(getActivity());
        final EditText txtInput = new EditText(getActivity());
        allTags = tagController.findTagsOfUser();
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
                dialogAlert.setTitle("Nuevo tag");
                dialogAlert.setMessage("Agregar el nombre del nuevo tag");
                dialogAlert.setView(txtInput);
                dialogAlert.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nameTag = txtInput.getText().toString();
                        if(nameTag != "" && nameTag.length() >0){
                            tagController.addTag(nameTag);
                            listAllTags();
                        }else{
                            AlertDialogService alert = new AlertDialogService();
                            alert.showAlertDialog(getActivity(), "Error", "Insertar un texto", false);
                        }
                    }
                });
                dialogAlert.setNegativeButton("Cancelar", null);
                AlertDialog dialogTag = dialogAlert.create();
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
        folds = this.note.getFolds();
    }

    public void generateViewEditNote(){
      EditText titleEdit = (EditText) viewEditNote.findViewById(R.id.titleEdit);
      EditText bodyEdit = (EditText) viewEditNote.findViewById(R.id.bodyEdit);

        titleEdit.setText(this.note.getTitle());
        bodyEdit.setText(this.note.getBody());
    }

    public void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm.isAcceptingText()) {

        }else{
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }

    public void updateNote(){
        EditText titleEdit = (EditText) viewEditNote.findViewById(R.id.titleEdit);
        EditText bodyEdit = (EditText) viewEditNote.findViewById(R.id.bodyEdit);
        String body = titleEdit.getText().toString();
        String title = bodyEdit.getText().toString();

        if(body != "" && body.length() > 0 && title != "" && title.length() >0){
            this.note.setTitle(body);
            this.note.setBody(title);
            this.note.setTags(tagsSelect);
            this.note.setCheckLists(checkLists);
            this.note.setFolds(folds);
            controller.updateNote(this.note);
            hideKeyboard();
            backView();
        }else{
            hideKeyboard();
            AlertDialogService alert = new AlertDialogService();
            alert.showAlertDialog(getActivity(), "Error", "Agregue un titulo y un texto a la nota", false);
        }

    }

    public void backView(){
        getActivity().onBackPressed();
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
            hideKeyboard();
            listAllTags();
        }
        if (id == R.id.action_task_list_edit) {
            hideKeyboard();
            listAllCheckList();
        }
        if (id == R.id.action_edit_fold) {
            hideKeyboard();
            showDialogNewFold();
        }
        if(id == R.id.action_save){
            updateNote();
        }

        return super.onOptionsItemSelected(item);
    }

    public void initialDataFold() {
        group = new ArrayList<String>();
        child = new ArrayList<List<String>>();
        for(int x =0; x<folds.size(); x++){
            Fold fold = folds.get(x);
            String content = fold.getContent();
            String groupAux = "";
            if(content.length() > 8){
                groupAux = content.substring(0, 8);
            }else{
                groupAux = content;
            }
            groupAux += "...";
            addInfoFold(groupAux, new String[]{content});
        }

    }

    public void addInfoFold(String p, String[] c) {
        group.add(p);
        List<String> item = new ArrayList<String>();
        for (int i = 0; i < c.length; i++) {
            item.add(c[i]);
        }
        child.add(item);
    }

    public void showDialogNewFold(){
        final EditText txtInput = new EditText(getActivity());
        dialogAlert = new AlertDialog.Builder(getActivity());
        dialogAlert.setTitle("Nuevo pliegue");
        dialogAlert.setMessage("Descripción");
        dialogAlert.setView(txtInput);
        dialogAlert.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String description = txtInput.getText().toString();
                if (description != "" && description.length() > 0) {
                    addFoldToGroup(description);
                }else{
                    AlertDialogService alert = new AlertDialogService();
                    alert.showAlertDialog(getActivity(), "Error", "Insertar un texto", false);
                }
            }
        });
        dialogAlert.setNegativeButton("Cancelar", null);
        AlertDialog dialogTag = dialogAlert.create();
        dialogTag.show();

    }

    public void addFoldToGroup(String content) {
        Fold fold = new Fold(content);
        fold.setSyncFlag(false);
        fold.setNoteId(note.getId());
        fold.setExtId(0);
        folds.add(fold);
        String[] data = { content };
        String group = "";
        if(content.length() > 8){
            group = content.substring(0, 8);
        }else{
            group = content;
        }
        group += "...";
        addInfoFold(group, data);
        adapterExpandableListView.notifyDataSetChanged();
    }

    public void deleteFoldToGroup(int position){
        group.remove(position);
        child.remove(position);
        folds.remove(position);
        adapterExpandableListView.notifyDataSetChanged();
    }

    public void editFold(int position){
        positionToEditFlod = position;
        ArrayList<String> list = (ArrayList<String>) child.get(position);
        String content = list.get(0);
        final EditText txtInput = new EditText(getActivity());
        txtInput.setText(content);
        dialogAlert = new AlertDialog.Builder(getActivity());
        dialogAlert.setTitle("Editar pliegue");
        dialogAlert.setMessage("Descripción");
        dialogAlert.setView(txtInput);
        dialogAlert.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String description = txtInput.getText().toString();
                if (description != "" && description.length() > 0) {
                    updateFold(positionToEditFlod, description);
                }else{
                    AlertDialogService alert = new AlertDialogService();
                    alert.showAlertDialog(getActivity(), "Error", "Insertar un texto", false);
                }
            }
        });
        dialogAlert.setNegativeButton("Cancelar", null);
        AlertDialog dialogTag = dialogAlert.create();
        dialogTag.show();
    }

    private void updateFold(int position, String content){
        Fold fold = folds.get(position);
        fold.setContent(content);
        folds.set(position, fold);
        String[] data = { content };

        String groupAux = "";
        if(content.length() > 8){
            groupAux = content.substring(0, 8);
        }else{
            groupAux = content;
        }
        groupAux += "...";

        group.set(position,groupAux);
        List<String> item = new ArrayList<String>();
        for (int i = 0; i < data.length; i++) {
            item.add(data[i]);
        }
        child.set(position, item);
        adapterExpandableListView.notifyDataSetChanged();
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
                        }else{
                            AlertDialogService alert = new AlertDialogService();
                            alert.showAlertDialog(getActivity(), "Error", "Insertar un texto", false);
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

    private FileOpen fileOpen;
    private int positionToEditFlod = 0;
    private ArrayList allTags; //son todos los tags de la BD
    private ArrayList indexTagSelect; //son todos los index de los tags
    private TextView viewTags; // es el elemento de la vista para colocar el label de tags
    private  String  labelTags; // es el label de todos los tags separados por comas
    //  private AlertDialog.Builder dialogBuilder;
    private AlertDialog.Builder dialogAlert;
    private ArrayList tagsSelect; // son todos los objectos de tagsSeleccionados
    private int ID_NOTE;
    private NoteController controller;
    private TagController tagController;
    private Note note;
    private ArrayList<File> files;

    private CheckListController checkListController;
    private AlertDialog.Builder dialogCheckLists;
    private ArrayList<CheckList> checkLists;
    private ArrayList<Fold> folds;
    private ListView listViewItems;
    private AlertDialog dialogCheckList;
    private StableArrayAdapter adapter;

    private ListView filesList;
}
