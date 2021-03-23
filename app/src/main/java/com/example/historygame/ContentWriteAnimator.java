package com.example.historygame;

import android.os.Handler;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class ContentWriteAnimator {

    private String writeText;
    private int writeIndex;
    private int writeDelay;
    private Handler writeHandler;

    private ListView choiceList;
    private View choiceListBottomDivider;
    private TextView contents;


    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            contents.setText(writeText.subSequence(0, writeIndex++));

            if(writeIndex <= writeText.length()){
                writeHandler.postDelayed(characterAdder, writeDelay);
            }
            else{
                finishAnimation();
            }
        }
    };

    public ContentWriteAnimator(ListView choiceList, View choiceListBottomDivider, TextView contents){
        this.choiceList = choiceList;
        this.choiceListBottomDivider = choiceListBottomDivider;
        this.contents = contents;
        writeHandler = new Handler();
        writeDelay = 35;
    }

    public void animateText(String writeText){
        this.writeText = writeText;
        writeIndex = 0;

        contents.setText("");

        writeHandler.removeCallbacks(characterAdder);
        writeHandler.postDelayed(characterAdder, writeDelay);
    }

    public void finishAnimation(){
        contents.setText(writeText);

        writeIndex = writeText.length();

        choiceList.setVisibility(View.VISIBLE);
        choiceListBottomDivider.setVisibility(View.VISIBLE);
    }

    public void setWriteDelay(int writeDelay) {
        this.writeDelay = writeDelay;
    }


}
