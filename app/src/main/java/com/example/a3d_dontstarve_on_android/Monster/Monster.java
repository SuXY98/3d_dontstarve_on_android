package com.example.a3d_dontstarve_on_android.Monster;

import android.content.Context;
import android.opengl.Matrix;

import com.example.a3d_dontstarve_on_android.Utils.Gl2Utils;
import com.example.a3d_dontstarve_on_android.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static android.opengl.Matrix.multiplyMM;

public class Monster {
    private List<ObjFilterMonster> filters;
    private float displayAngle;

    public Monster(Context context) {
        List<Obj3DMonster> model= ObjReaderMonster.readMultiObj(context,"assets/monster/pumpkin.obj");
        filters=new ArrayList<>();
        for (int i=0;i<model.size();i++){
            ObjFilterMonster f=new ObjFilterMonster(context.getResources());
            f.setObj3D(model.get(i));
            filters.add(f);
        }
        for (ObjFilterMonster f:filters){
            f.create();
        }
    }

    public void draw(float[] VPMatrix) {
        float[] MVPMatrix = new float[16];
        Vector3f position = new Vector3f(1, 0, 0);
//        float jumpTime = (GlobalTimer.getCurrentMS()% 900) / 5;
        for (ObjFilterMonster f:filters){
            float[] matrix= Gl2Utils.getOriginalMatrix();
            Matrix.translateM(matrix, 0, position.x, position.y, position.z);
            //System.out.println(mCamera.getYaw());
//            Matrix.translateM(matrix, 0, 0, 0.3f * (float)Math.abs(Math.cos(Math.toRadians(jumpTime))), 0);
//            Matrix.rotateM(matrix, 0, 90-displayAngle, 0, 1, 0);
            Matrix.scaleM(matrix,0,0.02f,0.02f,0.02f);
            multiplyMM(MVPMatrix,0,VPMatrix,0,matrix,0);
            f.setMatrix(MVPMatrix);
            f.draw();
        }
    }
}
