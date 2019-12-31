package com.example.a3d_dontstarve_on_android.World;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.renderscript.Matrix4f;

import com.example.a3d_dontstarve_on_android.Vector3f;
import com.example.a3d_dontstarve_on_android.World.Objs.BallModel;
import com.example.a3d_dontstarve_on_android.World.Objs.BaseModel;
import com.example.a3d_dontstarve_on_android.World.Objs.CubeModel;
import com.example.a3d_dontstarve_on_android.World.Objs.Object;

import java.nio.Buffer;
import java.util.Vector;

import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.GL_DEPTH_ATTACHMENT;
import static android.opengl.GLES20.GL_FRAMEBUFFER;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glBindFramebuffer;
import static android.opengl.GLES20.glFramebufferTexture2D;
import static android.opengl.GLES20.glGenFramebuffers;
import static android.opengl.GLES20.glViewport;

public class World {
    Vector<BaseModel> models;
    Vector<Object> objs;
    Context context;
    private boolean isShadowBinded;
    public World(Context context){
        this.context = context;
        objs = new Vector<>();
        models = new Vector<>();
        BaseModel m = new BallModel(1.0f);
        m.setK(
                new Vector3f[]{
                    new Vector3f(0.1f, 0.1f, 0.1f),
                        new Vector3f(0.5f, 0.5f, 0.5f),
                        new Vector3f(0.3f, 0.3f, 0.3f),
                }
        );
        models.add(m);
        Object object = new Object(0);
        float [] mModelView = new float[16];
        Matrix.setIdentityM(mModelView, 0);
        object.setModelViewMat(new Matrix4f(mModelView));

        objs.add(object);
    }

    public void renderWorld(WorldShaderProgram shader, float[] vpMatrix) {
        render(shader, vpMatrix, true, false);
    }

    private void render(WorldShaderProgram shader, float[] vpMatrix, boolean drawTex, boolean drawShadow){
        if(shader.isFail()){
            System.out.println("Shader is Broken, render world exit...\n");
            return;
        }
        if(drawShadow){
            if(!isShadowBinded){
                System.out.println("Shadow has not generated yet\n");
            }else{
                shader.setShadow(true);
            }
        }
        // 获取片段着色器的颜色的句柄
        int mColorHandler = GLES20.glGetUniformLocation(shader.getShaderProgramID(), "aColor");
        // 设置绘制三角形的颜色
        GLES20.glUniform4f(mColorHandler,1,1,1,1);
        for(int i = 0; i < objs.size(); i++) {
            //set model view projection matrix
            if(!shader.setViewProjection(vpMatrix))
                return;
            shader.setModelMatrix(objs.elementAt(i).getModelViewMat());
            //set other options
            shader.setMaterial(models.elementAt(objs.elementAt(i).getModelID()).getK());
            if(drawTex && models.elementAt(i).hasTexture())
                shader.setTexture(models.elementAt(i).textureID());
            else
                shader.setTexture(-1);
            //draw model
            try {
                models.elementAt(objs.elementAt(i).getModelID()).drawSelf(shader.getShaderProgramID());
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    public void InitShadow(WorldShaderProgram shader, Vector3f lightPosition, boolean isParallel){
        int [] depthMapFBO =  new int[1];
        glGenFramebuffers(1, depthMapFBO, 0);
        int SHADOW_WIDTH = 1024, SHADOW_HEIGHT = 1024;
        int []depthMap = new int[1];
        GLES20.glGenTextures(1, depthMap, 0);
        GLES20.glBindTexture(GL_TEXTURE_2D, depthMap[0]);
        GLES20.glTexImage2D(GL_TEXTURE_2D, 0, GLES20.GL_DEPTH_COMPONENT,
                SHADOW_WIDTH, SHADOW_HEIGHT, 0, GLES20.GL_DEPTH_COMPONENT, GLES20.GL_FLOAT, null);
        GLES20.glTexParameteri(GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

        glViewport(0, 0, SHADOW_WIDTH, SHADOW_HEIGHT);
        glBindFramebuffer(GL_FRAMEBUFFER, depthMapFBO[0]);
        glClear(GLES20.GL_DEPTH_BUFFER_BIT);
        render(shader, computeLightMat(shader, lightPosition, isParallel).getArray(), false, false);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, depthMapFBO[0]);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthMap[0], 0);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    private Matrix4f computeLightMat(WorldShaderProgram shader, Vector3f lightPosition, boolean isParallel){
        Matrix4f tMat = new Matrix4f();
        Matrix.perspectiveM(tMat.getArray(), 0, 90, 1, shader.zNear, shader.zFar);
        Matrix4f lMat = new Matrix4f();
        Matrix.setLookAtM(lMat, 0,
                lightPosition.x, lightPosition.y, lightPosition.z,

                );
        Matrix.multiplyMM(tMat.getArray(), 0, tMat.getArray(), 0, );
        return new Matrix4f();
    }
}
