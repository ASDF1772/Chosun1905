package com.example.historygame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ChoiceItemAdapter extends BaseAdapter {
    public ArrayList<ChoiceItem> choiceItemList;

    public ChoiceItemAdapter() {
        choiceItemList = new ArrayList<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.choice_item, parent, false);
        }

        TextView requireAbilityText = convertView.findViewById(R.id.text_require_ability);
        TextView text = convertView.findViewById(R.id.text_choice_contents);

        ChoiceItem choiceItem = choiceItemList.get(position);

        requireAbilityText.setText(choiceItem.getRequireAbility());
        text.setText(choiceItem.getText());

        String requireAbility = choiceItem.getRequireAbility();

        if(requireAbility.equals("")) {
            requireAbilityText.setVisibility(View.GONE);
            convertView.setForeground(null);
        }
        else if(Player.getPlayer().searchAbility(requireAbility)){
            requireAbilityText.setVisibility(View.VISIBLE);
            requireAbilityText.setTextColor(ContextCompat.getColor(context, R.color.colorGain));
            convertView.setForeground(null);
        }
        else{
            requireAbilityText.setVisibility(View.VISIBLE);
            requireAbilityText.setTextColor(ContextCompat.getColor(context, R.color.colorLose));
            convertView.setForeground(ContextCompat.getDrawable(context, R.drawable.filter_choice_item));
        }

        return convertView;
    }

    public void addItem(ChoiceItem choiceItem){
        choiceItemList.add(choiceItem);
    }

    public void clear(){
        choiceItemList.clear();
    }

    @Override
    public int getCount() {
        return choiceItemList.size();
    }

    @Override
    public ChoiceItem getItem(int position) {
        return choiceItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
