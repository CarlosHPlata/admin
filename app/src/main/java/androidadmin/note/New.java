package androidadmin.note;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.usuario.androidadmin.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class New extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
    }

    public void newNote(View v){
        Calendar c1 = GregorianCalendar.getInstance();
        SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        //Toast.makeText(this, formatDate.format(c1.getTime()), Toast.LENGTH_LONG).show();
        String dateToday = formatDate.format(c1.getTime());

        EditText textTitle = (EditText) findViewById(R.id.title);
        String title = textTitle.getText().toString();
        EditText textBody = (EditText) findViewById(R.id.body);
        String body = textBody.getText().toString();

        NoteSQLHelper noteHepler = new NoteSQLHelper(this, "ideondb", null, 1);
        SQLiteDatabase db = noteHepler.getWritableDatabase();
        if(db != null){
            ContentValues contentNote = new ContentValues();
            contentNote.put("title",title);
            contentNote.put("body",body);
            contentNote.put("date",dateToday);
            long i = db.insert("note",null,contentNote);
            if(i > 0){
                Toast.makeText(this, "Agrego con exito!", Toast.LENGTH_LONG).show();
            }
            db.close();
            super.onBackPressed();
        }else{
            Toast.makeText(this, "Problemas con la BD", Toast.LENGTH_LONG).show();
        }
    }

    public void back(View v){
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new, menu);
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
