package com.example.a3d_dontstarve_on_android.Nurbs;


import android.content.Context;

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
import static android.opengl.Matrix.scaleM;

import com.example.a3d_dontstarve_on_android.R;

import util.MatrixHelper;
import util.ShaderProgram;
import util.TextureHelper;

public class NurbsShader extends ShaderProgram {
    private final int uMatrixLocation;
    private final int aPositionLocation;
    private final int uTextureUnitLocation;
    private int terrainTexture;
    private final int factor_location;

    private float [] finalMatrix = new float[16];
    private float [] mMatrix = new float[16];
    public NurbsShader(Context context) {
        super(context, R.raw.nurbs_v,
                R.raw.nurbs_f);

        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        factor_location = glGetUniformLocation(program, "factor");
        terrainTexture = TextureHelper.loadTexture(context, R.drawable.grass);

    }

    public void setUniforms(float[] vmatrix,float[] pmatrix,float factor) {
        MatrixHelper.setIdent(mMatrix);
        scaleM(mMatrix,0,2,2,2);
        multiplyMM(finalMatrix,0,pmatrix,0,vmatrix,0);
        multiplyMM(finalMatrix,0,finalMatrix,0,mMatrix,0);
        glUniformMatrix4fv(uMatrixLocation, 1, false, finalMatrix, 0);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, terrainTexture);
        glUniform1i(uTextureUnitLocation,0);
        glUniform1f(factor_location,factor);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

}
