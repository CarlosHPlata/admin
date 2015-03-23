package com.controllers.sync.interfaces;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;

/**
 * Created by Usuario on 22/03/2015.
 */
public abstract class SyncHandler {

    protected static AsyncHttpClient client;
    protected Context context;
    protected SyncInterface listener;

    protected SyncHandler(Context context, SyncInterface listener, AsyncHttpClient client) {
        this.context = context;
        this.listener = listener;
        this.client = client;
    }

}
