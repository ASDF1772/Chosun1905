package com.example.historygame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AbilityGridAdapter extends BaseAdapter {

    private ArrayList<String> abilities;

    public AbilityGridAdapter(ArrayList<String> abilities) {
        this.abilities = abilities;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_item, parent, false);

        }

        TextView textView = convertView.findViewById(R.id.grid_item_text);
        textView.setText(abilities.get(position));

        return convertView;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public int getCount() {
        return abilities.size();
    }

    @Override
    public String getItem(int position) {
        return abilities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}