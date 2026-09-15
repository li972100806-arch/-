package com.jianghu.offline;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.Random;

public class JianghuEngine {
    public final String[] maps={"稻花村","青石镇","黑风山","落霞谷","寒月岭","苍月岛","雷炎洞穴","卧龙谷"};
    public final int[] mapReq={1,10,25,50,100,180,260,360};
    public final String[] enemies={"山鸡","流寇","黑风匪","落霞妖狐","寒月剑客","苍月兽","雷炎魔将","卧龙邪兽"};
    public final String[] skills={"基础剑术","青云掌","烈火功","厚土诀","剑心诀","怒魂诀"};
    public int mapIndex,skillLevel,equipmentPower,petLevel,petExp;
    public long lastTime;
    private final Random random=new Random();

    public void load(Context c){
        SharedPreferences s=c.getSharedPreferences("jianghu_extra",Context.MODE_PRIVATE);
        mapIndex=Math.max(0,Math.min(maps.length-1,s.getInt("map",0)));
        skillLevel=Math.max(1,s.getInt("skill",1)); equipmentPower=Math.max(0,s.getInt("equip",0));
        petLevel=Math.max(1,s.getInt("pet",1)); petExp=Math.max(0,s.getInt("petExp",0));
        lastTime=s.getLong("time",System.currentTimeMillis());
    }
    public int applyOffline(Player p){
        long minutes=Math.max(0,Math.min(360,(System.currentTimeMillis()-lastTime)/60000));
        if(minutes==0)return 0; int exp=(int)minutes*(8+mapIndex*3); p.addExp(exp); p.setSilver(p.getSilver()+(int)minutes*(3+mapIndex)); return (int)minutes;
    }
    public void save(Context c){
        c.getSharedPreferences("jianghu_extra",Context.MODE_PRIVATE).edit().putInt("map",mapIndex).putInt("skill",skillLevel).putInt("equip",equipmentPower).putInt("pet",petLevel).putInt("petExp",petExp).putLong("time",System.currentTimeMillis()).apply();
    }
    public Enemy spawn(Player p){
        int lv=p.getLevel(), base=25+mapIndex*30+lv*6;
        return new Enemy(enemies[mapIndex],base,7+mapIndex*4+lv*2,2+mapIndex*2+lv,30+mapIndex*25+lv*12,12+mapIndex*8+lv*4);
    }
    public int damage(Player p,Enemy e){return Math.max(1,CombatEngine.playerDamage(p,e)+skillLevel*2+equipmentPower/3+petLevel);}
    public boolean drop(Inventory inv){if(random.nextInt(100)<35){inv.add("material",1+mapIndex);return true;}return false;}
    public boolean trainSkill(Player p){int cost=skillLevel*35;if(playerCanPay(p,cost)&&skillLevel<skills.length*10){p.setSilver(p.getSilver()-cost);skillLevel++;return true;}return false;}
    public boolean craft(Player p,Inventory inv){if(inv.getCount("material")<2)return false;inv.remove("material",2);equipmentPower+=4+mapIndex;p.setAttack(p.getAttack()+1+mapIndex/2);return true;}
    public boolean trainPet(Player p){int cost=petLevel*20;if(!playerCanPay(p,cost))return false;p.setSilver(p.getSilver()-cost);petExp+=5;if(petExp>=petLevel*10){petExp=0;petLevel++;}return true;}
    private boolean playerCanPay(Player p,int n){return p.getSilver()>=n;}
}
