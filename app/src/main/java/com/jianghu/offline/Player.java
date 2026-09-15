package com.jianghu.offline;

public class Player {
    private String name;
    private int level, exp, hp, maxHp, attack, defense, silver;

    public Player(String name) {
        this.name = name; level = 1; exp = 0; maxHp = 100; hp = maxHp;
        attack = 10; defense = 5; silver = 100;
    }
    public String getName() { return name; }
    public int getLevel() { return level; }
    public int getExp() { return exp; }
    public int getExpToNext() { return level * 100; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public int getSilver() { return silver; }
    public void setName(String name) { this.name = name == null || name.isEmpty() ? "无名侠客" : name; }
    public void setLevel(int level) { this.level = Math.max(1, level); }
    public void setExp(int exp) { this.exp = Math.max(0, exp); }
    public void setHp(int hp) { this.hp = Math.max(0, Math.min(hp, maxHp)); }
    public void setMaxHp(int maxHp) { this.maxHp = Math.max(1, maxHp); if (hp > this.maxHp) hp = this.maxHp; }
    public void setAttack(int attack) { this.attack = Math.max(0, attack); }
    public void setDefense(int defense) { this.defense = Math.max(0, defense); }
    public void setSilver(int silver) { this.silver = Math.max(0, silver); }
    public boolean addExp(int amount) {
        if (amount <= 0) return false;
        exp += amount; boolean leveled = false;
        while (exp >= getExpToNext()) {
            exp -= getExpToNext(); level++; maxHp += 20; attack += 3; defense += 2; hp = maxHp; leveled = true;
        }
        return leveled;
    }
}