    package com.controllers.sync;

    import android.content.Context;
    import android.util.Log;

    import com.controllers.sync.interfaces.SyncHandler;
    import com.controllers.sync.interfaces.SyncInterface;
    import com.loopj.android.http.AsyncHttpClient;
    import com.loopj.android.http.JsonHttpResponseHandler;
    import com.models.CheckList;
    import com.models.File;
    import com.models.Fold;
    import com.models.Link;
    import com.models.Note;
    import com.models.Tag;
    import com.models.mappers.CheckListMapper;
    import com.models.mappers.FileMapper;
    import com.models.mappers.FoldMapper;
    import com.models.mappers.LinksMapper;
    import com.models.mappers.NoteMapper;
    import com.models.mappers.TagMapper;

    import org.apache.http.Header;
    import org.apache.http.entity.StringEntity;
    import org.apache.http.message.BasicHeader;
    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;

    import java.io.UnsupportedEncodingException;
    import java.util.ArrayList;
    import java.util.Date;

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
            this.tagMapper = new TagMapper(context);
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
                            noteTemp.setStatus(!tempJson.getBoolean("deleted"));
                            noteTemp.setCreatedAt(new Date());
                            noteTemp.setUpdatedAt(new Date());
                            noteTemp.setIdFather(tempJson.getInt("parent_id"));
                            noteTemp.setTags(new ArrayList<Tag>());
                            noteTemp.setId(-1);
                            noteTemp.setSyncFlag(true);

                            //obteniendo tags
                            noteTemp.setTags(insertTagsFromNotes(tempJson.getJSONArray("tags")));

                            //insertando nota
                            long idNote = noteMapper.insertNote(noteTemp);

                            //insertando checklist de la nota
                            JSONArray checkJson = tempJson.getJSONArray("checklist_items");
                            insertCheckFromUser(checkJson, (int) idNote);

                            //insertando los archivos:
                            JSONArray arrayFiles = tempJson.getJSONArray("assets");
                            insertFilesFromNotes(arrayFiles, (int) idNote);

                            //insertando los folds:
                            JSONArray arrayFolds = tempJson.getJSONArray("folds");
                            insertFoldsFromNote(arrayFolds, (int) idNote);

                            //insertando los links
                            JSONArray arrayLinks = tempJson.getJSONArray("links");
                            insertLinksFromNote(arrayLinks, (int) idNote);


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

        //curl -X POST -H "Authorization: 68f582de1c5439ae3a0a4f6a050822ac" -H
        // "Content-Type: application/json" -d '{"note":{"title":"nueva nota","body":"esta es la super nota", "tag_ids":[3,4]}}' http://104.131.189.224/api/notes
        public void createNote(String token, Note note){
            String bodyAsJson = "{\"note\":{\"title\":\""+note.getTitle()+"\",\"body\":\""+note.getBody()+"\", ";

            String jsonTags = "\"tag_ids\":[ ";

            for (int i=0; i<note.getTags().size(); i++){
                if (i>0) jsonTags+=", ";
                jsonTags += note.getTags().get(i).getExtId();
            }

            jsonTags += "]}}";

            bodyAsJson += jsonTags;

            StringEntity entity  = null;

            try {
                entity = new StringEntity(bodyAsJson);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            Header[] headers = {
                    new BasicHeader("Authorization",token)
            };

            client.post(this.context, "http://104.131.189.224/api/notes", headers , entity, "application/json",  new JsonHttpResponseHandler() {

                public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                    listener.onResponse(1);
                }


                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    if (listener != null) listener.onError(statusCode, responseString);
                }

                public void onFailure(int i, Header[] header, Throwable e, JSONObject json) {
                    String error = "Error fatal, intentelo mas tarde";
                    int errorCode = 500;
                    try {
                        error = json.getString("email");
                        errorCode = 1;
                    } catch (JSONException e1) {
                        try {
                            error = json.getString("password");
                            errorCode = 2;
                        } catch (JSONException e2) {
                            e2.printStackTrace();
                        }
                    }

                    listener.onError(errorCode, error);
                }
            });
        }

        //curl -X PUT -H "Authorization: dc45800fddee07cf9b300d2765283cb2" -H "Content-Type: application/json" -d '{"note":{"title":"actualizar etiquetas","body":"etiquetaaas actualizadaaas", "tag_ids":[3,4]}}' http://104.131.189.224/api/notes/
        public void updateNote(String token, Note note){
            Header[] headers = {
                    new BasicHeader("Authorization",token)
            };



            String bodyAsJson = "{\"note\":{\"title\":\""+note.getTitle()+"\",\"body\":\""+note.getBody()+"\", ";

            String jsonTags = "\"tag_ids\":[ ";

            for (int i=0; i<note.getTags().size(); i++){
                if (i>0) jsonTags+=", ";
                jsonTags += note.getTags().get(i).getExtId();
            }
            jsonTags += "]}}";
            bodyAsJson += jsonTags;

            StringEntity entity  = null;
            try {
                entity = new StringEntity(bodyAsJson);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            client.put(this.context, "http://104.131.189.224/api/notes/"+note.getExtId(), headers, entity,"application/json",new JsonHttpResponseHandler(){
                public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                    Log.i("DEBUG:", json.toString());
                }


                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.i("DEBUG:", "error");
                }

                public void onFailure(int i, Header[] header, Throwable e, JSONObject json){
                    Log.i("DEBUG:", json.toString());
                }
            });
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

        public void insertCheckFromUser(JSONArray checkJson, int idNote) throws JSONException {
            CheckList chekTemp;
            for (int e = 0; e<checkJson.length(); e++){
                JSONObject checkObj = checkJson.getJSONObject(e);
                chekTemp = new CheckList();

                chekTemp.setId(-1);
                chekTemp.setExtId(checkObj.getInt("id"));
                chekTemp.setDescription(checkObj.getString("description"));
                chekTemp.setChecked(checkObj.getBoolean("checked"));
                chekTemp.setNoteId(idNote);
                chekTemp.setSyncFlag(true);

                checkListMapper.insertCheckList(chekTemp);
            }
        }

        public ArrayList<Tag> insertTagsFromNotes(JSONArray arrayTags) throws JSONException {
            ArrayList<Tag> tags = new ArrayList<Tag>();

            for (int o = 0; o <arrayTags.length(); o++){
                JSONObject objTags = arrayTags.getJSONObject(o);
                Tag tempTag = new Tag();
                tempTag.setExtId(objTags.getInt("id"));
                tempTag = tagMapper.findOneByExtId(tempTag);
                if (tempTag == null){
                    tempTag = new Tag();
                    tempTag.setId(-1);
                    tempTag.setExtId(objTags.getInt("id"));
                    tempTag.setName(objTags.getString("title"));
                    tempTag.setSyncFlag(true);
                    long idTag = tagMapper.insertTag(tempTag);
                    tempTag.setId((int) idTag);
                }
                tags.add(tempTag);
            }
            return tags;
        }

        public void insertFilesFromNotes(JSONArray arrayFiles, int idNote) throws JSONException {
            FileMapper fileMapper = new FileMapper(context);
            File tempFile;
            for (int e=0; e<arrayFiles.length(); e++){
                JSONObject tempFileJson = arrayFiles.getJSONObject(e);
                tempFile = new File();
                tempFile.setId(-1);
                tempFile.setName(tempFileJson.getString("filename"));
                tempFile.setNote_id(idNote);
                tempFile.setExt_id(tempFileJson.getInt("id"));
                tempFile.setPath("");
                tempFile.setSync_flag(true);

                fileMapper.insetFile(tempFile);
            }
        }

        public void insertFoldsFromNote(JSONArray arrayFolds, int idNote) throws JSONException {
            Fold fold;
            FoldMapper foldMapper = new FoldMapper(context);
            for (int e=0; e<arrayFolds.length(); e++){
                JSONObject JsonFold = arrayFolds.getJSONObject(e);

                fold = new Fold();
                fold.setId(-1);
                fold.setSyncFlag(true);
                fold.setExtId(JsonFold.getInt("id"));
                fold.setNoteId(idNote);
                fold.setContent(JsonFold.getString("content"));

                foldMapper.insertFold(fold);
            }
        }

        public void insertLinksFromNote(JSONArray arrayLinks, int idNote) throws JSONException {
            Link link;
            LinksMapper linksMapper = new LinksMapper(context);
            for (int e=0; e<arrayLinks.length(); e++){
                JSONObject jsonLink = arrayLinks.getJSONObject(e);

                link = new Link();
                link.setId(-1);
                link.setSyncFlag(true);
                link.setNoteId(idNote);
                link.setLinkedNoteId(jsonLink.getInt("linked_note"));
                link.setExtId(jsonLink.getInt(""));

                linksMapper.addLink(link);
            }
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
        private TagMapper tagMapper;
    }
