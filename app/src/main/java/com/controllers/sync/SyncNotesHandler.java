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
     */
    public class SyncNotesHandler {

        public SyncNotesHandler(Context context){
            this.context = context;
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
                    }  catch (Exception e) {
                        Log.i("Debuggin","error");
                    }

                }


                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.i("Debuggin","FAILURE");
                }
            });
            Log.i("Debuggin","pre-final :"+note.getTitle());
        }

        public Note getNoteResponse(){
            Log.i("Debuggin","final :"+note.getTitle());
            return this.note;
        }

        public ArrayList<Note> getNotesResponseArray(){
            return this.notes;
        }

        public void start(){
            this.note = new Note();
            this.notes = new ArrayList<Note>();
            this.client = new AsyncHttpClient();
            this.status = 0;
        }

        public void end(){
            this.note = null;
            this.notes = null;
            this.client = null;
            this.status = 0;
        }

        public int getStatus(){
            return this.status;
        }

        private static AsyncHttpClient client;
        private Note note;
        private ArrayList<Note> notes;
        private Context context;
        private int status;
    }
