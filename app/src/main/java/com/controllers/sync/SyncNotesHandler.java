    package com.controllers.sync;

    import android.content.Context;
    import android.util.Log;

    import com.loopj.android.http.AsyncHttpClient;
    import com.loopj.android.http.JsonHttpResponseHandler;
    import com.models.Note;
    import com.models.User;

    import org.apache.http.Header;
    import org.apache.http.entity.StringEntity;
    import org.apache.http.message.BasicHeader;
    import org.json.JSONArray;
    import org.json.JSONObject;

    import java.util.ArrayList;

    /**
     * Created by Usuario on 17/03/2015.
     * Contributors
     * CmM dev.cmedina@gmail.com
     */


    public class SyncNotesHandler {

        public SyncNotesHandler(Context context){

            this.context = context;
            this.note = new Note();
            this.notes = new ArrayList<Note>();
            this.client = new AsyncHttpClient();
            this.status = 0;
        }

        public void getNotesFromuser(String token){
            Log.i("Debuggin", "comes around here");

            Header[] headers = {
                    new BasicHeader("Authorization",token)
            };


            client.get(this.context, "http://104.131.189.224/api/notes", headers, null, new JsonHttpResponseHandler() {


                public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                    Log.i("Debuggin","getting response");
                    try {
                        Log.i("Debuggin", "Response: "+json.toString());
                        note.setTitle(json.toString());
                        if(listener != null) listener.onNoteReceived(note);
                    }  catch (Exception e) {
                        Log.i("Debuggin","error");
                    }

                }


                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.i("Debuggin","FAILURE");
                    if(listener != null) listener.onErrorReceived(statusCode, responseString);
                }
            });
            Log.i("Debuggin","pre-final :"+note.getTitle());
        }


        public ArrayList<Note> getNotesResponseArray(){
            return this.notes;
        }


        public void destroy(){
            this.note = null;
            this.notes = null;
            this.client = null;
            this.status = 0;
        }


        public int getStatus(){
            return this.status;
        }

        public void setListener(SyncNotesInterface _listener){
            this.listener = _listener;
        }

        private static AsyncHttpClient client;
        private Note note;
        private ArrayList<Note> notes;
        private Context context;
        private int status;
        private SyncNotesInterface listener;
    }
