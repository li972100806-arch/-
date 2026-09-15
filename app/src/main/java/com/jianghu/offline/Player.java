package com.jianghu.offline;

public class Player {
    private String name;
    private int level;
    private int hp;
    private int maxHp;
    private int attack;
    private int defense;
    private int silver;

    public Player(String name) {
        this.name = name;
        this.level = 1;
        this.maxHp = 100;
        this.hp = maxHp;
        this.attack = 10;
        this.defense = 5;
        this.silver = 100;
    }

    public String getName() { return name; }
    public int getLevel() { return level; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public int getSilver() { return silver; }

    public void setName(String name) { this.name = name; }
    public void setLevel(int level) { this.level = level; }
    public void setHp(int hp) { this.hp = Math.max(0, Math.min(hp, maxHp)); }
    public void setMaxHp(int maxHp) { this.maxHp = Math.max(1, maxHp); }
    public void setAttack(int attack) { this.attack = Math.max(0, attack); }
    public void setDefense(int defense) { this.defense = Math.max(0, defense); }
    public void setSilver(int silver) { this.silver = Math.max(0, silver); }
}
