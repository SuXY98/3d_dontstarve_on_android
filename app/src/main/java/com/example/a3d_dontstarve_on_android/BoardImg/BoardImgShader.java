package com.example.a3d_dontstarve_on_android.BoardImg;

import android.content.Context;
import android.util.Log;

import com.example.a3d_dontstarve_on_android.R;
import com.example.a3d_dontstarve_on_android.Vector3f;

import util.LoggerConfig;
import util.MatrixHelper;
import util.ShaderProgram;
import util.TextureHelper;


import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.translateM;


public class BoardImgShader extends ShaderProgram {
    private static final String TAG = "BoardShader";
    private final int uMatrixLocation;
    private final int uTextureUnitLocation;
    private final int aPositionLocation;
    private final int aUVLoacation;

    private int boardTexture;
    private float [] finalMatrix = new float[16];
    private float [] mboardMatrix = new float[16];

    public BoardImgShader(Context context,int resourceID){
        super(context, R.raw.boardimg_v,
                R.raw.boardimg_f);

        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);

        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aUVLoacation = glGetAttribLocation(program,A_TEXTURE_COORDINATES);

        boardTexture = TextureHelper.loadTexture(context, resourceID);
    }

    public void setUniforms(float[] vmatrix, float[] pmatrix, float [] mmatrix, Vector3f N,Vector3f objPos) {
        processBoardMMatrix(N,objPos);

        multiplyMM(finalMatrix,0,vmatrix,0,mboardMatrix,0);
        multiplyMM(finalMatrix,0,pmatrix,0,finalMatrix,0);

        glUniformMatrix4fv(uMatrixLocation, 1, false,finalMatrix, 0);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, boardTexture);
        glUniform1i(uTextureUnitLocation,0);
    }

    private void processBoardMMatrix(Vector3f N,Vector3f pos){
        MatrixHelper.setIdent(mboardMatrix);
        double cos = N.dot(0,0,1);
        double angle = Math.acos(cos);
        float floatangle = (float)angle*57.29578f;
//        if(LoggerConfig.ON ){
//            Log.w(TAG," N" + N.x+N.y+N.z+"   rotate "+floatangle);
//        }
        rotateM(mboardMatrix,0,mboardMatrix,0,floatangle,0,1,0);
        translateM(mboardMatrix,0,mboardMatrix,0,pos.x,pos.y,pos.z);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getTextureCoodAttributeLocation(){
        return aUVLoacation;
    }
}
