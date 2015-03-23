package com.view;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.controllers.NoteController;
import com.controllers.TagController;
import com.example.usuario.androidadmin.R;
import com.models.Note;
import com.models.StableArrayAdapter;
import com.models.Tag;

import java.util.ArrayList;

public class ViewNote extends Fragment {
    public View viewNote;

    public static ViewNote newInstance(Bundle arguments){
        ViewNote viewNote = new ViewNote();
        if(arguments != null){
            viewNote.setArguments(arguments);
        }
        return viewNote;
    }

    public ViewNote(){

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
        listNoteSon = (ListView) viewNote.findViewById(R.id.listViewnoteSon);
        listNoteSon.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        Bundle bundle = getArguments();
        if(bundle != null){
            this.ID_NOTE = bundle.getInt("id");
            initViewNoteById();
        }
        return viewNote;
    }

    public void initViewNoteById(){
        findNoteById();
        findNotesSon();
        generateListViewNotesSon();
        generateNoteFather();
        generateTagSelected();
    }

    public void generateTagSelected(){
        TextView viewTags = (TextView) viewNote.findViewById(R.id.viewTags);
        ArrayList<Tag> tagsSelect = noteFather.getTags();
        String nameTags = "Tags:\n";
        for (int y=0; y<tagsSelect.size();y++) {
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
    }

    public void generateNoteFather(){
        TextView titleView = (TextView) viewNote.findViewById(R.id.titleView);
        titleView.setText(this.noteFather.getTitle());

        TextView bodyView = (TextView) viewNote.findViewById(R.id.bodyView);
        bodyView.setText(this.noteFather.getBody());
    }

    public void generateListViewNotesSon(){
        if(!noteFather.hasSons())
            return;
        final ListView listview = (ListView) viewNote.findViewById(R.id.listViewnoteSon);
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

       /* Intent intent = new Intent(ViewNote.class.getName());
        intent.putExtra("id", note.getId());
        startActivity(intent); */
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
            Toast.makeText(getActivity(), "Las notas hijas en mantenimiento", Toast.LENGTH_SHORT).show();
       /*     Intent i = new Intent(this,NewNote.class);
            i.putExtra("id",this.ID_NOTE);
            i.putExtra("isFather",false);
            startActivity(i); */

        }
        if (id == R.id.action_editNote) {
            Bundle arguments = new Bundle();
            arguments.putInt("id", this.ID_NOTE);

            Fragment fragment = EditNote.newInstance(arguments);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();
        }
        if (id == R.id.action_delete) {
            this.noteFather.setStatus(true);
            controller.deleteNote(this.noteFather);

            Fragment fragment = new ListNotes();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    private ListView listNoteSon;
    private ArrayList<Note> notesSon;
    private Note noteFather;

}
