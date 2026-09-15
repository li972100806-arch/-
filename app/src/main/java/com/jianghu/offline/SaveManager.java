package com.jianghu.offline;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveManager {
    private static final String FILE = "jianghu_save";

    public static void save(Context context, Player player) {
        SharedPreferences.Editor editor = context.getSharedPreferences(FILE, Context.MODE_PRIVATE).edit();
        editor.putString("name", player.getName());
        editor.putInt("level", player.getLevel());
        editor.putInt("hp", player.getHp());
        editor.putInt("maxHp", player.getMaxHp());
        editor.putInt("attack", player.getAttack());
        editor.putInt("defense", player.getDefense());
        editor.putInt("silver", player.getSilver());
        editor.apply();
    }

    public static Player load(Context context) {
        SharedPreferences p = context.getSharedPreferences(FILE, Context.MODE_PRIVATE);
        if (!p.contains("name")) return null;

        Player player = new Player(p.getString("name", "无名侠客"));
        player.setLevel(p.getInt("level", 1));
        player.setMaxHp(p.getInt("maxHp", 100));
        player.setHp(p.getInt("hp", 100));
        player.setAttack(p.getInt("attack", 10));
        player.setDefense(p.getInt("defense", 5));
        player.setSilver(p.getInt("silver", 100));
        return player;
    }

    public static void clear(Context context) {
        context.getSharedPreferences(FILE, Context.MODE_PRIVATE).edit().clear().apply();
    }
}
