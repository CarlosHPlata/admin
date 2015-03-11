package com.model;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.HashMap;
import java.util.List;

/**
 * Created by José Ramón Díaz on 13/02/2015.
 * Adaptador para que se puedan presentar las notas en una lista
 */
public class StableArrayAdapter extends ArrayAdapter<String> {

    public StableArrayAdapter(Context context, int textViewResourceId, List<String> objects) {
        super(context, textViewResourceId, objects);
        for (int i = 0; i < objects.size(); ++i) {
            mIdMap.put(objects.get(i), i);
        }
    }

    @Override
    public long getItemId(int position) {
        String item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();
}