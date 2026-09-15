package com.jianghu.offline;

public class Enemy {
    private final String name;
    private final int maxHp;
    private int hp;
    private final int attack;
    private final int defense;
    private final int exp;
    private final int silver;

    public Enemy(String name, int hp, int attack, int defense, int exp, int silver) {
        this.name = name;
        this.maxHp = hp;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.exp = exp;
        this.silver = silver;
    }

    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public int getExp() { return exp; }
    public int getSilver() { return silver; }
    public void damage(int value) { hp = Math.max(0, hp - Math.max(0, value)); }
    public boolean isAlive() { return hp > 0; }
}
