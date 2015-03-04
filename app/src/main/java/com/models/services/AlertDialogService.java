package com.models.services;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.example.usuario.androidadmin.R;

/** *
 *
 * Esta clase modifica el elemento Alert para mostrar mensajes y acciones personalizadas
 * en la aplicación.
 *
 * @author Edgar
 *
 * @version 0.1, 28/02/2015.
 *
 * */
public class AlertDialogService {

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        alertDialog.setTitle(title);
        alertDialog.setMessage(message);

        if(status != null)
            alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog.show();
    }
}
