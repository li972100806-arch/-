package com.jianghu.offline;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveManager {
    private static final String FILE = "jianghu_save";
    public static void save(Context c, Player p, Inventory inv) {
        c.getSharedPreferences(FILE, Context.MODE_PRIVATE).edit()
            .putString("name",p.getName()).putInt("level",p.getLevel()).putInt("exp",p.getExp())
            .putInt("hp",p.getHp()).putInt("maxHp",p.getMaxHp()).putInt("attack",p.getAttack())
            .putInt("defense",p.getDefense()).putInt("silver",p.getSilver())
            .putString("inventory",inv.toSaveString()).apply();
    }
    public static void save(Context c, Player p) { save(c,p,new Inventory()); }
    public static Player load(Context c, Inventory inv) {
        SharedPreferences s=c.getSharedPreferences(FILE,Context.MODE_PRIVATE);
        if(!s.contains("name")) return null;
        Player p=new Player(s.getString("name","无名侠客")); p.setLevel(s.getInt("level",1)); p.setExp(s.getInt("exp",0));
        p.setMaxHp(s.getInt("maxHp",100)); p.setHp(s.getInt("hp",100)); p.setAttack(s.getInt("attack",10));
        p.setDefense(s.getInt("defense",5)); p.setSilver(s.getInt("silver",100)); inv.loadSaveString(s.getString("inventory","")); return p;
    }
    public static Player load(Context c) { return load(c,new Inventory()); }
    public static void clear(Context c) { c.getSharedPreferences(FILE,Context.MODE_PRIVATE).edit().clear().apply(); }
}