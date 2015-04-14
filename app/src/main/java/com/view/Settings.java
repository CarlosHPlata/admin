package com.view;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.usuario.androidadmin.R;
/**
 * @(#)JceSecurity.java 1.50 04/04/14
 *
 * Esta clase seencarga de las opciones de configuracion del usuario
 *
 * @author Carlos Herrera
 *
 * @version 1.50, 014/04/15
 * @since 1.4
 */
public class Settings extends Fragment {

    private View view;

    public static Settings newInstance(Bundle arguments) {
        Settings fragment = new Settings();
        if(arguments != null){
            fragment.setArguments(arguments);
        }
        return fragment;
    }

    public Settings() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        Button btn = (Button) view.findViewById(R.id.editButton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true); //Indicamos que este Fragment tiene su propio menu de opciones
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // getMenuInflater().inflate(R.menu.menu_list_notes, menu);
        inflater.inflate(R.menu.menu_settings, menu);
        // return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }
}
