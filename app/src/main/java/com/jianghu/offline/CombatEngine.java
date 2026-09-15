package com.jianghu.offline;

public class CombatEngine {
    public static int playerDamage(Player player, Enemy enemy) {
        return Math.max(1, player.getAttack() - enemy.getDefense());
    }

    public static int enemyDamage(Player player, Enemy enemy) {
        return Math.max(1, enemy.getAttack() - player.getDefense());
    }

    public static boolean playerAttack(Player player, Enemy enemy) {
        enemy.damage(playerDamage(player, enemy));
        return !enemy.isAlive();
    }

    public static void enemyAttack(Player player, Enemy enemy) {
        player.setHp(player.getHp() - enemyDamage(player, enemy));
    }
}
