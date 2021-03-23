package com.example.historygame;

public class AchieveListItem {

    private String title;
    private String desc;
    private boolean isCleared;

    public AchieveListItem(String title, String desc) {
        this.title = title;
        this.desc = desc;
        this.isCleared = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isCleared() {
        return isCleared;
    }

    public void setCleared(boolean cleared) {
        isCleared = cleared;
    }
}
