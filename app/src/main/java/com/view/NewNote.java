package com.view;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.controllers.NoteController;
import com.example.usuario.androidadmin.R;

public class NewNote extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        controller = new NoteController(getApplicationContext());
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            this.ID_FATHER = bundle.getInt("idFather");
        }
    }

    public void addNote(View v){
        EditText textTitle = (EditText) findViewById(R.id.titleEdit);
        EditText textBody = (EditText) findViewById(R.id.body);
        String body = textBody.getText().toString();
        String title = textTitle.getText().toString();

        if(controller.addNote(title, body, this.ID_FATHER)){
            Toast.makeText(this, "Nota creada", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Error!!", Toast.LENGTH_LONG).show();
        }
        backView();

    }

    public void backView(){
        super.onBackPressed();
        this.finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_note, menu);
        return super.onCreateOptionsMenu(menu);
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

    private NoteController controller;
    private int ID_FATHER = 0;
}
