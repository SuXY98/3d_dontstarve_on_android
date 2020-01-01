package com.example.a3d_dontstarve_on_android.Skybox;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE1;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.Matrix.multiplyMM;

import android.content.Context;

import com.example.a3d_dontstarve_on_android.R;

import util.ShaderProgram;
import util.TextureHelper;

public class SkyboxShaderProgram extends ShaderProgram {
    private final int uMatrixLocation;
    private final int uTextureUnitLocation;
    private final int uTextureUnitLocation_n;
    private final int aPositionLocation;
    private final int factor_location;

    private int skyboxTexture;
    private int nightskyboxTexture;
    private float [] vieproMatrix = new float[16];
    private float [] skyboxViewMatric = new float [16];

    public SkyboxShaderProgram(Context context){
        super(context, R.raw.skybox_vertex_shader,
                R.raw.skybox_fragment_shader);

        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        uTextureUnitLocation_n = glGetUniformLocation(program, "u_TextureUnit_n");
        factor_location = glGetUniformLocation(program, "factor");

        nightskyboxTexture = TextureHelper.loadCubeMap(context,
                new int []{R.drawable.leftn,R.drawable.rightn,
                        R.drawable.bottomn,R.drawable.topn,
                        R.drawable.frontn,R.drawable.backn});
        skyboxTexture = TextureHelper.loadCubeMap(context,
                new int []{R.drawable.left, R.drawable.right,
                        R.drawable.bottom, R.drawable.top,
                        R.drawable.front, R.drawable.back});
    }

    public void setUniforms(float[] vmatrix,float[] pmatrix,float factor) {
        System.out.println(factor);
        processSkyboxT(vmatrix);
        multiplyMM(vieproMatrix,0,pmatrix,0,skyboxViewMatric,0);

        glUniformMatrix4fv(uMatrixLocation, 1, false,vieproMatrix, 0);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_CUBE_MAP, skyboxTexture);
        glUniform1i(uTextureUnitLocation, 0);
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_CUBE_MAP,nightskyboxTexture);
        glUniform1i(uTextureUnitLocation_n, 1);
        glUniform1f(factor_location,factor);
    }

    private void processSkyboxT(float[] vmatrix){
        for(int i = 0;i < 16;i++){
            skyboxViewMatric[i] = vmatrix[i];
        }
        skyboxViewMatric[3] = 0;
        skyboxViewMatric[7] = 0;
        skyboxViewMatric[11] = 0;
        skyboxViewMatric[12] = 0;
        skyboxViewMatric[13] = 0;
        skyboxViewMatric[14] = 0;
        skyboxViewMatric[15] = 0;
        //System.out.print(skyboxViewMatric[3]);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

}
