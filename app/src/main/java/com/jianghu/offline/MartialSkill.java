package com.jianghu.offline;

/** Offline martial-art skill with level, cost and combat multiplier. */
public class MartialSkill {
    private final String name;
    private final String description;
    private int level;
    private int cooldown;

    public MartialSkill(String name, String description, int cooldown) {
        this.name=name; this.description=description; this.cooldown=Math.max(0,cooldown);
        this.level=1;
    }
    public String getName(){return name;}
    public String getDescription(){return description;}
    public int getLevel(){return level;}
    public int getCooldown(){return cooldown;}
    public int cost(){return level*25;}
    public int multiplier(){return 100 + level*12;}
    public boolean train(Player p){
        int c=cost();
        if(p.getSilver()<c)return false;
        p.setSilver(p.getSilver()-c); level++; return true;
    }
    public String display(){return name+" Lv."+level+" · "+description+" · 消耗"+cost()+"两";}
}
