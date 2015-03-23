package com.controllers;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.models.User;

import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import java.io.UnsupportedEncodingException;

/**
 * Created by Usuario on 16/03/2015.
 */
public class SyncController {

     public SyncController(Context context){
         this.context = context;
     }

    public void syncNotes(){}

    public void syncTags() {}

    public User registUser(String mail, String pass) throws UnsupportedEncodingException {
        final User user = new User();
        Log.i("ESTOOOOOOOO","PASO POR AQUI");
        String bodyAsJson = "e6bc9f7c1e74cb3dd8ccde07e6edbc32";

        StringEntity entity  = new StringEntity(bodyAsJson);
        Header[] headers = {
                new BasicHeader("Authorization","e6bc9f7c1e74cb3dd8ccde07e6edbc32")
        };


        client.get(this.context, "http://104.131.189.224/api/notes", headers, null, new JsonHttpResponseHandler() {


            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                Log.i("ESTOOOOOOOO","EJEJE");
                try {
                    user.setToken(json.toString());
                    Log.i("ESTOOOOOOOO",json.toString());
                    Log.i("ESTOOOOOOOO",user.getToken());
                    status = 1;
                }  catch (Exception e) {
                    user.setToken("error de quien sabe que mierda");
                }

            }


            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.i("ESTOOOOOOOO","SUCCES DIFERENTE");
                user.setToken("llega de otro pedo");
            }


            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.i("ESTOOOOOOOO","SUCCESS STRING");
                user.setToken(responseString);
            }


            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("ESTOOOOOOOO","FAILURE");
                user.setToken("errorsini");
            }
        });


        Log.i("ESTOOOOOOOO","final: "+user.getToken());

        Log.i("ESTOOOOOOOO"," final 2:  "+user.getToken());
        return user;
    }

    public void getUserFromServer() {}

    public void getNotesOfUserFromServer() {}




    private static AsyncHttpClient client = new AsyncHttpClient();
    private Context context;
    private int status = 0;
}
