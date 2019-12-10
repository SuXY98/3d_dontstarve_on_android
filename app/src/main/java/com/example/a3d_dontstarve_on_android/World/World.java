package com.example.a3d_dontstarve_on_android.World;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.renderscript.Matrix4f;

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
        Object object = new Object(0);
        float [] mModelView = new float[16];
        Matrix.setIdentityM(mModelView, 0);

        Matrix.rotateM(mModelView, 0, mModelView, 0, 45, 0, 0, 1);
        Matrix.scaleM(mModelView, 0, mModelView, 0, 0.5f, 0.5f, 0.5f);
        Matrix.translateM(mModelView, 0, mModelView, 0, 0, 0, -5);

        object.setModelViewMat(new Matrix4f(mModelView));
        objs.add(object);
    }

    public void renderWorld(WorldShaderProgram shader, Matrix4f projection){
        // 获取片段着色器的颜色的句柄
        int mColorHandler = GLES20.glGetUniformLocation(shader.getShaderProgramID(), "aColor");
        // 设置绘制三角形的颜色
        GLES20.glUniform4f(mColorHandler,1,1,1,1);
        for(int i = 0; i < objs.size(); i++) {
            //set model view matrix
            float[] vp = new float[16];
            Matrix.multiplyMM(vp, 0, projection.getArray(), 0, objs.elementAt(i).getModelViewMat().getArray(), 0);
            if(!shader.setModelView(vp))
                return;
            //set other options
            shader.setMaterial(models.elementAt(objs.elementAt(i).getModelID()).getK());
            //draw model
            models.elementAt(objs.elementAt(i).getModelID()).drawSelf(shader.getShaderProgramID());
        }
    }
}
