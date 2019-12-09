package com.example.a3d_dontstarve_on_android.World;

import android.content.Context;

import com.example.a3d_dontstarve_on_android.R;
import com.example.a3d_dontstarve_on_android.Vector3f;

import util.ShaderProgram;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_BINDING_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniform3f;
import static android.opengl.GLES20.glUniformMatrix4fv;

public class WorldShaderProgram extends ShaderProgram {
    private int mMatrix;
    private int mTexture;

    private int vKa;
    private int vKd;
    private int vKs;
    private int mCamera;
    private int mLight;
    public WorldShaderProgram(Context context){
        super(context, R.raw.model_vertex_shader,
                R.raw.model_fragment_shader);

        mMatrix = glGetUniformLocation(program, "vMatrix");
        mTexture = glGetUniformLocation(program, "vTexture");

        //光照明参数
        mLight = glGetUniformLocation(program, "lightLocation");
        mCamera = glGetUniformLocation(program, "camera");
        vKa = glGetUniformLocation(program, "vKa");
        vKs = glGetUniformLocation(program, "vKs");
        vKd = glGetUniformLocation(program, "vKs");

        //默认设置
        float [] ident = {
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        };
        glUniformMatrix4fv(mMatrix, 1, false, ident, 0);

    }

    public int getShaderProgramID(){
        return program;
    }

    public boolean setModelView(float [] matrix){
        if(matrix.length != 16)
            return false;
        glUniformMatrix4fv(mMatrix, 1, false, matrix, 0);
        return true;
    }

    public void setLightModel(Vector3f lightLoc, Vector3f camLoc){
        glUniform3f(mLight, lightLoc.x, lightLoc.y, lightLoc.z);
        glUniform3f(mCamera, camLoc.x, camLoc.y, camLoc.z);
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
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_BINDING_2D, textureID);
        glUniform1i(mTexture, 0);
        return true;
    }
}
