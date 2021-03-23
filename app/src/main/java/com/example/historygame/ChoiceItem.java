package com.example.historygame;

class ChoiceItem{
    private String text;
    private String requireAbility;
    private String acquireAbility;
    private String nextEvent;
    private int health;
    private int mental;
    private int money;

    ChoiceItem(String[] choiceStr){
        if(choiceStr.length != 7){
            throw new ArrayIndexOutOfBoundsException("choiceItem 생성자에 choiceStr이상함");
        }

        this.requireAbility = choiceStr[0];
        this.text = choiceStr[1];
        this.nextEvent = choiceStr[2];
        this.acquireAbility = choiceStr[3];

        int[] status = new int[3];
        for(int i = 0; i<3;i++){
            if(choiceStr[i+4].equals("")){
                status[i] = 0;
            }
            else{
                status[i] = Integer.parseInt(choiceStr[i+4]);
            }
        }

        this.health = status[0];
        this.mental = status[1];
        this.money = status[2];
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRequireAbility() {
        return requireAbility;
    }

    public void setRequireAbility(String requireAbility) {
        this.requireAbility = requireAbility;
    }

    public String getAcquireAbility() {
        return acquireAbility;
    }

    public void setAcquireAbility(String acquireAbility) {
        this.acquireAbility = acquireAbility;
    }

    public String getNextEvent() {
        return nextEvent;
    }

    public void setNextEvent(String nextEvent) {
        this.nextEvent = nextEvent;
    }

    public int getHealth() { return health; }

    public void setHealth(int health) { this.health = health; }

    public int getMental() { return mental; }

    public void setMental(int mental) { this.mental = mental; }

    public int getMoney() { return money; }

    public void setMoney(int money) { this.money = money; }
}
