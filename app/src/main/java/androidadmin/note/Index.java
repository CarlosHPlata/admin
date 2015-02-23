package androidadmin.note;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usuario.androidadmin.R;

public class Index extends ActionBarActivity {

    TextView totalNote;
    ListView listNote;
    String[] rowsNotes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        totalNote = (TextView) findViewById(R.id.lbTotalNote);
        listNote = (ListView) findViewById(R.id.listViewNotes);
        listNote.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int idNote = Integer.parseInt(rowsNotes[position].split(" ")[0]);
                Intent activityEdit = new Intent(Index.this,Edit.class);
                activityEdit.putExtra("idEdit",idNote);
                startActivity(activityEdit);
                //message(idNote);
            }
        });
        getAllNote();
    }

 /*   public void message(int idNote){
        Toast.makeText(this, "ID: "+Integer.toString(idNote), Toast.LENGTH_LONG).show();
    }
*/
    public void newNoteActivity(View v){
        Intent i = new Intent(this, New.class);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllNote();
    }

    public void getAllNote(){
        NoteSQLHelper noteHepler = new NoteSQLHelper(this, "ideondb", null, 1);
        SQLiteDatabase db = noteHepler.getReadableDatabase();
        if(db != null){
            Cursor cursor = db.rawQuery("select * from note",null);
            int total = cursor.getCount();
            totalNote.setText(Integer.toString(total));
            rowsNotes = new String[total];
            int i =0;
            if(cursor.moveToFirst()){
                do {
                    String row = cursor.getInt(0)+" "+cursor.getString(1)+" - "+cursor.getString(3);
                    rowsNotes[i] = row;
                    i++;
                }while (cursor.moveToNext());

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, rowsNotes);
                ListView list =(ListView) findViewById(R.id.listViewNotes);
                list.setAdapter(adapter);
            }
        }else{
            Toast.makeText(this, "Problemas con la BD", Toast.LENGTH_LONG).show();
        }
    }


    public void backLogin(View v){
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_index, menu);
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
