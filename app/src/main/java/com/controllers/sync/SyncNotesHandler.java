    package com.controllers.sync;

    import android.content.Context;
    import android.util.Log;

    import com.controllers.sync.interfaces.SyncHandler;
    import com.controllers.sync.interfaces.SyncInterface;
    import com.loopj.android.http.AsyncHttpClient;
    import com.loopj.android.http.JsonHttpResponseHandler;
    import com.models.CheckList;
    import com.models.Note;
    import com.models.mappers.CheckListMapper;
    import com.models.mappers.NoteMapper;

    import org.apache.http.Header;
    import org.apache.http.message.BasicHeader;
    import org.json.JSONArray;
    import org.json.JSONException;
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
            this.noteMapper = new NoteMapper(context);
            this.checkListMapper = new CheckListMapper(context);
        }

        public void getNotesFromuser(String token){

            Header[] headers = {
                    new BasicHeader("Authorization",token)
            };


            client.get(this.context, "http://104.131.189.224/api/notes", headers, null, new JsonHttpResponseHandler() {
                public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                    try {
                        noteMapper.dropNotes();
                        JSONArray notes = json.getJSONArray("notes");
                        for (int i=0; i<notes.length(); i++){
                            JSONObject tempJson= notes.getJSONObject(i);
                            Note noteTemp = new Note();
                            noteTemp.setExtId(tempJson.getInt("id"));
                            noteTemp.setBody(tempJson.getString("body"));
                            noteTemp.setTitle(tempJson.getString("title"));
                            noteTemp.setFavorite(tempJson.getBoolean("favorite"));
                            noteTemp.setStatus(tempJson.getBoolean("deleted"));
                            noteTemp.setId(0);
                            long idNote = noteMapper.insertNote(noteTemp);

                            Log.i("NOTES", idNote+"");

                            JSONArray checkJson = tempJson.getJSONArray("checklist_items");
                            for (int e = 0; e<checkJson.length(); e++){
                                JSONObject checkObj = checkJson.getJSONObject(e);
                                CheckList chekTemp = new CheckList();
                                chekTemp.setExtId(checkObj.getInt("id"));
                                chekTemp.setChecked(checkObj.getBoolean("checked"));
                                chekTemp.setDescription(checkObj.getString("description"));
                                chekTemp.setNoteId((int) idNote);

                                checkListMapper.insertCheckList(chekTemp);
                            }

                            JSONArray arrayTags = tempJson.getJSONArray("tags");
                            for (int o = 0; o <arrayTags.length(); o++){
                                JSONObject objTags = arrayTags.getJSONObject(o);

                            }
                        }
                        Log.i("NOTES",notes.toString());
                        listener.onResponse(1);
                    } catch (JSONException e) {
                        e.printStackTrace();
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
        private NoteMapper noteMapper;
        private CheckListMapper checkListMapper;
    }
