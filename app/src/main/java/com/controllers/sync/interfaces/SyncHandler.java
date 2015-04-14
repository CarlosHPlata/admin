package com.controllers.sync.interfaces;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;

/**
 * @(#)JceSecurity.java 1.50 04/04/14
 *
 * Esta clase Determina la estructura con la que las clases de tipo Sync deben ser creadas. para su conexion con el servidor
 *
 * @author Carlos Herrera
 *
 * @version 1.50, 014/04/15
 * @since 1.4
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
