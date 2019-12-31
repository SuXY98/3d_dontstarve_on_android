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

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.glBindTexture;
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
    int []depthMap;
    private boolean isShadowBinded;
    public World(Context context){
        this.context = context;
        objs = new Vector<>();
        models = new Vector<>();
        depthMap = new int[1];
        GLES20.glGenTextures(1, depthMap, 0);
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
        render(shader, vpMatrix, false, true);
    }

    private void render(WorldShaderProgram shader, float[] vpMatrix, boolean drawTex, boolean drawShadow){
        if(shader.isFail()){
            System.out.println("Shader is Broken, render world exit...\n");
            return;
        }
        if(drawShadow){
////            if(!isShadowBinded){
//                //System.out.println("Shadow has not generated yet\n");
//                InitShadow(shader);
////                isShadowBinded = true;
////            }
            shader.setShadow(true);
        }else{
            shader.setShadow(false);
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
                e.printStackTrace();
            }
        }
    }

    /*
    * This method will create a shadow map with current scene, it will clear depth bit and color bit
    * If you want to open shadow, must call this method before rendering
    * */
    public void InitShadow(WorldShaderProgram shader, int wWidth, int wHeight){
        int [] depthMapFBO =  new int[1];
        glGenFramebuffers(1, depthMapFBO, 0);
        int SHADOW_WIDTH = 1024, SHADOW_HEIGHT = 1024;

        glBindTexture(GL_TEXTURE_2D, depthMap[0]);
        GLES20.glTexImage2D(GL_TEXTURE_2D, 0, GLES20.GL_DEPTH_COMPONENT,
                SHADOW_WIDTH, SHADOW_HEIGHT, 0, GLES20.GL_DEPTH_COMPONENT, GLES20.GL_FLOAT, null);
        GLES20.glTexParameteri(GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, depthMapFBO[0]);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthMap[0], 0);
        glViewport(0, 0, SHADOW_WIDTH, SHADOW_HEIGHT);
        glClear(GL_DEPTH_BUFFER_BIT);
        try{
            render(shader, computeLightMat(shader).getArray(), false, false);
        }catch(Exception e){
            e.printStackTrace(); //Actually never happen
        }
        glViewport(0, 0, wWidth, wHeight);
        glBindTexture(GL_TEXTURE_2D, depthMap[0]);
        //glClear(GL_DEPTH_BUFFER_BIT);
    }

    private Matrix4f computeLightMat(WorldShaderProgram shader)throws  Exception{
        if(!shader.isLightParallel()){
            throw new Exception("Point-light shadow is not supported yet");
        }
        Vector3f cameraLoc = shader.getCamLoc();
        Vector3f lightPosition = shader.getLightPosition();
        float near_plane = 1.0f, far_plane = 30f; //default set
        Matrix4f tMat = new Matrix4f();
        //We define light space based on a 20*20sqrt(2) rectangle
        Matrix.orthoM(tMat.getArray(), 0, -10.0f, 10.0f, -10.0f, 10.0f, near_plane, far_plane);
        //set light look to camera, and up is (0,1,0)
        Matrix4f lMat = new Matrix4f();
        lightPosition = lightPosition.plus(cameraLoc);
        Matrix.setLookAtM(lMat.getArray(), 0,
                lightPosition.x , lightPosition.y, lightPosition.z,
                cameraLoc.x, cameraLoc.y, cameraLoc.z,
                0, 1, 0
                );
        Matrix.multiplyMM(tMat.getArray(), 0, tMat.getArray(), 0, lMat.getArray(), 0);
        return new Matrix4f();
    }
}
