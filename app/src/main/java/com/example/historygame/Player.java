package com.example.historygame;

import java.util.ArrayList;

public class Player {
    private static Player player;
    private ArrayList<String> abilities;
    private int health;
    private int mental;
    private int money;
    private int pageNum;

    Player(){
        abilities = new ArrayList<>();
        init();
    }

    //Singleton
    public static Player getPlayer() {
        if(player == null)
        {
            player = new Player();
        }

        return player;
    }

    public ArrayList<String> getAbilities() {
        return abilities;
    }

    public void init(){
        health = 3;
        mental = 3;
        money = 3;
        pageNum = 1;
        abilities.clear();
    }

    public boolean searchAbility(String ability)
    {
        if(ability.equals("체력")){
            if(health > 0) {
                return true;
            }
            else{
                return false;
            }
        }
        else if(ability.equals("정신력")){
            if(mental > 0){
                return true;
            }
            else{
                return false;
            }
        }
        else if(ability.equals("돈")){
            if(money > 0){
                return true;
            }
            else{
                return false;
            }
        }

        if(abilities.contains(ability)){
            return true;
        }
        else{
            return false;
        }
    }
    //추가/삭제 되었는지 boolean 리턴
    public boolean addAbility(String ability)
    {
        if(!searchAbility(ability)){
            abilities.add(ability);
            return true;
        }

        return false;
    }

    public boolean removeAbility(String ability)
    {
        if(searchAbility(ability)){
            abilities.remove(ability);
            return true;
        }

        return false;
    }

    public int getHealth() {
        return health;
    }

    public int getMental() {
        return mental;
    }

    public int getMoney() {
        return money;
    }

    public int getPageNum(){
        return pageNum;
    }

    public void increasePageNum(){
        pageNum++;
    }

    public void decreaseHealth()
    {
        health--;
    }

    public void decreaseMental()
    {
        mental--;
    }

    public void decreaseMoney()
    {
        money--;
    }

    public void increaseHealth()
    {
        health++;
    }

    public void increaseMental()
    {
        mental++;
    }

    public void increaseMoney()
    {
        money++;
    }
}
