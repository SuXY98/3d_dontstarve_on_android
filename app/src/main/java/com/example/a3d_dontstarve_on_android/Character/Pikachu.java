package com.example.a3d_dontstarve_on_android.Character;

import android.content.Context;
import android.opengl.Matrix;

import com.example.a3d_dontstarve_on_android.Utils.Gl2Utils;
import com.example.a3d_dontstarve_on_android.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static android.opengl.Matrix.multiplyMM;
import static com.example.a3d_dontstarve_on_android.GlobalTimer.Timer;
import static com.example.a3d_dontstarve_on_android.GlobalTimer.getCurrentMS;

public class Pikachu {
    private List<ObjFilter2> filters;
    public Camera mCamera;
    private float displayAngle;

    public Pikachu(Context context) {
        List<Obj3D> model=ObjReader.readMultiObj(context,"assets/character/pikachu.obj");
        filters=new ArrayList<>();
        for (int i=0;i<model.size();i++){
            ObjFilter2 f=new ObjFilter2(context.getResources());
            f.setObj3D(model.get(i));
            filters.add(f);
        }
        for (ObjFilter2 f:filters){
            f.create();
        }
        mCamera = new Camera();
    }

    public void changeDisplayAngle(int direction) {
        if (direction==1) {
            displayAngle = mCamera.getYaw();
        }
        else if (direction==2) {
            displayAngle = mCamera.getYaw()+90;
        }
        else if (direction==3) {
            displayAngle = mCamera.getYaw()+180;
        }
        else if (direction==4) {
            displayAngle = mCamera.getYaw()+270;
        }
    }

    public void draw(float[] VPMatrix) {
        float[] MVPMatrix = new float[16];
        Vector3f position = mCamera.getPikachuPos();
        float jumpTime = (getCurrentMS()% 900) / 5;
        for (ObjFilter2 f:filters){
            float[] matrix= Gl2Utils.getOriginalMatrix();
            Matrix.translateM(matrix, 0, position.x, position.y, position.z);
            //System.out.println(mCamera.getYaw());
            Matrix.translateM(matrix, 0, 0, 0.3f * (float)Math.abs(Math.cos(Math.toRadians(jumpTime))), 0);
            Matrix.rotateM(matrix, 0, 90-displayAngle, 0, 1, 0);
            Matrix.scaleM(matrix,0,0.005f,0.005f,0.005f);
            multiplyMM(MVPMatrix,0,VPMatrix,0,matrix,0);
            f.setMatrix(MVPMatrix);
            f.draw();
        }
    }
}
