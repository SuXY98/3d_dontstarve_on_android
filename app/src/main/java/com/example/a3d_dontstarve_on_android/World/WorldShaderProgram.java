package com.example.a3d_dontstarve_on_android.World;

import android.content.Context;
import android.opengl.GLES20;
import android.renderscript.Matrix4f;

import com.example.a3d_dontstarve_on_android.R;
import com.example.a3d_dontstarve_on_android.Vector3f;

import util.ShaderProgram;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_BINDING_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniform3f;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;

public class WorldShaderProgram extends ShaderProgram {
    private int mMatrix;
    private int vpMatrix;
    private int mTexture;
    private int hasShadow;
    private int vKa;
    private int vKd;
    private int vKs;
    private int mCamera;
    private int mLight;
    private int hasTexture;
    private int shininess;
    private int lightColor;
    private Vector3f lightPos;
    private boolean isParallel;
    private Vector3f camLoc;
    private int shadowMap;
    private int parallelHandler;
    public float zNear;
    public float zFar;

    public WorldShaderProgram(Context context){
        super(context, R.raw.model_vertex_shader,
                R.raw.model_fragment_shader);
        if(program == 0){
            //build fail
            return;
        }
        mMatrix = glGetUniformLocation(program, "mMatrix");
        vpMatrix = glGetUniformLocation(program, "vpMatrix");
        mTexture = glGetUniformLocation(program, "vTexture");
        lightColor = glGetUniformLocation(program, "lightColor");
        //光照明参数
        mLight = glGetUniformLocation(program, "lightLocation");
        mCamera = glGetUniformLocation(program, "camera");
        vKa = glGetUniformLocation(program, "vKa");
        vKs = glGetUniformLocation(program, "vKs");
        vKd = glGetUniformLocation(program, "vKd");
        hasTexture = glGetUniformLocation(program, "hasTexture");
        shininess =  glGetUniformLocation(program, "shininess");
        hasShadow = glGetUniformLocation(program, "hasShadow");
        shadowMap = glGetUniformLocation(program, "shadowMap");

        parallelHandler = glGetUniformLocation(program, "isParallel");

        //默认设置
        float [] ident = {
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        };
        //glUniformMatrix4fv(mMatrix, 1, false, ident, 0);
        //glUniformMatrix4fv(vpMatrix, 1, false, ident, 0);
        zNear = 0.1f;
        zFar = 10f;
    }

    public int getShaderProgramID(){
        return program;
    }

    public boolean setViewProjection(float [] matrix){
        if(matrix.length != 16)
            return false;
        GLES20.glUniformMatrix4fv(vpMatrix, 1, false, matrix, 0);
        return true;
    }

    public void setModelMatrix(Matrix4f mMat){
        GLES20.glUniformMatrix4fv(mMatrix, 1, false, mMat.getArray(), 0);
    }

    public void setLightModel(Vector3f lightLoc, Vector3f camLoc, boolean isParallel){
        this.lightPos = lightLoc;
        this.isParallel = isParallel;
        this.camLoc = camLoc;
        glUniform4f(mLight, lightLoc.x, lightLoc.y, lightLoc.z, 1);
        glUniform3f(mCamera, camLoc.x, camLoc.y, camLoc.z);
        glUniform1i(parallelHandler, isParallel?1:0);
    }

    public void setLightColor(Vector3f lColor){
        float [] color = new float[]{lColor.x, lColor.y, lColor.z};
        GLES20.glUniform3fv(lightColor, 1, color, 0);
    }

    public Vector3f getLightPosition(){
        return  this.lightPos;
    }

    public boolean isLightParallel(){
        return this.isParallel;
    }
    public void setShininess(float s){
        glUniform1f(shininess, s);
    }

    public void setMaterial(Vector3f [] K){
        if(K.length != 3)
            return;
        Vector3f Ka = K[0];
        Vector3f Ks = K[1];
        Vector3f Kd = K[2];
        glUniform3f(vKa, Ka.x, Ka.y, Ka.z);
        glUniform3f(vKs, Ks.x, Ks.y, Ks.z);
        glUniform3f(vKd, Kd.x, Kd.y, Kd.z);
    }

    public boolean setTexture(int textureID){
        if(textureID < 0){
            glUniform1i(hasTexture, 0);
            return true;
        }
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureID);
        glUniform1i(mTexture, 0);
        glUniform1i(hasTexture, 1);
        return true;
    }


    public boolean isFail(){
        return program == 0;
    }

    public void setShadow(boolean state){
        glUniform1i(hasShadow, state?1:0);
    }

    public void setShadowMap(int ID){
        glUniform1i(shadowMap, ID);
    }

    public Vector3f getCamLoc(){
        return camLoc;
    }
}
