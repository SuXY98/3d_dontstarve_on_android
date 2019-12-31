package com.example.a3d_dontstarve_on_android;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class GameStart extends Activity {
    private ImageView gameStart;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.game_start);
        gameStart = findViewById(R.id.game_start);
        gameStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GameStart.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}