package com.controllers.sync.interfaces;

import com.models.Note;

/**
 * @(#)JceSecurity.java 1.50 04/04/14
 *
 * Toda clase que quiera usar una clase Sync debe implementar esta interfaz, ya que los metodos onResponse y onError
 * son los usados por la clase Sync para comunicar cuando la informacion del servidor llega de manera exitosa o no
 * Esta clase funciona como un listener, esperando por los metoso onResponse y onError que la sincronizacion se ralice.
 *
 * @author Carlos Herrera
 *
 * @version 1.50, 014/04/15
 * @since 1.4
 */
public interface SyncInterface {
        public void onResponse(Object response);
        public void onError(int StatusCode, String error);
}
