package com.example.a3d_dontstarve_on_android.Interface;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import data.VertexAarray;

import static android.opengl.GLES20.GL_UNSIGNED_BYTE;
import static android.opengl.GLES20.glDrawElements;

public class ArrowButton {
    private static final int POSITION_COMPONENT_COUNT = 3;
    private final ByteBuffer indexArray;
    private final VertexAarray vertexAarray;
    private float color[] = {1.0f, 1.0f, 1.0f, 1.0f};
    private ArrowBottonShaderProgram mProgram;

    public ArrowButton(Context context) {
        mProgram = new ArrowBottonShaderProgram(context);
        // Create a unit cube.
        vertexAarray = new VertexAarray(new float[]{
                -0.5f,  0.0f,  0.0f,
                0.0f,  0.5f,  0.0f,
                0.0f, -0.5f,  0.0f,
                0.0f, 0.3f,  0.0f,
                0.0f, -0.3f,  0.0f,
                0.5f, -0.3f,  0.0f,
                0.5f, 0.3f,  0.0f,
        });

        // 6 indices per cube side
        indexArray =  ByteBuffer.allocateDirect(9)
                .put(new byte[] {
                    0, 1, 2,
                    3, 4, 5,
                    3, 6, 5
                });
        indexArray.position(0);
    }

    public void bindData() {
        vertexAarray.setVertexAttribPointer(0,
                mProgram.getVertexAttrLocation(),
                POSITION_COMPONENT_COUNT, 0);
    }

    public void draw(float[] mvpMatrix) {
        mProgram.useProgram();
        mProgram.setColor(color);
        mProgram.setMVPMatrix(mvpMatrix);
        vertexAarray.setVertexAttribPointer(0,
                mProgram.getVertexAttrLocation(),
                POSITION_COMPONENT_COUNT, 0);
        glDrawElements(GLES20.GL_TRIANGLES, 9, GL_UNSIGNED_BYTE, indexArray);
    }

}
