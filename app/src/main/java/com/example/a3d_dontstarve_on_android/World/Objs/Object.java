package com.example.a3d_dontstarve_on_android.World.Objs;


import android.opengl.GLES20;
import android.opengl.Matrix;
import android.renderscript.Matrix4f;
import android.util.Log;

import com.example.a3d_dontstarve_on_android.Vector3f;


public class Object {
    private int ModelID;
    private Matrix4f modelViewMat;
    private Vector3f mColor;
    public boolean drawShadow;
    public Object(int ModelID){
        this.ModelID = ModelID;
        this.modelViewMat = new Matrix4f();
        this.mColor = new Vector3f(1,1,1);
        this.drawShadow = true;
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

    public void setObjColor(Vector3f vColor){
        this.mColor = vColor;
    }

    public void setUpSelf(int colorHandler){
        GLES20.glUniform4f(colorHandler,mColor.x,mColor.y,mColor.z,1);
    }

    //提供一个世界坐标系的Box以及模型对应的Box,返回这个Box是否与物体相交.
    public boolean isCollided(Vector3f []modelBox,  Vector3f [] box){
        Vector3f []selfBox = getBox(modelBox);
        //calculate center
        Vector3f thisCenter = selfBox[0].plus(selfBox[1]).scale(0.5f);
        Vector3f mCenter = box[0].plus(box[1]).scale(0.5f);
        Vector3f delta = mCenter.plus(thisCenter.reverse());
        Vector3f tSize = modelBox[0].plus(modelBox[1].reverse());
        Vector3f mSize = box[0].plus(box[1].reverse());
        Vector3f halfSum = tSize.scale(0.5f).plus(mSize.scale(0.5f));
        if(delta.x < 0)delta.x = -delta.x;
        if(delta.y < 0)delta.y = -delta.y;
        if(delta.z < 0)delta.z = -delta.z;

        //两个Box相交的充要条件是中心距离小于Box三边的一半长度的和
        return delta.x < halfSum.x && delta.y < halfSum.y && delta.z < halfSum.z;
    }

    public Vector3f [] getBox(Vector3f [] modelBox){
        if(modelBox.length != 2){
            Log.println(Log.ERROR,"Error1", "Box length not equal 2!");
            return null;
        }
        //calc collision
        //model center
        Vector3f mCenter = modelBox[0].plus(modelBox[1]).scale(0.5f);
        float [] r1 = new float [4];
        float [] r2 = new float [4];

        Matrix.multiplyMV(r1, 0, this.modelViewMat.getArray(), 0, modelBox[0].toArray(), 0);
        Matrix.multiplyMV(r2, 0, this.modelViewMat.getArray(), 0, modelBox[1].toArray(), 0);
        r1[0] /= r1[3];r2[0] /= r2[3];
        r1[1] /= r1[3];r2[1] /= r2[3];
        r1[2] /= r1[3];r2[2] /= r2[3];
        //重新计算Box
        Vector3f min = new Vector3f(
                r1[0] < r2[0]?r1[0]:r2[0],
                r1[1] < r2[1]?r1[1]:r2[1],
                r1[2] < r2[2]?r1[2]:r2[2]
        );
        Vector3f max = new Vector3f(
                r1[0] > r2[0]?r1[0]:r2[0],
                r1[1] > r2[1]?r1[1]:r2[1],
                r1[2] > r2[2]?r1[2]:r2[2]
        );
        return new Vector3f[]{max, min};
    }
}
