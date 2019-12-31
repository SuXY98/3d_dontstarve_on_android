package com.example.a3d_dontstarve_on_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;



import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    private MyGLSurfaceView mGlSurfaceView;
    private CloseActivityReceiver closeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mGlSurfaceView = new MyGLSurfaceView(this);
        setContentView(mGlSurfaceView);

        closeReceiver = new CloseActivityReceiver();
        IntentFilter intentFilter = new IntentFilter("con.lcry.close.activity");
        registerReceiver(closeReceiver, intentFilter);
    }

    public class CloseActivityReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent){
            MainActivity.this.finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(closeReceiver);
    }
}
