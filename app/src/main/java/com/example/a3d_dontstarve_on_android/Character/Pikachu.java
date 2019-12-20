package com.example.a3d_dontstarve_on_android.Character;

import android.content.Context;
import android.opengl.Matrix;

import com.example.a3d_dontstarve_on_android.Utils.Gl2Utils;

import java.util.ArrayList;
import java.util.List;

import static android.opengl.Matrix.multiplyMM;

public class Pikachu {
    private List<ObjFilter2> filters;

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
    }

    public void onSurfaceChanged(int width, int height) {
        for (ObjFilter2 f:filters){
            float[] matrix= Gl2Utils.getOriginalMatrix();
            Matrix.translateM(matrix,0,0,-0.3f,0);
            Matrix.scaleM(matrix,0,0.002f,0.002f*width/height,0.002f);
            f.setMatrix(matrix);
        }
    }

    public void draw(float[] VPMatrix) {
        float[] MVPMatrix = new float[16];
        for (ObjFilter2 f:filters){
            float[] matrix= Gl2Utils.getOriginalMatrix();
            Matrix.scaleM(matrix,0,0.0015f,0.0015f,0.0015f);
            multiplyMM(MVPMatrix,0,VPMatrix,0,matrix,0);
            f.setMatrix(MVPMatrix);
            f.draw();
        }
    }
}
