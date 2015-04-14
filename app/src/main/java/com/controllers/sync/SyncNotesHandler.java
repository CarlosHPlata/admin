    package com.controllers.sync;

    import android.content.Context;

    import com.controllers.sync.interfaces.SyncHandler;
    import com.controllers.sync.interfaces.SyncInterface;
    import com.loopj.android.http.AsyncHttpClient;
    import com.loopj.android.http.JsonHttpResponseHandler;
    import com.models.Note;

    import org.apache.http.Header;
    import org.apache.http.message.BasicHeader;
    import org.json.JSONObject;

    import java.util.ArrayList;

    /**
     * @(#)JceSecurity.java 1.50 04/04/14
     *
     * Esta clase se encarga de la sincronizacion de las notas con el servidor
     *
     * @author Carlos Herrera
     *
     * @version 1.50, 014/04/15
     * @since 1.4
     */


    public class SyncNotesHandler extends SyncHandler {

        public SyncNotesHandler(Context context,  SyncInterface listener){
            super(context, listener, new AsyncHttpClient());
            this.note = new Note();
            this.notes = new ArrayList<Note>();
            this.status = 0;
        }

        public void getNotesFromuser(String token){

            Header[] headers = {
                    new BasicHeader("Authorization",token)
            };


            client.get(this.context, "http://104.131.189.224/api/notes", headers, null, new JsonHttpResponseHandler() {


                public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                    try {
                        note.setTitle(json.toString());
                        if (listener != null) listener.onResponse(note);
                    } catch (Exception e) {
                    }

                }


                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    if (listener != null) listener.onError(statusCode, responseString);
                }
            });
        }

        public void createNote(String token, Note note){

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

        public void setListener(SyncInterface _listener){
            this.listener = _listener;
        }

        private Note note;
        private ArrayList<Note> notes;
        private int status;
    }
