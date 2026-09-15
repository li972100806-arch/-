package com.jianghu.offline;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    private Player player;
    private Inventory inventory;
    private Enemy enemy;
    private JianghuEngine game;
    private TextView status, enemyView, log, map, skill, equip, pet, extra;
    private Button autoButton;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean autoBattle;

    private final Runnable autoLoop = new Runnable() {
        @Override public void run() {
            if (!autoBattle) return;
            if (player.getHp() <= 0) {
                autoBattle = false;
                autoButton.setText("自动战斗：关闭");
                refresh("自动战斗停止：侠客生命耗尽。");
                return;
            }
            if (enemy == null || !enemy.isAlive()) explore();
            if (enemy != null && enemy.isAlive()) attack();
            if (autoBattle) handler.postDelayed(this, 900);
        }
    };

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);
        status=findViewById(R.id.tvStatus); enemyView=findViewById(R.id.tvEnemy); log=findViewById(R.id.tvLog);
        map=findViewById(R.id.tvMap); skill=findViewById(R.id.tvSkill); equip=findViewById(R.id.tvEquip);
        pet=findViewById(R.id.tvPet); extra=findViewById(R.id.tvExtra); autoButton=findViewById(R.id.btnAuto);
        inventory=new Inventory(); player=SaveManager.load(this,inventory);
        if(player==null){player=new Player("无名侠客");inventory.add("medicine",2);}
        game=new JianghuEngine(); game.load(this);
        int m=game.applyOffline(player); refresh(m>0?"离线挂机回来，已获得"+m+"分钟收益。":"踏入江湖，开始你的独行之路。");
        findViewById(R.id.btnExplore).setOnClickListener(v->explore());
        autoButton.setOnClickListener(v->toggleAutoBattle());
        findViewById(R.id.btnAttack).setOnClickListener(v->attack()); findViewById(R.id.btnRest).setOnClickListener(v->rest());
        findViewById(R.id.btnMedicine).setOnClickListener(v->buy()); findViewById(R.id.btnUse).setOnClickListener(v->use());
        findViewById(R.id.btnSkill).setOnClickListener(v->skill()); findViewById(R.id.btnEquip).setOnClickListener(v->craft());
        findViewById(R.id.btnPet).setOnClickListener(v->pet()); findViewById(R.id.btnOutfit).setOnClickListener(v->outfit());
        findViewById(R.id.btnMap).setOnClickListener(v->nextMap()); findViewById(R.id.btnSect).setOnClickListener(v->sect());
        findViewById(R.id.btnMarket).setOnClickListener(v->market()); findViewById(R.id.btnArtifact).setOnClickListener(v->artifact());
        findViewById(R.id.btnTower).setOnClickListener(v->tower()); findViewById(R.id.btnTravel).setOnClickListener(v->travel());
        findViewById(R.id.btnBounty).setOnClickListener(v->bounty()); findViewById(R.id.btnRetreat).setOnClickListener(v->retreat());
        findViewById(R.id.btnModify).setOnClickListener(v->modify()); findViewById(R.id.btnSave).setOnClickListener(v->save());
        findViewById(R.id.btnReset).setOnClickListener(v->reset());
    }

    private void toggleAutoBattle(){
        autoBattle=!autoBattle; autoButton.setText(autoBattle?"自动战斗：开启":"自动战斗：关闭");
        handler.removeCallbacks(autoLoop);
        if(autoBattle){refresh("自动战斗已开启，将持续探索并战斗。");handler.post(autoLoop);}else{refresh("自动战斗已停止。");save();}
    }
    private void explore(){if(player.getHp()<=0)return;enemy=game.spawn(player);refresh("你来到【"+game.maps[game.mapIndex]+"】，遭遇"+enemy.getName()+"。 ");}
    private void attack(){
        if(enemy==null||!enemy.isAlive()||player.getHp()<=0)return;
        int d=game.damage(player,enemy); enemy.damage(d);
        if(!enemy.isAlive()){
            player.addExp(enemy.getExp());player.setSilver(player.getSilver()+enemy.getSilver());boolean drop=game.drop(inventory);game.petExp++;
            if(game.petExp>=game.petLevel*10){game.petExp=0;game.petLevel++;} enemy=null;
            refresh("胜利！获得经验与银两"+(drop?"，并掉落锻造材料。":"。"));
        }else{CombatEngine.enemyAttack(player,enemy);refresh("你造成"+d+"点伤害，敌人反击。 ");}
        save();
    }
    private void rest(){player.setHp(player.getMaxHp());refresh("休息恢复生命。 ");save();}
    private void buy(){if(player.getSilver()>=20){player.setSilver(player.getSilver()-20);inventory.add("medicine",1);refresh("买入金疮药。 ");save();}else refresh("银两不足。");}
    private void use(){if(inventory.remove("medicine",1)){player.setHp(Math.min(player.getMaxHp(),player.getHp()+50));refresh("服下金疮药。 ");save();}else refresh("没有金疮药。");}
    private void skill(){if(game.trainSkill(player))refresh("功法修炼成功："+game.skills[(game.skillLevel-1)/10]+" Lv."+game.skillLevel);else refresh("银两不足或功法已达上限。");save();}
    private void craft(){if(game.craft(player,inventory))refresh("完成装备打造，强化值 +"+game.equipmentPower);else refresh("锻造需要2份材料。");save();}
    private void pet(){if(game.trainPet(player))refresh("灵宠修炼完成，等级："+game.petLevel);else refresh("银两不足，无法修炼灵宠。");save();}
    private void outfit(){if(game.upgradeOutfit(player))refresh("锦衣升级成功，等级："+game.outfitLevel);else refresh("银两不足，无法升级锦衣。");save();}
    private void nextMap(){if(game.mapIndex+1<game.maps.length&&player.getLevel()>=game.mapReq[game.mapIndex+1]){game.mapIndex++;refresh("解锁新地图：【"+game.maps[game.mapIndex]+"】。 ");save();}else refresh("下一地图需要等级 "+(game.mapIndex+1<game.maps.length?game.mapReq[game.mapIndex+1]:999)+"。 ");}
    private void sect(){if(game.upgradeSect(player))refresh("门府升级成功，等级："+game.sectLevel);else refresh("银两不足，无法升级门府。");save();}
    private void market(){if(game.marketSkill(player))refresh("坊市购得功法残页，功法提升。 ");else refresh("银两不足。");save();}
    private void artifact(){if(game.temperArtifact(player))refresh("法器淬体成功，等级："+game.artifactLevel);else refresh("银两不足。");save();}
    private void tower(){if(game.tower(player))refresh("幻塔突破第"+game.towerFloor+"层，获得奖励。 ");else refresh("当前等级不足，无法挑战幻塔下一层。 ");save();}
    private void travel(){boolean reward=game.travel(player);refresh(reward?"游历完成一轮，获得大量奖励。":"游历进度 +10%。");save();}
    private void bounty(){refresh(game.bounty(player));save();}
    private void retreat(){refresh(game.retreat(player));save();}

    private void modify(){
        final String[] labels={"银两","等级","经验","生命","最大生命","攻击","防御","功法等级","装备强化","灵宠等级","门府等级","法器等级","锦衣等级","幻塔层数","侠义值"};
        final EditText[] fields=new EditText[labels.length]; LinearLayout box=new LinearLayout(this);box.setOrientation(LinearLayout.VERTICAL);box.setPadding(35,10,35,5);
        int[] values={player.getSilver(),player.getLevel(),player.getExp(),player.getHp(),player.getMaxHp(),player.getAttack(),player.getDefense(),game.skillLevel,game.equipmentPower,game.petLevel,game.sectLevel,game.artifactLevel,game.outfitLevel,game.towerFloor,game.honor};
        for(int i=0;i<labels.length;i++){EditText e=new EditText(this);e.setHint(labels[i]);e.setSingleLine(true);e.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED);e.setText(String.valueOf(values[i]));fields[i]=e;box.addView(e);}
        new AlertDialog.Builder(this).setTitle("江湖修改菜单").setMessage("仅本地离线存档生效，可直接调整货币与属性").setView(box).setNegativeButton("取消",null).setPositiveButton("应用修改",(d,w)->{
            try{player.setSilver(Integer.parseInt(fields[0].getText().toString()));player.setLevel(Integer.parseInt(fields[1].getText().toString()));player.setExp(Integer.parseInt(fields[2].getText().toString()));player.setMaxHp(Integer.parseInt(fields[4].getText().toString()));player.setHp(Integer.parseInt(fields[3].getText().toString()));player.setAttack(Integer.parseInt(fields[5].getText().toString()));player.setDefense(Integer.parseInt(fields[6].getText().toString()));game.skillLevel=Math.max(1,Math.min(60,Integer.parseInt(fields[7].getText().toString())));game.equipmentPower=Math.max(0,Integer.parseInt(fields[8].getText().toString()));game.petLevel=Math.max(1,Integer.parseInt(fields[9].getText().toString()));game.sectLevel=Math.max(1,Integer.parseInt(fields[10].getText().toString()));game.artifactLevel=Math.max(0,Integer.parseInt(fields[11].getText().toString()));game.outfitLevel=Math.max(0,Integer.parseInt(fields[12].getText().toString()));game.towerFloor=Math.max(0,Integer.parseInt(fields[13].getText().toString()));game.honor=Math.max(0,Integer.parseInt(fields[14].getText().toString()));save();refresh("修改成功，新的属性已经保存。");}catch(Exception e){refresh("修改失败：请输入有效数字。");}
        }).show();
    }
    private void save(){SaveManager.save(this,player,inventory);game.save(this);}
    private void reset(){autoBattle=false;handler.removeCallbacks(autoLoop);autoButton.setText("自动战斗：关闭");SaveManager.clear(this);getSharedPreferences("jianghu_extra",MODE_PRIVATE).edit().clear().apply();player=new Player("无名侠客");inventory.clear();inventory.add("medicine",2);enemy=null;game=new JianghuEngine();game.load(this);refresh("新的江湖人生开始了。 ");save();}
    private void refresh(String msg){
        status.setText("侠客："+player.getName()+"\n等级："+player.getLevel()+" 经验："+player.getExp()+"/"+player.getExpToNext()+"\n生命："+player.getHp()+"/"+player.getMaxHp()+"\n攻击："+player.getAttack()+" 防御："+player.getDefense()+"\n银两："+player.getSilver()+" 材料："+inventory.getCount("material")+" 药："+inventory.getCount("medicine"));
        map.setText("地图："+game.maps[game.mapIndex]+"\n解锁等级："+game.mapReq[game.mapIndex]+" 已开放："+(game.mapIndex+1)+"/"+game.maps.length);
        skill.setText("功法："+game.skills[(game.skillLevel-1)/10]+" Lv."+game.skillLevel+"\n修炼消耗："+(game.skillLevel*35)+"两");
        equip.setText("装备：江湖旅装\n强化：+"+game.equipmentPower+" 打造：2材料");
        pet.setText("灵宠：青灵狐\n等级："+game.petLevel+" 进度："+game.petExp+"/"+(game.petLevel*10));
        extra.setText("门府 Lv."+game.sectLevel+" · 法器 Lv."+game.artifactLevel+" · 锦衣 Lv."+game.outfitLevel+"\n幻塔："+game.towerFloor+"层 · 游历："+game.travel+"% · 悬赏："+game.bounty+"/3 · 侠义："+game.honor);
        enemyView.setText(enemy==null?"当前安全，可以继续探索。":"敌人："+enemy.getName()+" 生命："+enemy.getHp()+"/"+enemy.getMaxHp()+"\n攻击："+enemy.getAttack()+" 防御："+enemy.getDefense());log.setText(msg);
    }
    @Override protected void onPause(){save();super.onPause();}
    @Override protected void onDestroy(){handler.removeCallbacks(autoLoop);super.onDestroy();}
}
