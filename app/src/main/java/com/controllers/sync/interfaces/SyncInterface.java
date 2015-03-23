package com.controllers.sync.interfaces;

import com.models.Note;

/**
 * Created by Camilo Medina dev.cmedina@gmail.com on 17/03/15.
 */
public interface SyncInterface {
        public void onResponse(Object response);
        public void onError(int StatusCode, String error);
}
