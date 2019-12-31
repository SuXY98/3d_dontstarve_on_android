package com.example.a3d_dontstarve_on_android.Tree;

import android.content.Context;
import android.opengl.Matrix;

import com.example.a3d_dontstarve_on_android.Monster.Obj3DMonster;
import com.example.a3d_dontstarve_on_android.Monster.ObjFilterMonster;
import com.example.a3d_dontstarve_on_android.Monster.ObjReaderMonster;
import com.example.a3d_dontstarve_on_android.Utils.Gl2Utils;
import com.example.a3d_dontstarve_on_android.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static android.opengl.Matrix.multiplyMM;

public class Tree {
    private List<ObjFilterMonster> filters;
    private float displayAngle;

    public Tree(Context context) {
        List<Obj3DMonster> model= ObjReaderMonster.readMultiObj(context,"assets/tree/file.obj");
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

    public void draw(float[] VPMatrix, Vector3f position) {
        float[] MVPMatrix = new float[16];
        for (ObjFilterMonster f:filters){
            float[] matrix= Gl2Utils.getOriginalMatrix();
            Matrix.translateM(matrix, 0, position.x, position.y, position.z);
            Matrix.scaleM(matrix,0,0.02f,0.02f,0.02f);
            multiplyMM(MVPMatrix,0,VPMatrix,0,matrix,0);
            f.setMatrix(MVPMatrix);
            f.draw();
        }
    }
}
