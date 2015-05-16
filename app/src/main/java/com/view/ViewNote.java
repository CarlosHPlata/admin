package com.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.controllers.CheckListController;
import com.controllers.LinkController;
import com.controllers.NoteController;
import com.example.usuario.androidadmin.R;
import com.models.CheckList;
import com.models.Fold;
import com.models.Link;
import com.models.Note;
import com.models.StableArrayAdapter;
import com.models.Tag;
import com.models.ExpandableListAdapter;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.ArrayList;
import java.util.List;

/** *
 *
 * Esta clase se encarga de visualizar una nota seleccionada, puede borrar, editar, marcar
 * como favorito, y ver lista de tareas o los checkList.
 *
 * @author Edgar 28/02/2015
 *
 * */
public class ViewNote extends Fragment {
    public View viewNote;
    public List<String> group;
    public List<List<String>> child;
    public ExpandableListView expandList;
    public ExpandableListAdapter adapterExpandableListView;

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
    public void onDestroyView() {
        super.onDestroyView();
        if(listviewLinks.getHeaderViewsCount() > 0){
            listviewLinks.removeAllViews();
        }
        if(listNoteSon.getHeaderViewsCount() > 0){
            listNoteSon.removeAllViews();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewNote = inflater.inflate(R.layout.activity_view_note, container, false);

        linkController = new LinkController(getActivity().getApplicationContext());
        controller = new NoteController(getActivity().getApplicationContext());
        checkListController = new CheckListController(getActivity().getApplicationContext());
        listviewLinks = (ListView) viewNote.findViewById(R.id.listViewLinks);

        listNoteSon = (ListView) viewNote.findViewById(R.id.listViewnoteSon);

        listviewLinks.setFooterDividersEnabled(false);
        listNoteSon.setFooterDividersEnabled(false);

        listNoteSon.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        expandList = (ExpandableListView) viewNote.findViewById(R.id.expandableListView1);
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.ID_NOTE = bundle.getInt("id");
            initViewNoteById();
            initExpandableListView();
        }

        createLolipopMenu();
        return viewNote;
    }

