package com.example.a3d_dontstarve_on_android.Interface;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.ByteBuffer;

import data.VertexAarray;

import static android.opengl.GLES20.GL_UNSIGNED_BYTE;
import static android.opengl.GLES20.glDrawElements;

public class StateBar {
    private static final int POSITION_COMPONENT_COUNT = 3;
    private final ByteBuffer indexArray;
    private final VertexAarray vertexAarray;
    private float color[] = {0.1f, 0.1f, 0.1f, 1.0f};
    private InterfaceShaderProgram mProgram;

    public StateBar(Context context) {
        mProgram = new InterfaceShaderProgram(context);
        // Create a unit cube.
        vertexAarray = new VertexAarray(new float[] {
            0.1f,  0.0f,  0.0f,
                    0.0f,  0.1f,  0.0f,
                    0.1f,  0.1f,  0.0f,
                    0.0f,  0.8f,  0.0f,
                    0.1f,  0.8f,  0.0f,
                    0.1f,  0.9f,  0.0f,
                    4.4f,  0.0f,  0.0f,
                    4.4f,  0.1f,  0.0f,
                    4.5f,  0.1f,  0.0f,
                    4.4f,  0.8f,  0.0f,
                    4.4f,  0.9f,  0.0f,
                    4.5f,  0.8f,  0.0f,

                0.1f,  -1.1f,  0.0f,
                0.0f,  -1.0f,  0.0f,
                0.1f,  -1.0f,  0.0f,
                0.0f,  -0.3f,  0.0f,
                0.1f,  -0.3f,  0.0f,
                0.1f,  -0.2f,  0.0f,
                4.4f,  -1.1f,  0.0f,
                4.4f,  -1.0f,  0.0f,
                4.5f,  -1.0f,  0.0f,
                4.4f,  -0.3f,  0.0f,
                4.4f,  -0.2f,  0.0f,
                4.5f,  -0.3f,  0.0f
        });

        // 6 indices per cube side
        indexArray =  ByteBuffer.allocateDirect(72)
                .put(new byte[] {
                        0, 1, 2,
                        1, 2, 3,
                        2, 3, 4,
                        3, 4, 5,
                        0, 2, 6,
                        2, 6, 7,
                        6, 7, 8,
                        7, 8, 9,
                        8, 9, 11,
                        9, 10, 11,
                        4, 5, 9,
                        5, 9, 10,

                        12, 13, 14,
                        13, 14, 15,
                        14, 15, 16,
                        15, 16, 17,
                        12, 14, 18,
                        14, 18, 19,
                        18, 19, 20,
                        19, 20, 21,
                        20, 21, 23,
                        21, 22, 23,
                        16, 17, 21,
                        17, 21, 22
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
        glDrawElements(GLES20.GL_TRIANGLES, 72, GL_UNSIGNED_BYTE, indexArray);
    }
}
