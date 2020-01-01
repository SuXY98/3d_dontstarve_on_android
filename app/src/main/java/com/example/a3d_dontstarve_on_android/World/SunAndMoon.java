package com.example.a3d_dontstarve_on_android.World;

import android.opengl.Matrix;
import android.renderscript.Matrix4f;

import com.example.a3d_dontstarve_on_android.GlobalTimer;

public class SunAndMoon {
    private Matrix4f sunPos;
    private Matrix4f moonPos;
    public SunAndMoon(){
        sunPos = new Matrix4f();
        moonPos = new Matrix4f();
    }

    public Matrix4f getSunPos(){
        updateLoc();
        return sunPos;
    }

    public Matrix4f getMoonPos() {
        updateLoc();
        return moonPos;
    }

    private void updateLoc(){
        Matrix.setIdentityM(sunPos.getArray(), 0);
        Matrix.setIdentityM(moonPos.getArray(), 0);
        float percentage = GlobalTimer.getProcess();
        Matrix.rotateM(sunPos.getArray(), 0,360 * percentage, 0, 1, 0  );
        Matrix.rotateM(moonPos.getArray(), 0,360 * percentage + 180, 0, 1, 0  );

        Matrix.translateM(sunPos.getArray(), 0, 210, 0, 0);
        Matrix.translateM(moonPos.getArray(), 0, 210, 0, 0);

        Matrix.scaleM(sunPos.getArray(), 0, 5,5,5);
        Matrix.scaleM(moonPos.getArray(), 0, 5, 5, 5);
    }
}
