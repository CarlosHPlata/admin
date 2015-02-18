package androidadmin.note;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Edgar on 10/02/2015.
 */
public class NoteSQLHelper extends SQLiteOpenHelper{

    String sqlTableNote = "CREATE TABLE note (id INTEGER PRIMARY KEY, title TEXT, body TEXT, date TEXT)";

    public NoteSQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlTableNote);
     }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXIST note");
        db.execSQL(sqlTableNote);

    }
}
