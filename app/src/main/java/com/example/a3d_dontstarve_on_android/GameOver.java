package com.example.a3d_dontstarve_on_android;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class GameOver extends Activity {
    private ImageView gameOver;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.game_over);
        gameOver = findViewById(R.id.game_over);
        gameOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GameOver.this,GameStart.class);
                startActivity(intent);
            }
        });

        Intent intent = new Intent();
        intent.setAction("con.lcry.close.activity");
        sendBroadcast(intent);
    }
}
