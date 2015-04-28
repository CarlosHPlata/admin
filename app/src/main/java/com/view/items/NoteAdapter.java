package com.view.items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.usuario.androidadmin.R;
import com.models.Note;

import java.util.ArrayList;

/**
 * Created by Usuario on 25/04/2015.
 */
public class NoteAdapter extends BaseAdapter {

    private ArrayList<Note> notes;
    private Context context;

    public NoteAdapter(ArrayList<Note> notes, Context context) {
        this.notes = notes;
        this.context = context;
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int position) {
        return notes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return notes.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.item_note, parent, false);

        TextView title = (TextView) row.findViewById(R.id.note_title);
        TextView description = (TextView) row.findViewById(R.id.note_description);

        Note temp = notes.get(position);

        String desc = temp.getBody();
        if (desc.length() > 20){
            desc = desc.substring(0, 15) + "...";
        }

        title.setText(temp.getTitle());
        description.setText(desc);

        return row;
    }
}
