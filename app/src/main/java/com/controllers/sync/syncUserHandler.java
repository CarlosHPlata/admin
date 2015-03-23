package com.controllers.sync;

import android.content.Context;
import android.util.Log;

import com.controllers.sync.interfaces.SyncHandler;
import com.controllers.sync.interfaces.SyncInterface;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.models.User;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Usuario on 17/03/2015.
 */
public class syncUserHandler extends SyncHandler{

    private User user;
    private ArrayList<User> users;


    public syncUserHandler(Context context, SyncInterface listener) {
        super(context, listener, new AsyncHttpClient());
    }

    public void  createUser(User user){
        String bodyAsJson = "{\"user\":{\"email\":\""+user.getEmail()+"\",\"password\":\""+user.getPassword()+"\"}}";

        StringEntity entity  = null;

        try {
            entity = new StringEntity(bodyAsJson);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Header[] headers = {
                new BasicHeader("Content-type", "application/json")
        };

        client.post(this.context, "http://104.131.189.224/api/user", headers , entity, "application/json",  new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                listener.onResponse(1);
            }


            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (listener != null) listener.onError(statusCode, responseString);
            }
        });

    }


    public void getToken(User user){
        RequestParams params = new RequestParams();
        params.add("email", user.getEmail());
        params.add("password", user.getPassword());

        client.post("http://104.131.189.224/api/token", params, new JsonHttpResponseHandler() {
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
}
