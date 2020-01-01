package com.example.a3d_dontstarve_on_android.Terrain;

import android.content.Context;

import com.example.a3d_dontstarve_on_android.R;
import com.example.a3d_dontstarve_on_android.Vector3f;

import util.ShaderProgram;
import util.TextureHelper;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.Matrix.multiplyMM;

public class TerrainShader extends ShaderProgram {
    private final int uMatrixLocation;
    private final int uTextureUnitLocation;
    private final int aPositionLocation;
    private final int aUVLoacation;
    private final int factor_location;

    private int terrainTexture;
    private float [] finalMatrix = new float[16];

    public TerrainShader(Context context, int resourceID){
        super(context, R.raw.terrain_v,
                R.raw.terrain_f);

        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);
        factor_location = glGetUniformLocation(program, "factor");

        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aUVLoacation = glGetAttribLocation(program,A_TEXTURE_COORDINATES);

        terrainTexture = TextureHelper.loadTexture(context, resourceID);
    }

    public void setUniforms(float[] vmatrix, float[] pmatrix, float [] mmatrix,float factor) {

        multiplyMM(finalMatrix,0,vmatrix,0,mmatrix,0);
        multiplyMM(finalMatrix,0,pmatrix,0,finalMatrix,0);

        glUniformMatrix4fv(uMatrixLocation, 1, false,finalMatrix, 0);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, terrainTexture);
        glUniform1i(uTextureUnitLocation,0);
        glUniform1f(factor_location,factor);
    }
    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getTextureCoodAttributeLocation(){
        return aUVLoacation;
    }
}
