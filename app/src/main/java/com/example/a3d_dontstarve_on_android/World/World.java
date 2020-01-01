package com.example.a3d_dontstarve_on_android.World;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.renderscript.Matrix4f;

import com.example.a3d_dontstarve_on_android.Vector3f;
import com.example.a3d_dontstarve_on_android.World.Objs.BallModel;
import com.example.a3d_dontstarve_on_android.World.Objs.BaseModel;
import com.example.a3d_dontstarve_on_android.World.Objs.CubeModel;
import com.example.a3d_dontstarve_on_android.World.Objs.Object;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Vector;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_EXTENSIONS;
import static android.opengl.GLES20.GL_LEQUAL;
import static android.opengl.GLES20.GL_NONE;
import static android.opengl.GLES20.GL_TEXTURE;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE1;
import static android.opengl.GLES20.GL_TEXTURE_BINDING_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.GL_DEPTH_ATTACHMENT;
import static android.opengl.GLES20.GL_FRAMEBUFFER;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glBindFramebuffer;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glFramebufferTexture2D;
import static android.opengl.GLES20.glGenFramebuffers;
import static android.opengl.GLES20.glGetError;
import static android.opengl.GLES20.glGetString;
import static android.opengl.GLES20.glViewport;


public class World {
    Vector<BaseModel> models;
    Vector<Object> objs;
    Context context;
    int [] fboId;
    int [] depthTextureId;
    int [] renderTextureId;
    SunAndMoon sunAndMoon;
    private boolean isShadowBinded;

    public World(Context context){
        this.context = context;
        objs = new Vector<>();
        models = new Vector<>();
        fboId = new int[1];
        depthTextureId = new int[1];
        renderTextureId = new int[1];
        // create a framebuffer object
        GLES20.glGenFramebuffers(1, fboId, 0);
        GLES20.glGenTextures(1, renderTextureId, 0);
        // create render buffer and bind 16-bit depth buffer
        GLES20.glGenRenderbuffers(1, depthTextureId, 0);
        BaseModel m = new BallModel(1.0f);
        sunAndMoon = new SunAndMoon();

        m.setK(
                new Vector3f[]{
                    new Vector3f(1f, 1f, 1f),
                        new Vector3f(0f, 0f, 0f),
                        new Vector3f(0f, 0f, 0f),
                }
        );
        m.openTexture = false;
        models.add(m);

        //sun
        objs.add(new Object(0));
        objs.elementAt(0).setObjColor(new Vector3f(1, 0, 0));
        objs.elementAt(0).setModelViewMat(sunAndMoon.getSunPos());
        objs.elementAt(0).drawShadow = false;

        //moon
        objs.add(new Object(0));
        objs.elementAt(1).setObjColor(new Vector3f(0.9f,0.9f,0.9f));
        objs.elementAt(1).setModelViewMat(sunAndMoon.getMoonPos());
        objs.elementAt(1).drawShadow = false;
    }

    public void renderWorld(WorldShaderProgram shader, float[] vpMatrix) {
        objs.elementAt(0).setModelViewMat(sunAndMoon.getSunPos());
        objs.elementAt(1).setModelViewMat(sunAndMoon.getMoonPos());
        render(shader, vpMatrix, false, true);
    }
    private Bitmap readBufferPixelToBitmap(int width, int height) {
        ByteBuffer buf = ByteBuffer.allocateDirect(width * height * 4);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        GLES20.glReadPixels(0, 0, width, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, buf);
        buf.rewind();

        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bmp.copyPixelsFromBuffer(buf);
        return bmp;
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
            //GLES20.glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, renderTextureId[0]);
            shader.setShadowMap(renderTextureId[0]);
            //GLES20.glActiveTexture(GL_TEXTURE0);
        }
        // 获取片段着色器的颜色的句柄
        int mColorHandler = GLES20.glGetUniformLocation(shader.getShaderProgramID(), "aColor");
        // 设置绘制三角形的颜色
        for(Object obj : objs) {
            obj.setUpSelf(mColorHandler);
            //set model view projection matrix
            if(!shader.setViewProjection(vpMatrix))
                return;
            shader.setModelMatrix(obj.getModelViewMat());
            //set other options
            shader.setMaterial(models.elementAt(obj.getModelID()).getK());
            if(drawTex && models.elementAt(obj.getModelID()).hasTexture())
                shader.setTexture(models.elementAt(obj.getModelID()).textureID());
            else
                shader.setTexture(-1);
            //set shadow
            if(drawShadow && obj.drawShadow){
                shader.setShadow(true);
            }else{
                shader.setShadow(false);
            }
            //draw model
            try {
                models.elementAt(obj.getModelID()).drawSelf(shader.getShaderProgramID());
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

        //glActiveTexture(GL_TEXTURE1);

        int SHADOW_WIDTH = 1024, SHADOW_HEIGHT = 1024;

        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, depthTextureId[0]);
        GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16,SHADOW_WIDTH, SHADOW_HEIGHT);
        // Try to use a texture depth component

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, renderTextureId[0]);
        // GL_LINEAR does not make sense for depth texture. However, next tutorial shows usage of GL_LINEAR and PCF. Using GL_NEAREST
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        // Remove artifact on the edges of the shadowmap
        GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE );
        GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE );

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId[0]);
        GLES20.glTexImage2D( GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, SHADOW_WIDTH, SHADOW_HEIGHT, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
        // specify texture as color attachment
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, renderTextureId[0], 0);
        // attach the texture to FBO depth attachment point
        // (not supported with gl_texture_2d)
        GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_RENDERBUFFER, depthTextureId[0]);


        //GLES20.glCullFace(GLES20.GL_FRONT);// Bind the shadow map texture now...
        glViewport(0, 0, SHADOW_WIDTH, SHADOW_HEIGHT);
        glClear(GL_DEPTH_BUFFER_BIT);
        try{
            //rendering scene in the light space
            render(shader, computeLightMat(shader).getArray(), false, false);
        }catch(Exception e){
            e.printStackTrace(); //Actually never happen
        }
        glViewport(0, 0, wWidth, wHeight);
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
        Bitmap test = readBufferPixelToBitmap(SHADOW_WIDTH, SHADOW_HEIGHT);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        //glActiveTexture(GL_TEXTURE0);
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

    public boolean isCollided(int objID1, int objID2){
        Vector3f [] box1 = objs.elementAt(objID1).getBox(models.elementAt(objs.elementAt(objID1).getModelID()).getBox());
        return objs.elementAt(objID2).isCollided(models.elementAt(objs.elementAt(objID2).getModelID()).getBox(), box1);
    }
}
