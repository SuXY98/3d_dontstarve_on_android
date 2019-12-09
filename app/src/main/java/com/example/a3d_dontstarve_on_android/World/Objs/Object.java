package com.example.a3d_dontstarve_on_android.World.Objs;


import android.renderscript.Matrix4f;

import com.example.a3d_dontstarve_on_android.Vector3f;

public class Object {
    private int ModelID;
    private Matrix4f modelViewMat;

    public Object(int ModelID){
        this.ModelID = ModelID;
        this.modelViewMat = new Matrix4f();
    }

    public void setModelViewMat(Matrix4f modelViewMat) {
        this.modelViewMat = modelViewMat;
    }

    public int getModelID(){
        return ModelID;
    }

    public Matrix4f getModelViewMat(){
        return modelViewMat;
    }
}
