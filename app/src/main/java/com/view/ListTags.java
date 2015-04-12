package com.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.controllers.NoteController;
import com.controllers.TagController;
import com.example.usuario.androidadmin.R;
import com.models.StableArrayAdapter;
import com.models.Tag;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Clase que muestar una lista de todos los tags de las notas, puede agregar, eliminar y actualizar
 * cualquier etiqueta.
 * @author Edgar
 * @version 0.1 10/04/2015.
 *
 */
public class ListTags extends Fragment {
    public ListView listview;
    public View viewTag;

    public ListTags() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true); //Indicamos que este Fragment tiene su propio menu de opciones
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // getMenuInflater().inflate(R.menu.menu_list_notes, menu);
        inflater.inflate(R.menu.menu_list_tags, menu);
        // return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

       if (id == R.id.action_new_tag_list) {
           showModalnewTag();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewTag = inflater.inflate(R.layout.activity_list_tags, container, false);
        listview = (ListView) viewTag.findViewById(R.id.listViewTags);

        tagController = new TagController(getActivity().getApplicationContext());

        initViewListTag();
        return viewTag;
    }

    public void initViewListTag(){
        loadTags();
        showTags();
    }

    public void loadTags(){
        tags = tagController.fingAll();
    }

    public void showTags(){
        dialogNewTag = new AlertDialog.Builder(getActivity());
        txtInput = new EditText(getActivity());
        final ArrayList<String> list = getTagsName();
        final StableArrayAdapter adapter = new StableArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, list);

        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                showModalEditTag(position);

            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
                final int positionAux = position;
                final long positionId = id;
                PopupMenu popup = new PopupMenu(getActivity(), view);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    //MENU FILA
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_eliminar:
                                deleteTag(positionAux);
                            //    Log.e("POP_MENU", "item "+item.getItemId()+" posicion "+positionAux+" id"+positionId
                                initViewListTag();
                                break;
                            default:
                               // Log.e("POP_MENU", "DEFAULT");
                               // Log.e("POP_MENU", "item "+item.getItemId()+" posicion "+positionAux+" id"+positionId);
                                break;
                        }
                        return true;
                    }
                    //FIN MENU FILA
                });
                popup.show();
                return true;
            }
        });
    }

    private void showModalEditTag(int position){
        positionToEdit = position;
        Tag tagAux = tags.get(position);
        dialogNewTag.setTitle("Editar etiqueta");
        txtInput.setText(tagAux.getName());
        dialogNewTag.setView(txtInput);
        dialogNewTag.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nameTag = txtInput.getText().toString();
                if(nameTag != "" && nameTag.length()>0){
                   // Log.e("POP_MENU", "ID update "+positionToEdit+" newText "+nameTag);
                    updateTag(positionToEdit, nameTag);
                }
                initViewListTag();
            }
        });
        dialogNewTag.setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showTags();
            }
        });

        AlertDialog dialogTag = dialogNewTag.create();
        dialogTag.show();

    }

    private void updateTag(int index, String text){
        Tag tag = tags.get(index);
        tag.setName(text);
        tagController.updateTag(tag);
    }

    private void deleteTag(int index){
        Tag tag = tags.get(index);
        tagController.deleteTag(tag);
    }

    private void showModalnewTag(){
        dialogNewTag = new AlertDialog.Builder(getActivity());
        txtInput = new EditText(getActivity());
        dialogNewTag.setTitle("Nuevo tag");
        dialogNewTag.setMessage("Agregar el nombre del nuevo tag");
        dialogNewTag.setView(txtInput);
        dialogNewTag.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nameTag = txtInput.getText().toString();
                if(nameTag != "" && nameTag.length()>0){
                    tagController.addTag(nameTag);
                }
                initViewListTag();
            }
        });
        dialogNewTag.setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showTags();
            }
        });
        AlertDialog dialogTag = dialogNewTag.create();
        dialogTag.show();
    }

    private ArrayList<String> getTagsName(){
        ArrayList<String> titles = new ArrayList<>();
        for(Tag tag: tags){
            titles.add(tag.getName());
        }
        return titles;
    }

    private int positionToEdit = 0;
    private TagController tagController;
    protected ArrayList<Tag> tags;
   private AlertDialog.Builder dialogNewTag;
    private EditText txtInput;
}
