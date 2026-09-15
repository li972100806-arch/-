package com.jianghu.offline;
import android.content.Context;import android.content.SharedPreferences;import java.util.Random;
public class JianghuEngine{
 public final String[] maps={"稻花村","青石镇","黑风山","落霞谷","寒月岭","苍月岛","雷炎洞穴","卧龙谷"};
 public final int[] mapReq={1,10,25,50,100,180,260,360};
 public final String[] enemies={"山鸡","流寇","黑风匪","落霞妖狐","寒月剑客","苍月兽","雷炎魔将","卧龙邪兽"};
 public final String[] skills={"基础剑术","青云掌","烈火功","厚土诀","剑心诀","怒魂诀"};
 public int mapIndex,skillLevel,equipmentPower,petLevel,petExp,sectLevel,artifactLevel,outfitLevel,towerFloor,travel,bounty,honor;
 public long lastTime;private final Random random=new Random();
 public void load(Context c){SharedPreferences s=c.getSharedPreferences("jianghu_extra",Context.MODE_PRIVATE);mapIndex=Math.max(0,Math.min(maps.length-1,s.getInt("map",0)));skillLevel=Math.max(1,s.getInt("skill",1));equipmentPower=Math.max(0,s.getInt("equip",0));petLevel=Math.max(1,s.getInt("pet",1));petExp=Math.max(0,s.getInt("petExp",0));sectLevel=Math.max(1,s.getInt("sect",1));artifactLevel=Math.max(0,s.getInt("artifact",0));outfitLevel=Math.max(0,s.getInt("outfit",0));towerFloor=Math.max(0,s.getInt("tower",0));travel=Math.max(0,s.getInt("travel",0));bounty=Math.max(0,s.getInt("bounty",0));honor=Math.max(0,s.getInt("honor",0));lastTime=s.getLong("time",System.currentTimeMillis());}
 public int applyOffline(Player p){long m=Math.max(0,Math.min(360,(System.currentTimeMillis()-lastTime)/60000));if(m==0)return 0;int exp=(int)m*(8+mapIndex*3);p.addExp(exp);p.setSilver(p.getSilver()+(int)m*(3+mapIndex));return(int)m;}
 public void save(Context c){c.getSharedPreferences("jianghu_extra",Context.MODE_PRIVATE).edit().putInt("map",mapIndex).putInt("skill",skillLevel).putInt("equip",equipmentPower).putInt("pet",petLevel).putInt("petExp",petExp).putInt("sect",sectLevel).putInt("artifact",artifactLevel).putInt("outfit",outfitLevel).putInt("tower",towerFloor).putInt("travel",travel).putInt("bounty",bounty).putInt("honor",honor).putLong("time",System.currentTimeMillis()).apply();}
 public Enemy spawn(Player p){int lv=p.getLevel(),base=25+mapIndex*30+lv*6;return new Enemy(enemies[mapIndex],base,7+mapIndex*4+lv*2,2+mapIndex*2+lv,30+mapIndex*25+lv*12,12+mapIndex*8+lv*4);}
 public int damage(Player p,Enemy e){return Math.max(1,CombatEngine.playerDamage(p,e)+skillLevel*2+equipmentPower/3+petLevel+artifactLevel+outfitLevel);}
 public boolean drop(Inventory inv){if(random.nextInt(100)<35){inv.add("material",1+mapIndex);return true;}return false;}
 public boolean trainSkill(Player p){int cost=skillLevel*35;if(pay(p,cost)&&skillLevel<skills.length*10){p.setSilver(p.getSilver()-cost);skillLevel++;return true;}return false;}
 public boolean craft(Player p,Inventory inv){if(inv.getCount("material")<2)return false;inv.remove("material",2);equipmentPower+=4+mapIndex;p.setAttack(p.getAttack()+1+mapIndex/2);return true;}
 public boolean trainPet(Player p){int cost=petLevel*20;if(!pay(p,cost))return false;p.setSilver(p.getSilver()-cost);petExp+=5;if(petExp>=petLevel*10){petExp=0;petLevel++;}return true;}
 public boolean upgradeSect(Player p){int cost=sectLevel*80;if(!pay(p,cost))return false;p.setSilver(p.getSilver()-cost);sectLevel++;p.setDefense(p.getDefense()+1);return true;}
 public boolean marketSkill(Player p){if(!pay(p,120))return false;p.setSilver(p.getSilver()-120);skillLevel=Math.min(skills.length*10,skillLevel+3);return true;}
 public boolean temperArtifact(Player p){int cost=(artifactLevel+1)*100;if(!pay(p,cost))return false;p.setSilver(p.getSilver()-cost);artifactLevel++;p.setAttack(p.getAttack()+2);return true;}
 public boolean upgradeOutfit(Player p){if(!pay(p,(outfitLevel+1)*70))return false;p.setSilver(p.getSilver()-(outfitLevel+1)*70);outfitLevel++;p.setMaxHp(p.getMaxHp()+8);return true;}
 public boolean tower(Player p){int need=(towerFloor+1)*2;if(p.getLevel()<need)return false;towerFloor++;p.setSilver(p.getSilver()+30+towerFloor*10);p.addExp(20+towerFloor*5);return true;}
 public boolean travel(Player p){travel+=10;if(travel>=100){travel=0;p.setSilver(p.getSilver()+100);p.addExp(100);return true;}return false;}
 public String bounty(Player p){if(bounty>=3)return "今日悬赏已完成3单，明日再来。";bounty++;int reward=60+mapIndex*30;honor+=10+mapIndex*5;p.setSilver(p.getSilver()+reward);p.addExp(50+mapIndex*20);return "悬赏完成！获得"+reward+"两，侠义值 +"+(10+mapIndex*5)+"。";}
 public String retreat(Player p){int cost=100+sectLevel*40;if(!pay(p,cost))return "闭关需要"+cost+"两。";p.setSilver(p.getSilver()-cost);p.addExp(120+skillLevel*10);p.setHp(p.getMaxHp());return "闭关修炼完成，功力大进，经验提升。";}
 private boolean pay(Player p,int n){return p.getSilver()>=n;}
}
