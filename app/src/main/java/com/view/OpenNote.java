package com.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.usuario.androidadmin.R;
import com.model.Note;
import com.model.StableArrayAdapter;

import java.util.ArrayList;

public class OpenNote extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_note);
        loadNote();
        showNoteData();
    }

    private void loadNote() {
        Intent intent = getIntent();
        note = (Note) intent.getSerializableExtra("note");
        notes = note.getIncrustedNotes();
    }

    private void showNoteData() {
        showTitle();
        showContent();
        showLinks();
        showImg();
        showChilds();

    }

    private void showChilds() {
        if(!note.hasChilds())
            return;
        final ListView listview = (ListView) findViewById(R.id.listIncrustedNotes);
        final ArrayList<String> list = getNotesTitles();
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

    private void showLinks() {
        TextView txtLinks = (TextView) findViewById(R.id.txtLinks);
        txtLinks.setText("");
        /*for (String link : note.getSons()){
            txtLinks.setText(txtLinks.getText()+"\n"+link);
        }*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_open_note, menu);
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
    }

    private void passNote(Note note) {
        // 1. create an intent pass class name or intnet action name
        Intent intent = new Intent(OpenNote.class.getName());

        // 3. put person in intent data
        intent.putExtra("note", note);
        // 4. start the activity
        startActivity(intent);
    }

    private ArrayList<String> getNotesTitles(){
        ArrayList<String> titles = new ArrayList<>();
        for(Note note: notes){
            titles.add(note.getTitle());
        }
        return titles;
    }

    private ArrayList<Note> notes;
    private Note note;
}
