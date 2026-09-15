package com.jianghu.offline;

import java.util.Random;

/** Offline battle calculations. No network or background service required. */
public class CombatEngine {
    private static final Random RANDOM = new Random();
    private static boolean lastPlayerMiss;
    private static boolean lastPlayerCrit;
    private static boolean lastEnemyMiss;

    public static int playerDamage(Player player, Enemy enemy) {
        lastPlayerMiss = RANDOM.nextInt(100) < 10;
        lastPlayerCrit = false;
        if (lastPlayerMiss) return 0;
        int base = Math.max(1, player.getAttack() - enemy.getDefense());
        if (RANDOM.nextInt(100) < 15) {
            base *= 2;
            lastPlayerCrit = true;
        }
        int variance = 90 + RANDOM.nextInt(21);
        return Math.max(1, base * variance / 100);
    }

    public static int enemyDamage(Player player, Enemy enemy) {
        lastEnemyMiss = RANDOM.nextInt(100) < 7;
        if (lastEnemyMiss) return 0;
        int base = Math.max(1, enemy.getAttack() - player.getDefense());
        int variance = 90 + RANDOM.nextInt(21);
        return Math.max(1, base * variance / 100);
    }

    public static boolean wasPlayerMiss() { return lastPlayerMiss; }
    public static boolean wasPlayerCrit() { return lastPlayerCrit; }
    public static boolean wasEnemyMiss() { return lastEnemyMiss; }

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
