package androidadmin.note;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.usuario.androidadmin.R;

public class Edit extends ActionBarActivity {

    EditText textTitle;
    EditText textBody;
    int idEdit = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        textTitle = (EditText) findViewById(R.id.titleEdit);
        textBody  = (EditText) findViewById(R.id.bodyEdit);

        Bundle b = this.getIntent().getExtras();
        if(b != null){
            findOneNoteById(b.getInt("idEdit"));
        }
    }

    public void updateNote(View v){
       String newTitle = textTitle.getText().toString();
       String newBody = textBody.getText().toString();

        NoteSQLHelper noteHepler = new NoteSQLHelper(this, "ideondb", null, 1);
        SQLiteDatabase db = noteHepler.getWritableDatabase();
        if(db != null){
            ContentValues contentNote = new ContentValues();
            contentNote.put("title",newTitle);
            contentNote.put("body",newBody);
            long i = db.update("note",contentNote," id ="+idEdit,null);
            if(i > 0){
                Toast.makeText(this, "Actualizado con exito!", Toast.LENGTH_LONG).show();
            }
            db.close();
            super.onBackPressed();
        }else{
            Toast.makeText(this, "Problemas con la BD", Toast.LENGTH_LONG).show();
        }
    }

    public void findOneNoteById(int id){
        NoteSQLHelper noteHepler = new NoteSQLHelper(this, "ideondb", null, 1);
        SQLiteDatabase db = noteHepler.getReadableDatabase();
        if(db != null){
            Cursor cursor = db.rawQuery("select * from note where id ="+id,null);
            try{
                if(cursor.moveToFirst()){
                    idEdit = cursor.getInt(0);
                    textTitle.setText(cursor.getString(1));
                    textBody.setText(cursor.getString(2));

                }
            }finally {
                db.close();
            }
        }
    }

    public void back(View v){
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
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
}
