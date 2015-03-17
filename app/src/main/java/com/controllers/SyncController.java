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
        String bodyAsJson = "{\"user\":{\"email\":\""+mail+"\",\"password\":\""+pass+"\"}}";

        StringEntity entity  = new StringEntity(bodyAsJson);
        Header[] headers = {
                new BasicHeader("Content-type", "application/json")
        };

        client.post(this.context, "http://104.131.189.224/api/user", headers , entity, "application/json",  new AsyncHttpResponseHandler() {


            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                try {
                    json = json.getJSONObject("user");
                    user.setId(json.getInt("id"));
                    user.setEmail(json.getString("email"));
                    user.setPassword("123456");
                    user.setToken(json.getString("auth_token"));
                } catch ( JSONException e) {
                    user.setToken("error y te jodes");
                } catch (Exception e) {
                    user.setToken("error de quien sabe que mierda");
                }

            }


            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                user.setToken("llega de otro pedo");
            }


            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                user.setToken(responseString);
            }


            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                user.setToken("errorsini");
            }

            @Override
            public void onRetry(int retryNo) {
                user.setToken("u.u");
            }

            @Override
            public void onFinish() {
                user.setToken("al menos entro aqui");
            }

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                user.setToken("es un response raro");
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                user.setToken("ubo un failure");
            }
        });



        return user;
    }

    public void getUserFromServer() {}

    public void getNotesOfUserFromServer() {}




    private static AsyncHttpClient client = new AsyncHttpClient();
    private Context context;
}
