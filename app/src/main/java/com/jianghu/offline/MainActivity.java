package com.jianghu.offline;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
    private Player player; private Inventory inventory; private Enemy enemy; private TextView status, enemyView;
    private final Item medicine = new Item("medicine", "金疮药", "恢复50点生命", 50, 20);
    @Override public void onCreate(Bundle b) { super.onCreate(b); setContentView(R.layout.activity_main);
        status=findViewById(R.id.tvStatus); enemyView=findViewById(R.id.tvEnemy);
        Button explore=findViewById(R.id.btnExplore), attack=findViewById(R.id.btnAttack), heal=findViewById(R.id.btnHeal), buy=findViewById(R.id.btnBuy), use=findViewById(R.id.btnUse), save=findViewById(R.id.btnSave), reset=findViewById(R.id.btnReset);
        inventory=new Inventory(); player=SaveManager.load(this,inventory); if(player==null){player=new Player("无名侠客"); inventory.add("medicine",1);} refresh();
        explore.setOnClickListener(v->{ if(player.getHp()<=0){player.setHp(player.getMaxHp());} enemy=new Enemy("山贼",55+player.getLevel()*10,8+player.getLevel()*2,3+player.getLevel(),60+player.getLevel()*10,30+player.getLevel()*5); refresh(); });
        attack.setOnClickListener(v->{ if(enemy==null||!enemy.isAlive()||player.getHp()<=0)return; boolean dead=CombatEngine.playerAttack(player,enemy); if(dead){player.addExp(enemy.getExp());player.setSilver(player.getSilver()+enemy.getSilver());enemy=null;} else CombatEngine.enemyAttack(player,enemy); SaveManager.save(this,player,inventory); refresh(); });
        heal.setOnClickListener(v->{player.setHp(player.getMaxHp()); SaveManager.save(this,player,inventory); refresh();});
        buy.setOnClickListener(v->{if(player.getSilver()>=medicine.getPrice()){player.setSilver(player.getSilver()-medicine.getPrice());inventory.add(medicine.getId(),1);SaveManager.save(this,player,inventory);refresh();}});
        use.setOnClickListener(v->{if(inventory.remove(medicine.getId(),1)){player.setHp(player.getHp()+medicine.getHeal());SaveManager.save(this,player,inventory);refresh();}});
        save.setOnClickListener(v->{SaveManager.save(this,player,inventory);refresh();});
        reset.setOnClickListener(v->{SaveManager.clear(this);player=new Player("无名侠客");inventory.clear();inventory.add("medicine",1);enemy=null;refresh();});
    }
    private void refresh(){status.setText("侠客："+player.getName()+"\n等级："+player.getLevel()+"  经验："+player.getExp()+"/"+player.getExpToNext()+"\n生命："+player.getHp()+"/"+player.getMaxHp()+"\n攻击："+player.getAttack()+"  防御："+player.getDefense()+"\n银两："+player.getSilver()+"\n金疮药："+inventory.getCount("medicine")); enemyView.setText(enemy==null?"当前安全，可以继续探索。":"敌人："+enemy.getName()+"  生命："+enemy.getHp()+"/"+enemy.getMaxHp()+"\n攻击："+enemy.getAttack()+"  防御："+enemy.getDefense());}
}