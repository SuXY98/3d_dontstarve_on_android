package com.example.a3d_dontstarve_on_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {
    private GLSurfaceView glSurfaceView;
    private boolean rendererSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        glSurfaceView = new GLSurfaceView(this);
        //check whether support gles2.0
        final ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo =
                activityManager.getDeviceConfigurationInfo();

        final boolean supportEs2 =
                configurationInfo.reqGlEsVersion >= 0x20000
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
                        && (Build.FINGERPRINT.startsWith("generic")
                        || Build.FINGERPRINT.startsWith("unknown")
                        || Build.MODEL.contains("google_sdk")
                        || Build.MODEL.contains("Emulator")
                        || Build.MODEL.contains("Android SDK built for x86")));
        final MyRenderer skyboxRender = new MyRenderer(this);
        if(supportEs2){
            glSurfaceView.setEGLContextClientVersion(2);
            //Assign render
            glSurfaceView.setRenderer(skyboxRender);
            rendererSet = true;
        }else{
            Toast.makeText(this,"This device does not support OpenglEs 2.0",
                    Toast.LENGTH_LONG).show();
            return;
        }
        setContentView(glSurfaceView);
    }

    //handle life cycle events, or will crash
    //when switch to another app
    @Override
    protected void onPause(){
        super.onPause();
        if(rendererSet){
            glSurfaceView.onPause();
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        if(rendererSet){
            glSurfaceView.onResume();
        }
    }
}
