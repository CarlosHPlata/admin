package com.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.controllers.NoteController;
import com.example.usuario.androidadmin.R;
import com.models.Fold;
import com.models.Note;
import com.models.StableArrayAdapter;
import com.models.Tag;
import com.view.ExpandableLisView.InfoDetailsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que se encarga de mostrar los datos de una nota que ha sido borrada
 * @author Ramón Díaz
 * @version 0.1 13/03/2015.
 *
 */

public class ViewDeletedNote extends Fragment {
    public View viewDeletedNote;
    List<String> group;
    List<List<String>> child;
    ExpandableListView expandList;
    InfoDetailsAdapter adapterExpandableListView;
    /*Metrodos originales, no los borre por si mas adelante los necesito
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_deleted_note);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_deleted_note, menu);
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

        return super.onOptionsItemSelected(item);
    }*/

    public static ViewDeletedNote newInstance(Bundle arguments){
        ViewDeletedNote viewDeletedNote = new ViewDeletedNote();
        if(arguments != null){
            viewDeletedNote.setArguments(arguments);
        }
        return viewDeletedNote;
    }

    public ViewDeletedNote(){

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true); //Indicamos que este Fragment tiene su propio menu de opciones
    }

    public void initViewNoteById(){
        findNoteById();
        findNotesSon();
        generateListViewNotesSon();
        generateNoteFather();
        generateTagSelected();
    }

    public void generateTagSelected() {
        TextView viewTags = (TextView) viewDeletedNote.findViewById(R.id.viewTags);
        ArrayList<Tag> tagsSelect = noteFather.getTags();
        String nameTags = "Tags:\n";
        for (int y = 0; y < tagsSelect.size(); y++) {
            Tag tagAux = tagsSelect.get(y);
            nameTags += tagAux.getName() + ", ";
        }
        viewTags.setText(nameTags);
    }

    public void findNotesSon(){
        notesSon = controller.findAllNotesSonByIdFather(this.ID_NOTE);
    }

    public void findNoteById(){
        this.noteFather = controller.findOneById(this.ID_NOTE);
        folds = this.noteFather.getFolds();
    }

    public void generateNoteFather(){
        TextView titleView = (TextView) viewDeletedNote.findViewById(R.id.titleView);
        titleView.setText(this.noteFather.getTitle());

        TextView bodyView = (TextView) viewDeletedNote.findViewById(R.id.bodyView);
        bodyView.setText(this.noteFather.getBody());
    }

    public void generateListViewNotesSon(){
        if(!noteFather.hasSons())
            return;
        final ListView listview = (ListView) viewDeletedNote.findViewById(R.id.listViewnoteSon);
        final ArrayList<String> list = getNotesTitles(noteFather.getSons());
        final StableArrayAdapter adapter = new StableArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                viewNoteSon(noteFather.getSons().get(position));
            }

        });

    }

    public void viewNoteSon(Note note){
        Intent intent = new Intent(ViewNote.class.getName());
        intent.putExtra("id", note.getId());
        startActivity(intent);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.menu_view_deleted_note, menu);
        inflater.inflate(R.menu.menu_view_deleted_note, menu);
      //  return true;
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
        if(id == R.id.action_delete){
            controller.deleteNotePermanently(noteFather);
            getActivity().onBackPressed();
        }
        if (id == R.id.action_restore_note) {
            controller.restore(noteFather);
            getActivity().onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewDeletedNote = inflater.inflate(R.layout.activity_view_note, container, false);

        controller = new NoteController(getActivity().getApplicationContext());
        listNoteSon = (ListView) viewDeletedNote.findViewById(R.id.listViewnoteSon);
        listNoteSon.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        expandList = (ExpandableListView) viewDeletedNote.findViewById(R.id.expandableListView1);
        Bundle bundle = getArguments();
        if(bundle != null){
            this.ID_NOTE = bundle.getInt("id");
            initViewNoteById();
            initExpandableListView();
        }
        return viewDeletedNote;
    }

    public void initExpandableListView(){
        initialDataFold();
        adapterExpandableListView = new InfoDetailsAdapter(getActivity(), this.group, this.child);
        expandList.setAdapter(adapterExpandableListView);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

/*
    @Override
    protected void onResume() {
        super.onResume();
        findNoteById();
        findNotesSon();
        generateListViewNotesSon();
        generateNoteFather();
    }
*/

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
            addInfoFold(groupAux, new String[]{content});
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

    private ArrayList<String> getNotesTitles(ArrayList<Note> notes){
        ArrayList<String> titles = new ArrayList<>();
        for(Note note: notes){
            titles.add(note.getTitle());
        }
        return titles;
    }

    private int ID_NOTE;
    private NoteController controller;
    private ArrayList<Fold> folds;
    private ListView listNoteSon;
    private ArrayList<Note> notesSon;
    private Note noteFather;
}
