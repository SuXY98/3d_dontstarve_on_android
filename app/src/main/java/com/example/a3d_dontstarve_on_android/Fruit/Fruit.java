package com.example.a3d_dontstarve_on_android.Fruit;

import android.content.Context;
import android.opengl.Matrix;

import com.example.a3d_dontstarve_on_android.GlobalTimer;
import com.example.a3d_dontstarve_on_android.Utils.Gl2Utils;
import com.example.a3d_dontstarve_on_android.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static android.opengl.Matrix.multiplyMM;

public class Fruit {
    private List<ObjFilterFruit> filters;
    private float displayAngle;

    public Fruit(Context context) {
        List<Obj3DFruit> model= ObjReaderFruit.readMultiObj(context,"assets/fruit/file.obj");
        filters=new ArrayList<>();
        for (int i=0;i<model.size();i++){
            ObjFilterFruit f=new ObjFilterFruit(context.getResources());
            f.setObj3D(model.get(i));
            filters.add(f);
        }
        for (ObjFilterFruit f:filters){
            f.create();
        }
    }

    public void draw(float[] VPMatrix,Vector3f position) {
        float[] MVPMatrix = new float[16];
       float rTime = (GlobalTimer.getCurrentMS()/10);
        for (ObjFilterFruit f:filters){
            float[] matrix= Gl2Utils.getOriginalMatrix();
            Matrix.translateM(matrix, 0, position.x, position.y, position.z);
            Matrix.rotateM(matrix, 0, rTime, 0, 1, 0);
            Matrix.scaleM(matrix,0,0.002f,0.002f,0.002f);
            multiplyMM(MVPMatrix,0,VPMatrix,0,matrix,0);
            f.setMatrix(MVPMatrix);
            f.draw();
        }
    }
}
