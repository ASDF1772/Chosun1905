package com.example.historygame;

import android.util.Log;

import java.util.ArrayList;

public class EventSelector {

    private static EventSelector eventSelector;

    private ArrayList<String> enableEventList;
    public String nextEvent;
    public boolean isEnd;
    public int eventCount;

    private EventSelector() {
        enableEventList = new ArrayList<>();
        eventCount = 0;
        initEventList();
    }

    public static EventSelector getInstance() {
        if(eventSelector == null)
        {
            eventSelector = new EventSelector();
        }

        return eventSelector;
    }

    public void initEventList() {
        enableEventList.add("meet_bosang");
        enableEventList.add("meet_busang");
        enableEventList.add("enter_pyega1");
        enableEventList.add("enter_pyega2");
        enableEventList.add("discover_jjangdol");
        enableEventList.add("go_dongdaemun");
        enableEventList.add("gloomy_night");

        nextEvent = "starting";
        isEnd = false;
    }

    public String selectEvent() {
        String eventName;

        //다음 이벤트 있는지 없는지
        if (nextEvent.equals("")) {
            if (enableEventList.isEmpty()) {
                isEnd = true;
                return "ending";
            }

            int index = (int) (Math.random() * enableEventList.size());
            eventName = enableEventList.get(index);

            enableEventList.remove(eventName);

            eventCount++;

            //일정 기간 후 메인 이벤트 추가
            addMainEvent();
        }
        else {
            eventName = nextEvent;
            nextEvent = "";

            if(eventName.contains("ending"))
            {
                isEnd = true;
            }
        }

        //연계 이벤트 추가
        addLinkEvent(eventName);

        for (String s :
                enableEventList) {
            Log.d("asdf", s);
        }
        Log.d("asdf", "----------------------------");

        return eventName;
    }

    public void addLinkEvent(String event){
        if(event.equals("suksudong")){
            enableEventList.add("jjangdol_plan");
        }
        else if(event.equals("endure_ito_kill")){
            enableEventList.add("daehanuigun_formation");
        }
    }

    public void addMainEvent(){
        switch (eventCount){
            case 3: enableEventList.add("eulsaneugyak"); break;
        }
    }
}