    public void createLolipopMenu(){
        //creating floating menu
        ((MainActivity )getActivity()).actionMenu.close(true);
        ImageView icon = new ImageView(getActivity()); // Create an icon
        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit_note));

        ((MainActivity )getActivity()).actionButton= new FloatingActionButton.Builder(getActivity())
                .setContentView(icon)
                .setBackgroundDrawable(getResources().getDrawable(R.drawable.lolipop_floating_buttom))
                .build();

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(getActivity());
        // repeat many times:

        ImageView itemIcon = new ImageView(getActivity());
        itemIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_link));
        SubActionButton addLink = itemBuilder.setContentView(itemIcon).
                setBackgroundDrawable(getResources().getDrawable(R.drawable.lolipop_floating_buttom))
                .build();

        itemIcon = new ImageView(getActivity());
        itemIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_delete_link));
        SubActionButton deleteLink = itemBuilder.setContentView(itemIcon).
                setBackgroundDrawable(getResources().getDrawable(R.drawable.lolipop_floating_buttom))
                .build();

        itemIcon = new ImageView(getActivity());
        itemIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit_note));
        SubActionButton editNote = itemBuilder.setContentView(itemIcon).
                setBackgroundDrawable(getResources().getDrawable(R.drawable.lolipop_floating_buttom))
                .build();

        itemIcon = new ImageView(getActivity());
        itemIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_new_note));
        SubActionButton addNote = itemBuilder.setContentView(itemIcon).
                setBackgroundDrawable(getResources().getDrawable(R.drawable.lolipop_floating_buttom))
                .build();

        ((MainActivity )getActivity()).actionMenu = new FloatingActionMenu.Builder(getActivity())
                .addSubActionView(addLink)
                .addSubActionView(deleteLink)
                .addSubActionView(editNote)
                .addSubActionView(addNote)
                .attachTo(((MainActivity) getActivity()).actionButton)
                .build();

        addLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                addLink();
            }
        });

        deleteLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                deleteLink();
            }
        });

        editNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                Bundle arguments = new Bundle();
                arguments.putInt("id", ID_NOTE);

                Fragment fragment = EditNote.newInstance(arguments);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                startNewSonNoteFragment();
            }
        });
    }

    public void initExpandableListView(){
        initialDataFold();
        adapterExpandableListView = new ExpandableListAdapter(getActivity(), this.group, this.child);
        expandList.setAdapter(adapterExpandableListView);
        expandList.setGroupIndicator(null);
    }

    public void initViewNoteById() {
        findNoteById();
        findNotesSon();
        generateListViewNotesSon();
        generateListViewLinks();
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
        folds = this.noteFather.getFolds();
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
                viewNote(noteFather.getSons().get(position));
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
                            viewNote(noteFather.getSons().get(position));
                        }

                    });

                    final StableArrayAdapter adapter = new StableArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
                    listview.setAdapter(adapter);
                }
                return true;
            }
        });

    }

    //Carga el listview de links con los links que tenga la nota
    public void generateListViewLinks() {
        if (!noteFather.hasLinks()) {
            Log.v("%%%%%%%%%%%%%%%%%%", "Noooo Tiene links");
            return;
        }
        Log.v("%%%%%%%%%%%%%%%%%%", "Si Tiene links");
     //   final ListView listviewLinks = (ListView) viewNote.findViewById(R.id.listViewLinks);
        listviewLinks.setFooterDividersEnabled(false);
        final ArrayList<String> list = getNotesTitlesFromLinks(noteFather.getLinks());
        final StableArrayAdapter adapter = new StableArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
        listviewLinks.setAdapter(adapter);

        listviewLinks.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                //Probablemente este metodo debe ser refactorizado a viewNote()
                viewNote(controller.findOneById(noteFather.getLinks().get(position).getLinkedNoteId()));
            }

        });
        /*
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
                            viewNote(noteFather.getSons().get(position));
                        }

                    });

                    final StableArrayAdapter adapter = new StableArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
                    listview.setAdapter(adapter);
                }
                return true;
            }
        });*/

    }

    public void viewNote(Note note) {
        Bundle arguments = new Bundle();
        arguments.putInt("id", note.getId());
        Fragment fragment = ViewNote.newInstance(arguments);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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
        if (id == R.id.action_delete) {
            if (selection) {
                deleteSelectedSons();
            } else {
               // this.noteFather.setStatus(true);
                controller.deleteNote(this.noteFather);
                getActivity().onBackPressed();
            }
        }
        if (id == R.id.action_taskList) {
            listAllCheckList();
        }
        if(id == R.id.action_favorite){
            //cambia de nota favorita a no favorita y viceversa
            toggleFavorite();
        }
        return super.onOptionsItemSelected(item);
    }

    public void addLink(){
        Bundle arguments = new Bundle();
        arguments.putInt("noteId", noteFather.getId());
        Fragment fragment = ListNotesToLink.newInstance(arguments);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
    }

    public void deleteLink(){
       // final ListView listview = (ListView) viewNote.findViewById(R.id.listViewLinks);
        //final ArrayList<String> list = getNotesTitles(noteFather.getSons());
        //final StableArrayAdapter adapter = new StableArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
        //listview.setAdapter(adapter);

        listviewLinks.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                //final String item = (String) parent.getItemAtPosition(position);
                //viewNote(noteFather.getSons().get(position));
                //Aqui se elimina el link a la nota clickeada
                linkController.deleteLink(noteFather.getLinks().get(position));
                //Regresa a listar las notas
                Fragment fragment = new ListNotes();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
            }

        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) {
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }

    }

    public void listAllCheckList() {
        dialogCheckLists = new AlertDialog.Builder(getActivity());
        final EditText txtInput = new EditText(getActivity());
        checkLists = checkListController.findAllByNoteId(this.ID_NOTE);

        listViewItems = new ListView(getActivity());
        listViewItems.setFooterDividersEnabled(false);
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

    private ArrayList<String> getNotesTitlesFromLinks(ArrayList<Link> links){
        ArrayList<String> titles = new ArrayList<>();
        for (Link link : links) {
            titles.add(controller.findOneById(link.getLinkedNoteId()).getTitle());
        }
        return titles;
    }

    private void startNewSonNoteFragment() {
        Bundle arguments = new Bundle();
        arguments.putInt("idFather", noteFather.getId());
        Fragment fragment = NewNote.newInstance(arguments);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        /*
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit(); */
    }

    private void deleteSelectedSons() {
        ArrayList<Note> notesToDelete = getSelectedNotes();
        if(notesToDelete != null) {
            controller.deleteNotes(notesToDelete);
            getActivity().onBackPressed();
        }
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

    private void initialDataFold() {
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
            addInfoFold(groupAux, new String[] { content });
        }
    }

    private void addInfoFold(String p, String[] c) {
        group.add(p);
        List<String> item = new ArrayList<String>();
        for (int i = 0; i < c.length; i++) {
            item.add(c[i]);
        }
        child.add(item);
    }

    private void toggleFavorite(){
        noteFather.setFavorite(!noteFather.isFavorite());
        controller.updateNote(noteFather);
    }

    private int ID_NOTE;
    private NoteController controller;
    private ListView listNoteSon;
    private ListView listviewLinks;
    private ArrayList<Note> notesSon;
    private Note noteFather;
    private CheckListController checkListController;
    private AlertDialog.Builder dialogCheckLists;
    private ArrayList<CheckList> checkLists;
    private ArrayList<Fold> folds;
    private ListView listViewItems;
    private AlertDialog dialogCheckList;
    private boolean selection = false;
    private LinkController linkController;
}
