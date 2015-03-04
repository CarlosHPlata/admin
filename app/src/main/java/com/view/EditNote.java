package com.view;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.controllers.NoteController;
import com.example.usuario.androidadmin.R;
import com.models.Note;

public class EditNote extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        controller = new NoteController(getApplicationContext());
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            this.ID_NOTE = bundle.getInt("id");
            initViewEditNote();
        }
    }

    public void initViewEditNote(){
        findNoteById();
        generateViewEditNote();
    }

    public void findNoteById(){
        this.note = controller.findOneById(this.ID_NOTE);
    }

    public void generateViewEditNote(){
      EditText titleEdit = (EditText) findViewById(R.id.titleEdit);
      EditText bodyEdit = (EditText) findViewById(R.id.bodyEdit);

        titleEdit.setText(this.note.getTitle());
        bodyEdit.setText(this.note.getBody());
    }

    public void updateNote(View v){
        EditText titleEdit = (EditText) findViewById(R.id.titleEdit);
        EditText bodyEdit = (EditText) findViewById(R.id.bodyEdit);

        Note noteUpdate = new Note();
        noteUpdate.setId(this.ID_NOTE);
        this.note.setTitle(titleEdit.getText().toString());
        this.note.setBody(bodyEdit.getText().toString());
        controller.updateNote(this.note);
        backView();
    }

    public void backView(){
        super.onBackPressed();
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_note, menu);
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

    private int ID_NOTE;
    private NoteController controller;
    private Note note;
}
