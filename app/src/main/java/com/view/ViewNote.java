package com.view;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.controllers.NoteController;
import com.example.usuario.androidadmin.R;
import com.models.Note;
import com.models.StableArrayAdapter;

import java.util.ArrayList;

public class ViewNote extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);
        controller = new NoteController(getApplicationContext());
        listNoteSon = (ListView) findViewById(R.id.listViewnoteSon);
        listNoteSon.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            this.ID_NOTE = bundle.getInt("id");
            initViewNoteById();
        }
    }

    public void initViewNoteById(){
        findNotesSon();
        generateListViewNotesSon();
        findNoteById();
        generateNoteFather();
    }

    public void findNotesSon(){
        notesSon = controller.findAllNotesSonByIdFather(this.ID_NOTE);

    }

    public void findNoteById(){
        this.noteFhater = controller.findOneById(this.ID_NOTE);
    }

    public void generateNoteFather(){
        TextView titleView = (TextView) findViewById(R.id.titleView);
        titleView.setText(this.noteFhater.getTitle());

        TextView bodyView = (TextView) findViewById(R.id.bodyView);
        bodyView.setText(this.noteFhater.getBody());
    }

    public void generateListViewNotesSon(){
        ArrayList<String> titles = new ArrayList<>();
        for(Note note: notesSon){
            titles.add(note.getTitle());
        }

        StableArrayAdapter adapter = new StableArrayAdapter(this,android.R.layout.simple_list_item_1, titles);
        this.listNoteSon.setAdapter(adapter);

        this.listNoteSon.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                viewNoteSon(notesSon.get(position));
            }

        });
    }

    public void viewNoteSon(Note note){
        Intent intent = new Intent(OpenNote.class.getName());
        intent.putExtra("id", note.getId());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        findNoteById();
        generateNoteFather();
        findNotesSon();
        generateListViewNotesSon();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_note, menu);
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
        if (id == R.id.action_newNoteSon) {
            Intent i = new Intent(this,NewNote.class);
            i.putExtra("id",this.ID_NOTE);
            i.putExtra("isFather",false);
            startActivity(i);

        }
        if (id == R.id.action_editNote) {
            Intent i = new Intent(this,EditNote.class);
            i.putExtra("id",this.ID_NOTE);
            startActivity(i);
        }
        if (id == R.id.action_delete) {
            this.noteFhater.setStatus(true);
            controller.deleteNote(this.noteFhater);
            Intent i = new Intent(this,ListNotes.class);
            startActivity(i);
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private int ID_NOTE;
    private NoteController controller;
    private ListView listNoteSon;
    private ArrayList<Note> notesSon;
    private Note noteFhater;
}
