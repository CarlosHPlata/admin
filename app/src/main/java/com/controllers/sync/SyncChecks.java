package com.controllers.sync;

import android.content.Context;
import android.util.Log;

import com.controllers.sync.interfaces.SyncHandler;
import com.controllers.sync.interfaces.SyncInterface;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.models.CheckList;
import com.models.mappers.CheckListMapper;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by Usuario on 06/05/2015.
 */
public class SyncChecks extends SyncHandler{

    public SyncChecks(Context context, SyncInterface listener) {
        super(context, listener, new AsyncHttpClient());
        checkListMapper = new CheckListMapper(this.context);
    }

    //curl -X POST -H "Authorization: 68f582de1c5439ae3a0a4f6a050822ac"
    // -H "Content-Type: application/json" -d
    // '{"checklist_item":{"description":"eaas"}}'
    // http://localhost:3000/api/notes/3/checklist_items
    // RESPONSE:
    // {"checklist_item":{"id":3,"description":"eaas","checked":false}}
    public void createCheck(String token, final CheckList check){
        Header[] headers = {
                new BasicHeader("Authorization", token),
                new BasicHeader("Content-type", "application/json")
        };

        String bodyAsJson = "{\"checklist_item\":{\"description\":\""+check.getDescription()+"\",\"checked\":"+check.isChecked()+"}}";

        Log.i("IDEON", bodyAsJson);

        StringEntity entity = null;

        try{
            entity = new StringEntity(bodyAsJson);
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        client.post(this.context, "http://104.131.189.224/api/notes/3/checklist_items", headers, entity,  "application/json", new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONObject json){
                Log.i("IDEON", "success");
                try{
                    JSONObject checkJson = json.getJSONObject("checklist_item");
                    check.setExtId(checkJson.getInt("id"));
                    check.setSyncFlag(true);
                    checkListMapper.updateCheckList(check);
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }

            public void onFailure(int i, Header[] header, Throwable e, JSONObject json) {
                Log.i("IDEON", "failure tag"+json.toString());
            }
        });
    }

    //curl -X PUT -H "Authorization: 68f582de1c5439ae3a0a4f6a050822ac"
    // -H "Content-Type: application/json" -d
    // '{"checklist_item":{"description":"offofofo"}}'
    // http://localhost:3000/api/notes/3/checklist_items/2
    // RESPONSE
    // {"checklist_item":{"id":2,"description":"offofofo","checked":false}}
    public void updateCheck(String token, final CheckList check){
        Header [] headers = {
                new BasicHeader("Authorization", token),
                new BasicHeader("Content-type", "application/json")
        };

        String bodyAsJson = "{\"checklist_item\":{\"description\":\""+check.getDescription()+"\",\"checked\":"+check.isChecked()+"}}";;

        Log.i("IDEON", bodyAsJson);

        StringEntity entity = null;
        try{
            entity = new StringEntity(bodyAsJson);
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        client.put(this.context, "http://104.131.189.224/api/notes/3/checklist_items/2"+check.getExtId(),headers, entity, "application/json", new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                check.setSyncFlag(true);
                checkListMapper.updateCheckList(check);
            }

            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("IDEON", "error");
            }

            public void onFailure(int i, Header[] header, Throwable e, JSONObject json){
                Log.i("IDEON2", "error");
            }
        });
    }

    private CheckListMapper checkListMapper;
}
