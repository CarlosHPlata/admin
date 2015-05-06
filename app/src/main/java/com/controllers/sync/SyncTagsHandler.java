package com.controllers.sync;

import android.content.Context;
import android.util.Log;

import com.controllers.sync.interfaces.SyncHandler;
import com.controllers.sync.interfaces.SyncInterface;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.models.Tag;
import com.models.mappers.TagMapper;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by Usuario on 02/05/2015.
 */
public class SyncTagsHandler extends SyncHandler {

    public SyncTagsHandler(Context context, SyncInterface listener) {
        super(context, listener, new AsyncHttpClient());
    }

    //curl -X POST -H "Authorization: dc45800fddee07cf9b300d2765283cb2" -H
    // "Content-Type: application/json" -d
    // '{"tag":{"name":"etiquetaaa"}}' http://104.131.189.224/api/tags
    //respnse {"tag":{"id":3,"title":"etiquetaaa"}}
    public void createTag(String token, final Tag tag){
        Header [] headers = {
            new BasicHeader("Authorization", token),
                new BasicHeader("Content-type", "application/json")
        };

        String bodyAsJson = "{\"tag\":{\"name\":\""+tag.getName()+"\"}}";

        Log.i("IDEON", bodyAsJson);

        StringEntity entity = null;

        try{
            entity = new StringEntity(bodyAsJson);
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        client.post(this.context, "http://104.131.189.224/api/tags", headers, entity, "application/json", new JsonHttpResponseHandler(){

            public void onSuccess(int statusCode, Header[] headers, JSONObject json){
                Log.i("IDEON", "success");
                try {
                    JSONObject etiqueta = json.getJSONObject("tag");
                    tag.setExtId(etiqueta.getInt("id"));
                    tag.setSyncFlag(true);
                    TagMapper mapper = new TagMapper(context);
                    mapper.updateTag(tag);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(int i, Header[] header, Throwable e, JSONObject json) {
                Log.i("IDEON", "failure tag"+json.toString());
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


    //curl -X PUT -H "Authorization: dc45800fddee07cf9b300d2765283cb2" -H
    // "Content-Type: application/json" -d
    // '{"tag":{"name":"etiquetaaa actualizadaaa"}}'
    // http://104.131.189.224/api/tags/3
    public void updateTag(String token, final Tag tag){
        Header [] headers = {
                new BasicHeader("Authorization", token),
                new BasicHeader("Content-type", "application/json")
        };

        String bodyAsJson = "{\"tag\":{\"name\":\""+tag.getName()+"\"}}";

        Log.i("IDEON", bodyAsJson);

        StringEntity entity = null;
        try{
            entity = new StringEntity(bodyAsJson);
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        client.put(this.context, "http://104.131.189.224/api/tags/"+tag.getExtId(), headers, entity, "application/json", new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                tag.setSyncFlag(true);
                TagMapper mapper = new TagMapper(context);
                mapper.updateTag(tag);
                Log.i("DEBUG:", json.toString());
            }


            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("DEBUG:", "error");
            }

            public void onFailure(int i, Header[] header, Throwable e, JSONObject json){
                Log.i("DEBUG:", "Pos mal");
            }
        });
    }

}
