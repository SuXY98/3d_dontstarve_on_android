package com.example.a3d_dontstarve_on_android.World;

import android.opengl.Matrix;
import android.renderscript.Matrix4f;

import com.example.a3d_dontstarve_on_android.Vector3f;
import com.example.a3d_dontstarve_on_android.World.Objs.BaseModel;
import com.example.a3d_dontstarve_on_android.World.Objs.CubeModel;
import com.example.a3d_dontstarve_on_android.World.Objs.Object;

import java.util.Vector;

public class World {
    Vector<BaseModel> models;
    Vector<Object> objs;

    public World(){
        objs = new Vector<>();
        models = new Vector<>();
        models.add(new CubeModel(false));
        objs.add(new Object(0));
    }

    public void renderWorld(WorldShaderProgram shader, Matrix4f projection){
        for(int i = 0; i < objs.size(); i++){
            //set model view matrix
            float []vp = new float[16];
            Matrix.multiplyMM(vp, 0, projection.getArray(), 0,  objs.elementAt(i).getModelViewMat().getArray(), 0);
            shader.setModelView(vp);
            //set other options
            shader.setMaterial(models.elementAt(objs.elementAt(i).getModelID()).getK());
            //draw model
            models.elementAt(objs.elementAt(i).getModelID()).drawSelf(shader.getShaderProgramID());
        }
    }
}
