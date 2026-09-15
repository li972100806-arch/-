package com.jianghu.offline;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
    private Player player;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        status = findViewById(R.id.tvStatus);
        Button create = findViewById(R.id.btnCreate);
        Button save = findViewById(R.id.btnSave);
        Button heal = findViewById(R.id.btnHeal);
        Button reset = findViewById(R.id.btnReset);

        player = SaveManager.load(this);
        if (player == null) player = new Player("无名侠客");
        refresh();

        create.setOnClickListener(v -> {
            player = new Player("无名侠客");
            SaveManager.save(this, player);
            refresh();
        });

        save.setOnClickListener(v -> {
            SaveManager.save(this, player);
            refresh();
        });

        heal.setOnClickListener(v -> {
            player.setHp(player.getMaxHp());
            SaveManager.save(this, player);
            refresh();
        });

        reset.setOnClickListener(v -> {
            SaveManager.clear(this);
            player = new Player("无名侠客");
            refresh();
        });
    }

    private void refresh() {
        status.setText("侠客：" + player.getName()
                + "\n等级：" + player.getLevel()
                + "\n生命：" + player.getHp() + "/" + player.getMaxHp()
                + "\n攻击：" + player.getAttack()
                + "\n防御：" + player.getDefense()
                + "\n银两：" + player.getSilver()
                + "\n\n这是完全离线的本地存档。");
    }
}
