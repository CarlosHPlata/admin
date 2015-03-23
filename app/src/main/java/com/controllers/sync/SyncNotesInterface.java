package com.controllers.sync;

import com.models.Note;

/**
 * Created by Camilo Medina dev.cmedina@gmail.com on 17/03/15.
 */
public interface SyncNotesInterface {
        public void onNoteReceived(Note thisNote);
        public void onErrorReceived(int StatusCode, String error);
}
