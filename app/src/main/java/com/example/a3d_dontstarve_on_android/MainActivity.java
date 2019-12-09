package com.example.a3d_dontstarve_on_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
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
    private Button btn;
    private LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mGlSurfaceView = new MyGLSurfaceView(this);
        setContentView(mGlSurfaceView);
    }
}
