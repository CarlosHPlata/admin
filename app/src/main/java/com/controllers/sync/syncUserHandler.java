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
import org.json.JSONException;
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

    //curl -X POST -H "Content-Type: application/json" -d '{"user":{"email":"plata@mail.com","password":"123456"}}' http://104.131.189.224/api/user
    //response:
    //{"user":{"id":3,"email":"plata@mail.com","auth_token":"dc45800fddee07cf9b300d2765283cb2"}}
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
                Log.i("DEBUG:",json.toString());
                //listener.onResponse(1);
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

    //curl -X POST http://104.131.189.224/api/token --data "email=joserracamacho@gmail.com&password=12345678"
    //response:
    public void getToken(final User user){
        RequestParams params = new RequestParams();
        params.add("email", "carlos.ksa21@gmail.com");//user.getEmail());
        params.add("password", "carlosherrera18"); //user.getPassword());

        client.post("http://104.131.189.224/api/token", params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                try {
                    json = json.getJSONObject("user");
                    String auth = json.getString("auth_token");
                    user.setToken(auth);
                    listener.onResponse(user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onError(1, "Error");
            }

            public void onFailure(int i, Header[] header, Throwable e, JSONObject json) {
                listener.onError(1, json.toString());
            }
        });
    }



    //curl -X PUT -H "Authorization: dc45800fddee07cf9b300d2765283cb2" -H "Content-Type: application/json" -d '{"user":{"email":"plata@google.com","password":"asdfasdf"}}' http://localhost:3000/api/user
    //response:
    //{"user":{"id":3,"email":"plata@google.com","auth_token":"dc45800fddee07cf9b300d2765283cb2"}}
    public void updateUser(User user){
        Header[] headers = {
                new BasicHeader("Authorization",user.getToken())
        };

        StringEntity entity  = null;
        String bodyAsJson = "{\"user\":{\"email\":\""+user.getEmail()+"\",\"password\":\""+user.getPassword()+"\"}}";
        try {
            entity = new StringEntity(bodyAsJson);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        client.put(this.context, "http://localhost:3000/api/user", headers, entity,"application/json",new JsonHttpResponseHandler(){
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



    //curl -X DELETE -H "Authorization: dc45800fddee07cf9b300d2765283cb2" http://localhost:3000/api/user
    //response:
    //OK
    public void deleteUser(User user){
        Header[] headers = {
                new BasicHeader("Authorization",user.getToken())
        };

        client.delete(context, "http://localhost:3000/api/user", headers, null, new JsonHttpResponseHandler(){
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
