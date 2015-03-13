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

public class ViewDeletedNote extends ActionBarActivity {
    /*Metrodos originales
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

    public void initViewNoteById(){
        findNoteById();
        findNotesSon();
        generateListViewNotesSon();
        generateNoteFather();
    }

    public void findNotesSon(){
        notesSon = controller.findAllNotesSonByIdFather(this.ID_NOTE);
    }

    public void findNoteById(){
        this.noteFather = controller.findOneById(this.ID_NOTE);
    }

    public void generateNoteFather(){
        TextView titleView = (TextView) findViewById(R.id.titleView);
        titleView.setText(this.noteFather.getTitle());

        TextView bodyView = (TextView) findViewById(R.id.bodyView);
        bodyView.setText(this.noteFather.getBody());
    }

    public void generateListViewNotesSon(){
        if(!noteFather.hasSons())
            return;
        final ListView listview = (ListView) findViewById(R.id.listViewnoteSon);
        final ArrayList<String> list = getNotesTitles(noteFather.getSons());
        final StableArrayAdapter adapter = new StableArrayAdapter(this,android.R.layout.simple_list_item_1, list);
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
        if (id == R.id.action_restore_note) {
            Intent i = new Intent(this,ListNotes.class);
            //i.putExtra("id",this.ID_NOTE);
            //i.putExtra("isFather",false);
            controller.restore(noteFather);
            startActivity(i);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);
        controller = new NoteController(getApplicationContext());
        listNoteSon = (ListView) findViewById(R.id.listViewnoteSon);
        listNoteSon.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        //Toast.makeText(this, "Entro en ViewNote", Toast.LENGTH_LONG).show();
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){

            this.ID_NOTE = bundle.getInt("id");
            initViewNoteById();
        }
    }



    /*
     Métodos de Ramón

      private void loadNote() {
        Intent intent = getIntent();
        note = (Note) intent.getSerializableExtra(NOTE_NAME);
        notes = note.getIncrustedNotes();
    }

     private void showNoteData() {
        showTitle();
        showContent();
        showSons();
        showImg();
        showIncrustedNotes();

    }

     private void showIncrustedNotes() {
        if(!note.hasIncrustedNotes())
            return;
        final ListView listview = (ListView) findViewById(R.id.listIncrustedNotes);
        final ArrayList<String> list = getNotesTitles(notes);
        final StableArrayAdapter adapter = new StableArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                passNote(notes.get(position));
            }

        });
    }

    private void showImg() {
    ImageView imgView = (ImageView) findViewById(R.id.imageView);

    imgView.setImageResource(R.drawable.ic_launcher);
    }

    private void showContent() {
        EditText txtContent = (EditText) findViewById(R.id.txtContent);
        txtContent.setText(note.getBody());
    }

    private void showTitle() {
        TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(note.getTitle());
    }

    private void passNote(Note note) {
    //Se crea un intent y se pasa el nombre de la clase
    Intent intent = new Intent(OpenNote.class.getName());
    //Se pone la nota en el intent
    intent.putExtra(NOTE_NAME, note);
    //Se inicia la actividad
    startActivity(intent);
    }
     */

    @Override
    protected void onResume() {
        super.onResume();
        findNoteById();
        findNotesSon();
        generateListViewNotesSon();
        generateNoteFather();
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
