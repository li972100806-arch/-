package com.jianghu.offline;

/** Simple offline equipment model with slot, quality and enhancement. */
public class Equipment {
    public enum Quality { 普通, 精良, 稀有, 史诗, 传说 }
    private final String slot;
    private final String name;
    private final Quality quality;
    private int level;
    private int attack;
    private int defense;

    public Equipment(String slot, String name, Quality quality, int attack, int defense) {
        this.slot = slot; this.name = name; this.quality = quality;
        this.attack = Math.max(0, attack); this.defense = Math.max(0, defense);
    }
    public String getSlot(){ return slot; }
    public String getName(){ return name; }
    public Quality getQuality(){ return quality; }
    public int getLevel(){ return level; }
    public int getAttack(){ return attack; }
    public int getDefense(){ return defense; }
    public void enhance(){ level++; attack += Math.max(1, quality.ordinal()+1); defense += Math.max(1, quality.ordinal()); }
    public String display(){ return name+" · "+quality+" +"+level+"（攻"+attack+" 防"+defense+"）"; }
}
