package com.jianghu.offline;

import java.util.Random;

/** Offline battle calculations. No network or background service required. */
public class CombatEngine {
    private static final Random RANDOM = new Random();

    public static int playerDamage(Player player, Enemy enemy) {
        // 10% miss, 15% critical hit, otherwise 90%-110% damage variance.
        if (RANDOM.nextInt(100) < 10) return 0;
        int base = Math.max(1, player.getAttack() - enemy.getDefense());
        if (RANDOM.nextInt(100) < 15) base *= 2;
        int variance = 90 + RANDOM.nextInt(21);
        return Math.max(1, base * variance / 100);
    }

    public static int enemyDamage(Player player, Enemy enemy) {
        if (RANDOM.nextInt(100) < 7) return 0;
        int base = Math.max(1, enemy.getAttack() - player.getDefense());
        int variance = 90 + RANDOM.nextInt(21);
        return Math.max(1, base * variance / 100);
    }

    public static boolean playerAttack(Player player, Enemy enemy) {
        int damage = playerDamage(player, enemy);
        if (damage > 0) enemy.damage(damage);
        return !enemy.isAlive();
    }

    public static void enemyAttack(Player player, Enemy enemy) {
        int damage = enemyDamage(player, enemy);
        if (damage > 0) player.setHp(player.getHp() - damage);
    }
}
