package com.view;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.controllers.CheckListController;
import com.controllers.NoteController;
import com.controllers.TagController;
import com.example.usuario.androidadmin.R;
import com.models.CheckList;
import com.models.Note;
import com.models.StableArrayAdapter;
import com.models.Tag;

import java.util.ArrayList;

public class ViewNote extends Fragment {
    public View viewNote;

    public static ViewNote newInstance(Bundle arguments) {
        ViewNote viewNote = new ViewNote();
        if (arguments != null) {
            viewNote.setArguments(arguments);
        }
        return viewNote;
    }

    public ViewNote() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true); //Indicamos que este Fragment tiene su propio menu de opciones
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewNote = inflater.inflate(R.layout.activity_view_note, container, false);

        controller = new NoteController(getActivity().getApplicationContext());
        checkListController = new CheckListController(getActivity().getApplicationContext());
        listNoteSon = (ListView) viewNote.findViewById(R.id.listViewnoteSon);
        listNoteSon.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.ID_NOTE = bundle.getInt("id");
            initViewNoteById();
        }
        return viewNote;
    }

    public void initViewNoteById() {
        findNoteById();
        findNotesSon();
        generateListViewNotesSon();
        generateNoteFather();
        generateTagSelected();
    }

    public void generateTagSelected() {
        TextView viewTags = (TextView) viewNote.findViewById(R.id.viewTags);
        ArrayList<Tag> tagsSelect = noteFather.getTags();
        String nameTags = "Tags:\n";
        for (int y = 0; y < tagsSelect.size(); y++) {
            Tag tagAux = tagsSelect.get(y);
            nameTags += tagAux.getName() + ", ";
        }
        viewTags.setText(nameTags);
    }

    public void findNotesSon() {
        notesSon = controller.findAllNotesSonByIdFather(this.ID_NOTE);
    }

    public void findNoteById() {
        this.noteFather = controller.findOneById(this.ID_NOTE);
        checkLists = this.noteFather.getCheckLists();
    }

    public void generateNoteFather() {
        TextView titleView = (TextView) viewNote.findViewById(R.id.titleView);
        titleView.setText(this.noteFather.getTitle());

        TextView bodyView = (TextView) viewNote.findViewById(R.id.bodyView);
        bodyView.setText(this.noteFather.getBody());
    }

    public void generateListViewNotesSon() {
        if (!noteFather.hasSons())
            return;
        final ListView listview = (ListView) viewNote.findViewById(R.id.listViewnoteSon);
        final ArrayList<String> list = getNotesTitles(noteFather.getSons());
        final StableArrayAdapter adapter = new StableArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                viewNoteSon(noteFather.getSons().get(position));
            }

        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selection = !selection;
                if (selection) {
                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                            //Evento vacio para que no se abra la nota, en su lugar se debe de marcar
                        }

                    });

                    final StableArrayAdapter adapter = new StableArrayAdapter(getActivity(), android.R.layout.simple_list_item_multiple_choice, list);
                    listview.setAdapter(adapter);
                } else {
                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                            final String item = (String) parent.getItemAtPosition(position);
                            viewNoteSon(noteFather.getSons().get(position));
                        }

                    });

                    final StableArrayAdapter adapter = new StableArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
                    listview.setAdapter(adapter);
                }
                return true;
            }
        });

    }

    public void viewNoteSon(Note note) {

       /* Intent intent = new Intent(ViewNote.class.getName());
        intent.putExtra("id", note.getId());
        startActivity(intent); */
        Bundle arguments = new Bundle();
        arguments.putInt("id", note.getId());
        Fragment fragment = ViewNote.newInstance(arguments);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_view_note, menu);
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
        if (id == R.id.action_newNoteSon) {
            /*Intent i = new Intent(this,NewNote.class);
            i.putExtra("id",this.ID_NOTE);
            i.putExtra("isFather",false);
            startActivity(i);*/
            startNewSonNoteFragment();
        }
        if (id == R.id.action_editNote) {
            Bundle arguments = new Bundle();
            arguments.putInt("id", this.ID_NOTE);

            Fragment fragment = EditNote.newInstance(arguments);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
        }
        if (id == R.id.action_delete) {
            if (selection) {
                deleteSelectedSons();
            } else {
                this.noteFather.setStatus(true);
                controller.deleteNote(this.noteFather);

                Fragment fragment = new ListNotes();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
            }
        }
        if (id == R.id.action_taskList) {
            listAllCheckList();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void listAllCheckList() {
        dialogCheckLists = new AlertDialog.Builder(getActivity());
        final EditText txtInput = new EditText(getActivity());
        checkLists = checkListController.findAllByNoteId(this.ID_NOTE);

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

        StableArrayAdapter adapter = new StableArrayAdapter(getActivity(), android.R.layout.simple_list_item_multiple_choice, list);
        listViewItems.setAdapter(adapter);
        for (int i = 0; i < checkLists.size(); i++) {
            CheckList checkListAux = checkLists.get(i);
            if (checkListAux.isChecked()) {
                listViewItems.setItemChecked(i, true);
            } else {
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
                for (int x = 0; x < checkLists.size(); x++) {
                    CheckList checkList = checkLists.get(x);
                    boolean isCheck = false;
                    for (int i = 0; i < sparse.size(); i++) {
                        if (sparse.valueAt(i)) {
                            //CheckList checkList1 = checkLists.get(sparse.keyAt(i));
                            if (x == sparse.keyAt(i)) {
                                isCheck = true;
                                break;
                            }
                        } else {

                        }
                    }
                    if (isCheck) {
                        checkList.setChecked(true);
                    } else {
                        checkList.setChecked(false);
                    }
                    checkListController.updateCheckList(checkList);
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
                        if (description != "" && description.length() > 0) {
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

    private void updateListView() {
        checkLists = checkListController.findAllByNoteId(this.ID_NOTE);
        ArrayList<String> list = getDescriptionOfCheckList();
        StableArrayAdapter adapter = new StableArrayAdapter(getActivity(), android.R.layout.simple_list_item_multiple_choice, list);
        listViewItems.setAdapter(adapter);
        for (int i = 0; i < checkLists.size(); i++) {
            CheckList checkListAux = checkLists.get(i);
            if (checkListAux.isChecked()) {
                listViewItems.setItemChecked(i, true);
            } else {
                listViewItems.setItemChecked(i, false);
            }
        }

    }

    private ArrayList<String> getDescriptionOfCheckList() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < checkLists.size(); i++) {
            CheckList checkListAux = checkLists.get(i);
            list.add(checkListAux.getDescription());
        }
        return list;
    }

    private void deleteCheckList(int position) {
        CheckList checkList = checkLists.get(position);
        checkListController.deleteCheckList(checkList);
    }

    private void newCheckList(String description) {
        checkListController.addCheckList(description, this.ID_NOTE);
    }

    private ArrayList<String> getNotesTitles(ArrayList<Note> notes) {
        ArrayList<String> titles = new ArrayList<>();
        for (Note note : notes) {
            titles.add(note.getTitle());
        }
        return titles;
    }

    private void startNewSonNoteFragment() {
        Bundle arguments = new Bundle();
        arguments.putInt("idFather", noteFather.getId());
        Fragment fragment = NewNote.newInstance(arguments);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
    }

    private void deleteSelectedSons() {
        ArrayList<Note> notesToDelete = getSelectedNotes();
        controller.deleteNotes(notesToDelete);
        Fragment fragment = new ListNotes();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
    }

    private ArrayList<Note> getSelectedNotes() {
        final ListView listview = (ListView) getActivity().findViewById(R.id.listViewnoteSon);
        SparseBooleanArray selected = listview.getCheckedItemPositions();
        if (selected != null && selected.size() > 0) {
            ArrayList<Note> selectedNotes = new ArrayList<>();
            for (int i = 0; i < notesSon.size(); i++) {
                if (selected.get(i)) {
                    selectedNotes.add(notesSon.get(i));
                }
            }
            return selectedNotes;
        }
        return null;
    }

    private int ID_NOTE;
    private NoteController controller;
    private ListView listNoteSon;
    private ArrayList<Note> notesSon;
    private Note noteFather;
    private CheckListController checkListController;
    private AlertDialog.Builder dialogCheckLists;
    private ArrayList<CheckList> checkLists;
    private ListView listViewItems;
    private AlertDialog dialogCheckList;
    private boolean selection = false;
}
