package com.example.historygame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class AchieveListAdapter extends BaseAdapter {

    private ArrayList<AchieveListItem> listViewItemList;

    public AchieveListAdapter() {
        this.listViewItemList = new ArrayList<>() ;
    }

    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }

        TextView titleTextView = convertView.findViewById(R.id.text_achieve_title);
        TextView descTextView = convertView.findViewById(R.id.text_achieve_desc);

        AchieveListItem listViewItem = listViewItemList.get(position);

        titleTextView.setText(listViewItem.getTitle());
        descTextView.setText(listViewItem.getDesc());

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    public void addItem(String title, String desc) {
        AchieveListItem item = new AchieveListItem(title, desc);

        listViewItemList.add(item);
    }
}