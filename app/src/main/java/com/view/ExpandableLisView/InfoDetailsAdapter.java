package com.view.ExpandableLisView;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Clase que extiende de un ExpandableListAdapter, usado para los pliegues de la nota,
 * esta clase puede ser personalizada para agregar elementos a la lista de pliegues.
 * Created by Edgar on 21/04/2015.
 */
public class InfoDetailsAdapter extends BaseExpandableListAdapter {
    Activity activity;
    List<String> group;
    List<List<String>> child;
    public InfoDetailsAdapter(Activity a, List<String> group,
                              List<List<String>> child) {
        activity = a;
        this.group = group;
        this.child = child;
    }
    // child method stub
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        // System.out.println("*******************"+child.get(groupPosition).get(childPosition));
        return child.get(groupPosition).get(childPosition);
    }
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }
    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        return child.get(groupPosition).size();
    }
    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        String string = child.get(groupPosition).get(childPosition);
        return getGenericView(string);
    }
    // group method stub
    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return group.get(groupPosition);
    }
    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }
    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return group.size();
    }
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        String string = group.get(groupPosition);
        return getGenericView(string);
    }
    // View stub to create Group/Children 's View
    public TextView getGenericView(String s) {
        // Layout parameters for the ExpandableListView
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, 64);
        TextView text = new TextView(activity);
        text.setLayoutParams(lp);
        // Center the text vertically
        text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        // Set the text starting position
        text.setPadding(36, 0, 0, 0);
        text.setText(s);
        return text;
    }
    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }
}
